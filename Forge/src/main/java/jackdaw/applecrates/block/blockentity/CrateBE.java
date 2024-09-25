package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CommonCrateBlock;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateBE extends CommonCrateBE {

    private final LazyOptional<IItemHandler> crateStockHopper;

    public CrateBE(CrateWoodType type, BlockPos pos, BlockState state, StackHandlerAdapter adapter) {
        super(type, pos, state, adapter);
        crateStockHopper = LazyOptional.of(() -> adapter.crateStock);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // do not read super here, for the same reason as handleUpdateTag !!
        loadCrateDataFromTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        //do not call super here. it uses the load method from above, but we're not sending all the same data here !
        loadCrateDataFromTag(tag);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (getBlockState().getValue(CommonCrateBlock.FACING).equals(side) && cap == ForgeCapabilities.ITEM_HANDLER) {
            return crateStockHopper.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        crateStockHopper.invalidate();
        super.invalidateCaps();
    }

    public CrateStackHandler getCrateStock() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.crateStock;
        return new CrateStackHandler();
    }

    public ItemStackHandler getInteractable() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.interactableTradeSlots;
        return new ItemStackHandler(2);
    }

    public ItemStackHandler getPriceAndSale() {
        if (stackHandler instanceof StackHandlerAdapter stackHandlerAdapter)
            return stackHandlerAdapter.savedTradeSlots;
        return new ItemStackHandler(2);
    }
}
