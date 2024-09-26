package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlockBase;
import jackdaw.applecrates.container.StackHandlerAdapter;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrateBlockEntity extends CrateBlockEntityBase {

    private final LazyOptional<IItemHandler> crateStockHopper;

    public CrateBlockEntity(CrateWoodType type, BlockPos pos, BlockState state) {
        super(type, pos, state, new StackHandlerAdapter());
        crateStockHopper = LazyOptional.of(() -> ((StackHandlerAdapter) this.stackHandler).crateStock);
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
        if (getBlockState().getValue(CrateBlockBase.FACING).equals(side) && cap == ForgeCapabilities.ITEM_HANDLER) {
            return crateStockHopper.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        crateStockHopper.invalidate();
        super.invalidateCaps();
    }
}
