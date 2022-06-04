package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CrateStackHandler extends ItemStackHandler {

    public static final String TAGSTOCK = "stocked";

    public CrateStackHandler() {
        super(30);
    }

    public boolean updateStackInPayementSlot(ItemStack payment) {
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
}
