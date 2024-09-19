package jackdaw.applecrates.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IStackHandlerAdapter {
    ItemStack getInteractableItem(int slot);

    ItemStack getPriceAndSaleItem(int slot);

    ItemStack getCratestockItem(int slot);

    void saveInventoryData(CompoundTag tag);

    void loadInventoryData(CompoundTag tag);

    int getCratestacksTotalItemCount(Item item);

    boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop);
}
