package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CrateBE extends BlockEntity {

    public static final String TAGOWNER = "owner";
    public static final String TAGSTOCK = "cratestock";
    public static final String TAGINTERACTABLE = "interactable";
    public static final String TAGPRICESALE = "pricensale";
    public static final String TAGUNLIMITED = "isUnlimited";
    public CrateStackHandler crateStock = new CrateStackHandler();
    public ItemStackHandler interactable = new ItemStackHandler(2);
    public ItemStackHandler priceAndSale = new ItemStackHandler(2);
    public boolean isUnlimitedShop = false;
    private UUID owner;

    public CrateBE(CrateWoodType type, BlockPos pos, BlockState state) {
        super(CrateWoodType.getBlockEntityType(type), pos, state);
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

    /**
     * sync on login : getUpdateTag / handleUpdateTag
     */
    @Override
    public CompoundTag getUpdateTag() {
        return saveCrateDataToTag(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //do not call super here. it uses the load method from above, but we're not sending all the same data here !
        loadCrateDataFromTag(tag);
    }

    /**
     * sync on data change
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);// just defers to getUpdateTag
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // do not read super here, for the same reason as handleUpdateTag !!
        loadCrateDataFromTag(pkt.getTag());
    }

    private CompoundTag saveCrateDataToTag(CompoundTag tag) {
        tag.put(TAGSTOCK, crateStock.serializeNBT());
        tag.put(TAGINTERACTABLE, interactable.serializeNBT());
        tag.put(TAGPRICESALE, priceAndSale.serializeNBT());
        tag.putBoolean(TAGUNLIMITED, isUnlimitedShop);
        if (owner != null)
            tag.putUUID(TAGOWNER, owner);
        return tag;
    }

    private void loadCrateDataFromTag(CompoundTag tag) {
        crateStock.deserializeNBT((CompoundTag) tag.get(TAGSTOCK));
        interactable.deserializeNBT((CompoundTag) tag.get(TAGINTERACTABLE));
        priceAndSale.deserializeNBT((CompoundTag) tag.get(TAGPRICESALE));
        if (tag.contains(TAGUNLIMITED))
            isUnlimitedShop = tag.getBoolean(TAGUNLIMITED);
        if (tag.contains(TAGOWNER))
            owner = tag.getUUID(TAGOWNER);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(ServerPlayer player) {
        this.owner = player.getGameProfile().getId();
    }

    //defaults to true without owner to prevent unbreakable blocks, even though the owner should always be set
    public boolean isOwner(Player player) {
        return owner == null || player != null && owner.equals(player.getGameProfile().getId());
    }
}
