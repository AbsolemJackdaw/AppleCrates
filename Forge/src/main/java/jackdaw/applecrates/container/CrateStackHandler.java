package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends ItemStackHandler {

    public static final String TAGSTOCK = "stocked";

    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(30);
    }

    public int getCountOfItem(Item item) {
        return this.itemCountCache.computeIfAbsent(item, $ -> {
            int count = 0;
            for (int i = 0; i < this.getSlots(); i++) {
                var stack = this.getStackInSlot(i);
                if (stack.is(item)) {
                    count += stack.getCount();
                }
            }

            return count;
        });
    }

    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getStackInSlot(29).isEmpty()) {
            prepPay.setCount(1);
            setStackInSlot(29, prepPay);
        }
        if (!ItemStack.isSame(payment, getStackInSlot(29)))
            return false;
        ItemStack prepXchange = getStackInSlot(29).copy();
        CompoundTag tag = prepXchange.getOrCreateTag();
        if (tag.contains(TAGSTOCK)) {
            tag.putInt(TAGSTOCK, tag.getInt(TAGSTOCK) + payment.getCount());
        } else {
            tag.putInt(TAGSTOCK, payment.getCount());
        }
        setStackInSlot(29, prepXchange);
        return true;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.itemCountCache.clear();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.itemCountCache.clear();
    }
}
