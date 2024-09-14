package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends SimpleContainerNBT {

    public static final String TAGSTOCK = "stocked";
    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(30);
    }

    public int getCountOfItem(Item item) {
        return this.itemCountCache.computeIfAbsent(item, $ -> {
            int count = 0;
            for (int i = 0; i < this.getContainerSize(); i++) {
                var stack = this.getItem(i);
                if (stack.is(item)) {
                    count += stack.getCount();
                }
            }
            return count;
        });
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return slot != 29;
    }


    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getItem(29).isEmpty()) {
            prepPay.setCount(1);
            setItem(29, prepPay);
        }
        if (!ItemStack.isSame(payment, getItem(29)))
            return false;
        ItemStack prepXchange = getItem(29).copy();
        CompoundTag tag = prepXchange.getOrCreateTag();
        if (tag.contains(TAGSTOCK)) {
            tag.putInt(TAGSTOCK, tag.getInt(TAGSTOCK) + payment.getCount());
        } else {
            tag.putInt(TAGSTOCK, payment.getCount());
        }
        setItem(29, prepXchange);
        return true;
    }
}
