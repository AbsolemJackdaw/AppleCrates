package jackdaw.applecrates;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public interface GenericInventory {

    int numberOfSlots();

    ItemStack getItemInSlot(int slot);

    void setItemInSlot(int slot, ItemStack stack);

    Tag serializeNBT();

    void deserializeNBT(CompoundTag tag);
}
