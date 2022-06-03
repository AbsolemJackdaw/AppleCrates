package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CrateBE extends BlockEntity {

    public CrateStackHandler crateStock = new CrateStackHandler();
    public ItemStackHandler interactable = new ItemStackHandler(2);
    public ItemStackHandler priceAndSale = new ItemStackHandler(2);
    private UUID owner;

    public CrateBE(WoodType type, BlockPos pos, BlockState state) {
        super(GeneralRegistry.BE_MAP.get(type).get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadCrateDataFromTag(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveCrateDataToTag(tag);

    }

    //sync on login : getUpdateTag / handleUpdateTag
    @Override
    public CompoundTag getUpdateTag() {
        return saveCrateDataToTag(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //super.handleUpdateTag(tag);//do not call super here. it uses the load mehtod from above, but we're not sending all the same data here !
        loadCrateDataFromTag(tag);
    }

    //sync on data change
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);// just defers to getUpdateTag
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        //super.onDataPacket(net, pkt); // do not read super here, for the same reason as handleUpdateTag !!
        loadCrateDataFromTag(pkt.getTag());
    }

    private CompoundTag saveCrateDataToTag(CompoundTag tag) {
//        tag.put("pricensale", priceAndSale.serializeNBT()); //only send price and sale inventory, this is the one that is used client side for rendering in the besr
        tag.put("cratestock", crateStock.serializeNBT());
        tag.put("interactable", interactable.serializeNBT());
        tag.put("pricensale", priceAndSale.serializeNBT());
        if (owner != null)
            tag.putUUID("owner", owner);
        return tag;
    }

    private void loadCrateDataFromTag(CompoundTag tag) {
        crateStock.deserializeNBT((CompoundTag) tag.get("cratestock"));
        interactable.deserializeNBT((CompoundTag) tag.get("interactable"));
        priceAndSale.deserializeNBT((CompoundTag) tag.get("pricensale"));
        if (tag.contains("owner"))
            owner = tag.getUUID("owner");
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(ServerPlayer player) {
        this.owner = player.getGameProfile().getId();
    }
}
