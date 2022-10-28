package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CrateStackHandler extends SimpleContainerNBT implements CrateStockHandler {


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


}
