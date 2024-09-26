package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import jackdaw.applecrates.container.inventory.GenericStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StackHandlerAdapter implements IStackHandlerAdapter {
    public CrateStackHandler crateStock = new CrateStackHandler();
    public GenericStackHandler interactableTrades = new GenericStackHandler(2);
    public GenericStackHandler savedTrades = new GenericStackHandler(2);

    @Override
    public IGenericInventory getInteractableTradeSlots() {
        return interactableTrades;
    }

    @Override
    public IGenericInventory getSavedTradeSlots() {
        return savedTrades;
    }

    @Override
    public ICrateStock getCrateStock() {
        return crateStock;
    }

    @Override
    public ItemStack getInteractableTradeItem(int slot) {
        return interactableTrades.getItem(slot);
    }

    @Override
    public void setInteractableTradeItem(int slot, ItemStack stack) {
        interactableTrades.setItem(slot, stack);
    }

    @Override
    public ItemStack getSavedTradeSlotsItem(int slot) {
        return savedTrades.getItem(slot);
    }

    @Override
    public void setSavedTradeSlotItem(int slot, ItemStack stack) {
        savedTrades.setItem(slot, stack);
    }

    @Override
    public ItemStack getCrateStockItem(int slot) {
        return crateStock.getItem(slot);
    }

    @Override
    public void setCrateStockItem(int slot, ItemStack stack) {
        crateStock.setItem(slot, stack);
    }

    @Override
    public int getCratestacksTotalItemCount(Item item) {
        return crateStock.getCountOfItem(item);
    }

    @Override
    public void saveInventoryData(CompoundTag tag) {
        tag.put(Constants.TAGSTOCK, crateStock.serializeNBT());
        tag.put(Constants.TAGINTERACTABLE, interactableTrades.serializeNBT());
        tag.put(Constants.TAGPRICESALE, savedTrades.serializeNBT());
    }

    @Override
    public void loadInventoryData(CompoundTag tag) {
        crateStock.deserializeNBT((CompoundTag) tag.get(Constants.TAGSTOCK));
        interactableTrades.deserializeNBT((CompoundTag) tag.get(Constants.TAGINTERACTABLE));
        savedTrades.deserializeNBT((CompoundTag) tag.get(Constants.TAGPRICESALE));
    }

    @Override
    public boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop) {
        return crateStock.updateStackInPaymentSlot(payment, unlimitedShop);
    }
}
