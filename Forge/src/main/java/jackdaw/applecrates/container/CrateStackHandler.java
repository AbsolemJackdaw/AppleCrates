package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class CrateStackHandler extends ItemStackHandler {

    public static final String TAGSTOCK = "stocked";

    public CrateStackHandler() {
        super(30);
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
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot == 29)
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == 29)
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot != 29 && super.isItemValid(slot, stack);
    }
}
