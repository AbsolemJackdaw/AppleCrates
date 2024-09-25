package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CommonCrateBlock;
import jackdaw.applecrates.container.IStackHandlerAdapter;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import jackdaw.applecrates.container.inventory.SimpleContainerNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class CrateBE extends CommonCrateBE implements WorldlyContainer {

    public CrateBE(CrateWoodType type, BlockPos pos, BlockState state, IStackHandlerAdapter handler) {
        super(type, pos, state, handler);
    }

    public CrateStackHandler getCrateStock() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.crateStock;
        return new CrateStackHandler();
    }

    public SimpleContainerNBT getInteractable() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.interactableTrades;
        return new SimpleContainerNBT(2);
    }

    public SimpleContainerNBT getPriceAndSale() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.savedTrades;
        return new SimpleContainerNBT(2);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (getBlockState().getValue(CommonCrateBlock.FACING).equals(direction))
            return IntStream.range(0, Constants.TOTALCRATESTOCKLOTS).toArray();
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return getBlockState().getValue(CommonCrateBlock.FACING).equals(direction);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return Constants.TOTALCRATESTOCKLOTS;
    }

    @Override
    public boolean isEmpty() {
        return getCrateStock().isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return getCrateStock().getItem(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return getCrateStock().removeItem(i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i < Constants.TOTALCRATESTOCKLOTS)
            return getCrateStock().removeItemNoUpdate(i);
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i < Constants.TOTALCRATESTOCKLOTS)
            getCrateStock().setItem(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return getCrateStock().stillValid(player);
    }

    @Override
    public void clearContent() {
        getCrateStock().clearContent();
    }
}
