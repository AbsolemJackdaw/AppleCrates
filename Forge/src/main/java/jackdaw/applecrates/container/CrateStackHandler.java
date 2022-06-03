package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CrateStackHandler extends ItemStackHandler {

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
        if (tag.contains("stocked")) {
            tag.putInt("stocked", tag.getInt("stocked") + payment.getCount());
        } else {
            tag.putInt("stocked", payment.getCount());
        }
        return true;
    }
}
