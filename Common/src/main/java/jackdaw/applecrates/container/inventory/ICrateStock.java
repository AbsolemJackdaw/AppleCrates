package jackdaw.applecrates.container.inventory;

import net.minecraft.world.item.ItemStack;

public interface ICrateStock extends IGenericInventory {

    boolean updateStackInPaymentSlot(ItemStack stack, boolean isUnlimited);

}
