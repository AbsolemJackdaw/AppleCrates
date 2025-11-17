package jackdaw.applecrates.container;

import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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

    void loadInventoryData(ValueInput input);

    void saveInventoryData(ValueOutput output);

    int getCratestacksTotalItemCount(Item item);

    boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop);
}
