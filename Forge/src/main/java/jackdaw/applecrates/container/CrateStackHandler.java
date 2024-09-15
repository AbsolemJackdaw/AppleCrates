package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends ItemStackHandler {

    public static final String TAGSTOCK = "stocked";

    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(31);
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

        if (getStackInSlot(getLastSlot()).isEmpty()) {
            prepPay.setCount(1);
            setStackInSlot(getLastSlot(), prepPay);
        }
        if (!ItemStack.isSame(payment, getStackInSlot(getLastSlot())))
            return false;
        ItemStack prepXchange = getStackInSlot(getLastSlot()).copy();
        CompoundTag tag = prepXchange.getOrCreateTag();
        if (tag.contains(TAGSTOCK)) {
            tag.putInt(TAGSTOCK, tag.getInt(TAGSTOCK) + payment.getCount());
        } else {
            tag.putInt(TAGSTOCK, payment.getCount());
        }
        setStackInSlot(getLastSlot(), prepXchange);
        return true;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot == getLastSlot())
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == getLastSlot())
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot != getLastSlot() && super.isItemValid(slot, stack);
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

    public int getLastSlot() {
        return getSlots() - 1;
    }
}
