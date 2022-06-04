package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CrateStackHandler extends ItemStackHandler {

    public static final String STOCKED_STR = "stocked";

    public CrateStackHandler() {
        super(30);
    }

    public boolean updateStackInPayementSlot(ItemStack payment) {

        if (getStackInSlot(29).isEmpty()) {
            ItemStack solo = payment.copy();
            solo.setCount(1);
            setStackInSlot(29, solo);
        }
        if (!ItemStack.isSame(payment, getStackInSlot(29)))
            return false;
        ItemStack inSlot = getStackInSlot(29);
        CompoundTag tag = inSlot.getOrCreateTag();
        if (tag.contains(STOCKED_STR)) {
            tag.putInt(STOCKED_STR, tag.getInt(STOCKED_STR) + payment.getCount());
        } else {
            tag.putInt(STOCKED_STR, payment.getCount());
        }
        return true;
    }
}
