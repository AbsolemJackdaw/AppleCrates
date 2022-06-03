package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.items.ItemStackHandler;

public class CrateBE extends BlockEntity {

    public CrateStackHandler crateStock = new CrateStackHandler();
    public ItemStackHandler interactable = new ItemStackHandler(2);
    public ItemStackHandler priceAndSale = new ItemStackHandler(2);

    public CrateBE(WoodType type, BlockPos pos, BlockState state) {
        super(GeneralRegistry.BE_MAP.get(type).get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        crateStock.deserializeNBT((CompoundTag) tag.get("cratestock"));
        interactable.deserializeNBT((CompoundTag) tag.get("interactable"));
        priceAndSale.deserializeNBT((CompoundTag) tag.get("pricensale"));

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("cratestock", crateStock.serializeNBT());
        tag.put("interactable", interactable.serializeNBT());
        tag.put("pricensale", priceAndSale.serializeNBT());

    }
}
