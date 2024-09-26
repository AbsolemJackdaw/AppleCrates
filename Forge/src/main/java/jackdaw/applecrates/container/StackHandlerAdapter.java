package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import jackdaw.applecrates.container.inventory.GenericStackHandler;
import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class StackHandlerAdapter implements IStackHandlerAdapter {
    public CrateStackHandler crateStock = new CrateStackHandler();
    public GenericStackHandler interactableTradeSlots = new GenericStackHandler(2);
    public GenericStackHandler savedTradeSlots = new GenericStackHandler(2);

    @Override
    public IGenericInventory getInteractableTradeSlots() {
        return interactableTradeSlots;
    }

    @Override
    public IGenericInventory getSavedTradeSlots() {
        return savedTradeSlots;
    }

    @Override
    public ICrateStock getCrateStock() {
        return crateStock;
    }

    @Override
    public ItemStack getInteractableTradeItem(int slot) {
        return interactableTradeSlots.getStackInSlot(slot);
    }

    @Override
    public void setInteractableTradeItem(int slot, ItemStack stack) {
        interactableTradeSlots.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack getSavedTradeSlotsItem(int slot) {
        return savedTradeSlots.getStackInSlot(slot);
    }

    @Override
    public void setSavedTradeSlotItem(int slot, ItemStack stack) {
        savedTradeSlots.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack getCrateStockItem(int slot) {
        return crateStock.getStackInSlot(slot);
    }

    @Override
    public void setCrateStockItem(int slot, ItemStack stack) {
        crateStock.setStackInSlot(slot, stack);
    }

    @Override
    public int getCratestacksTotalItemCount(Item item) {
        return crateStock.getCountOfItemCached(item);
    }

    @Override
    public void saveInventoryData(CompoundTag tag) {
        tag.put(Constants.TAGSTOCK, crateStock.serializeNBT());
        tag.put(Constants.TAGINTERACTABLE, interactableTradeSlots.serializeNBT());
        tag.put(Constants.TAGPRICESALE, savedTradeSlots.serializeNBT());
    }

    @Override
    public void loadInventoryData(CompoundTag tag) {
        crateStock.deserializeNBT((CompoundTag) tag.get(Constants.TAGSTOCK));
        interactableTradeSlots.deserializeNBT((CompoundTag) tag.get(Constants.TAGINTERACTABLE));
        savedTradeSlots.deserializeNBT((CompoundTag) tag.get(Constants.TAGPRICESALE));
    }

    @Override
    public boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop) {
        return crateStock.updateStackInPaymentSlot(payment, unlimitedShop);
    }
}
