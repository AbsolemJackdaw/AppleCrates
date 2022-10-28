package jackdaw.applecrates.container;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class GenericItemStackHandler extends ItemStackHandler implements GenericInventory {
    public GenericItemStackHandler(int size) {
        super(size);
    }

    //*****Wrapper*****/

    @Override
    public int numberOfSlots() {
        return getSlots();
    }

    @Override
    public ItemStack getItemInSlot(int slot) {
        return getStackInSlot(slot);
    }

    @Override
    public void setItemInSlot(int slot, ItemStack stack) {
        setStackInSlot(slot, stack);
    }
}
