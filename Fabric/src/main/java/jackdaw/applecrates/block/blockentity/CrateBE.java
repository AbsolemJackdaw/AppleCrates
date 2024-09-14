package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.container.IStackHandlerAdapter;
import jackdaw.applecrates.container.SimpleContainerNBT;
import jackdaw.applecrates.container.StackHandlerAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CrateBE extends CommonCrateBE {

    public CrateBE(CrateWoodType type, BlockPos pos, BlockState state, IStackHandlerAdapter handler) {
        super(type, pos, state, handler);
    }

    public CrateStackHandler getCrateStock() {
        if (stackHandler instanceof StackHandlerAdapter sha)
            return sha.crateStock;
        return new CrateStackHandler();
    }

    public SimpleContainerNBT getInteractable() {
        if (stackHandler instanceof StackHandlerAdapter sha)
            return sha.interactable;
        return new SimpleContainerNBT(2);
    }

    public SimpleContainerNBT getPriceAndSale() {
        if (stackHandler instanceof StackHandlerAdapter sha)
            return sha.priceAndSale;
        return new SimpleContainerNBT(2);
    }

}
