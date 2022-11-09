package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CrateStackHandler extends SimpleContainerNBT implements CrateStockHandler, WorldlyContainer {


    public CrateStackHandler() {
        super(30);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return slot != 29;
    }

    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        return Constants.CommonCode.updateStackInPaymentSlot(this, payment, isUnlimitedShop);
    }

    //***wrapper***/
    public int getCountOfItem(Item item) {
        return Constants.CommonCode.getCountOfItem(this, item);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        var array = new int[numberOfSlots()];
        for (int i = 0; i < array.length; i++)
            array[i] = i;
        return array;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return i != 29;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return i != 29;
    }
}
