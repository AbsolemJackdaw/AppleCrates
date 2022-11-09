package jackdaw.applecrates.container;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class CrateBEForge extends CrateBE {

    private LazyOptional<IItemHandler> crateStockHopper;

    public CrateBEForge(CrateWoodType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (this.crateStock instanceof IItemHandler handler)
            crateStockHopper = LazyOptional.of(() -> handler);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (getBlockState().getValue(CrateBlock.FACING).equals(side) && crateStockHopper != null) {
            return crateStockHopper.cast();
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        if (crateStockHopper != null)
            crateStockHopper.invalidate();
        super.invalidateCaps();
    }
}
