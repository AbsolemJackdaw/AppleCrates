package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class CrateBlockFabric extends CrateBlock implements WorldlyContainerHolder {

    public CrateBlockFabric(CrateWoodType type) {
        super(type);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor lvl, BlockPos pos) {
        if (lvl.getBlockEntity(pos) instanceof CrateBE be && be.crateStock instanceof WorldlyContainer hopperable) {
            return hopperable;
        }
        return null;
    }
}
