package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlockBase;
import jackdaw.applecrates.container.StackHandlerAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class CrateBlockEntity extends CrateBlockEntityBase {

    private final IItemHandler crateStockHopper;

    public CrateBlockEntity(CrateWoodType type, BlockPos pos, BlockState state) {
        super(type, pos, state, new StackHandlerAdapter());
        crateStockHopper = ((StackHandlerAdapter) this.stackHandler).crateStock;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        // super.onDataPacket(net, pkt, lookupProvider);
        // do not read super here, for the same reason as handleUpdateTag !!
        loadCrateDataFromTag(lookupProvider, pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        //do not call super here. it uses the load method from above, but we're not sending all the same data here !
        loadCrateDataFromTag(lookupProvider, tag);
    }

    public IItemHandler getCapability(Direction side) {
        return getBlockState().getValue(CrateBlockBase.FACING).equals(side) ? crateStockHopper : null;
    }
}
