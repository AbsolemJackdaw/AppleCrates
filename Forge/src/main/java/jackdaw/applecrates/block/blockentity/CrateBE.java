package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.items.ItemStackHandler;

public class CrateBE extends BlockEntity {

    public ItemStackHandler crateInventory = new ItemStackHandler(30);
    public ItemStackHandler crateSales = new ItemStackHandler(3);
    public ItemStackHandler priceAndSale = new ItemStackHandler(2);

    public CrateBE(WoodType type, BlockPos pos, BlockState state) {
        super(GeneralRegistry.BE_MAP.get(type).get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

}
