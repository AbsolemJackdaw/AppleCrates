package jackdaw.applecrates.container.inventory;

import jackdaw.applecrates.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends SimpleContainerNBT implements ICrateStock {

    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(Constants.TOTALCRATESLOTS);
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
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        return slot != Constants.TOTALCRATESTOCKLOTS && super.canPlaceItem(slot, stack);
    }

    @Override
    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) {
            prepPay.setCount(1);
            setItem(Constants.TOTALCRATESTOCKLOTS, prepPay);
        }
        if (!ItemStack.isSameItemSameTags(payment, getItem(Constants.TOTALCRATESTOCKLOTS)))
            return false;
        ItemStack prepXchange = getItem(Constants.TOTALCRATESTOCKLOTS).copy();
        CompoundTag tag = prepXchange.getOrCreateTag();
        if (tag.contains(Constants.TAGSTOCK)) {
            tag.putInt(Constants.TAGSTOCK, tag.getInt(Constants.TAGSTOCK) + payment.getCount());
        } else {
            tag.putInt(Constants.TAGSTOCK, payment.getCount());
        }
        setItem(Constants.TOTALCRATESTOCKLOTS, prepXchange);
        return true;
    }
}
