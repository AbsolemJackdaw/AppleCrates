package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlockBase;
import jackdaw.applecrates.container.StackHandlerAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class CrateBlockEntity extends CrateBlockEntityBase {

    private final ItemStacksResourceHandler crateStockHopper;

    public CrateBlockEntity(CrateWoodType type, BlockPos pos, BlockState state) {
        super(type, pos, state, new StackHandlerAdapter());
        crateStockHopper = ((StackHandlerAdapter) this.stackHandler).crateStock;
    }

    @Override
    public void onDataPacket(Connection net, ValueInput valueInput) {
        // do not read super here, for the same reason as handleUpdateTag !!
        loadCrateDataFromTag(valueInput);
    }

    @Override
    public void handleUpdateTag(ValueInput input) {
        //do not call super here. it uses the load method from above, but we're not sending all the same data here !
        loadCrateDataFromTag(input);
    }

    public StacksResourceHandler<ItemStack, ItemResource> getCapability(Direction side) {
        return getBlockState().getValue(CrateBlockBase.FACING).equals(side) ? crateStockHopper : null;
    }
}
