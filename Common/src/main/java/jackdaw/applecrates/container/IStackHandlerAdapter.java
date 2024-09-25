package jackdaw.applecrates.container;

import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IStackHandlerAdapter {

    IGenericInventory getInteractableTradeSlots();

    IGenericInventory getSavedTradeSlots();

    ICrateStock getCrateStock();


    ItemStack getInteractableTradeItem(int slot);

    void setInteractableTradeItem(int slot, ItemStack stack);

    ItemStack getSavedTradeSlotsItem(int slot);

    void setSavedTradeSlotItem(int slot, ItemStack stack);

    ItemStack getCrateStockItem(int slot);

    void setCrateStockItem(int slot, ItemStack stack);

    void saveInventoryData(CompoundTag tag);

    void loadInventoryData(CompoundTag tag);

    int getCratestacksTotalItemCount(Item item);

    boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop);
}
