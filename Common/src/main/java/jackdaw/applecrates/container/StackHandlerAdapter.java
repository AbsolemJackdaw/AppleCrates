package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import jackdaw.applecrates.container.inventory.GenericStackHandler;
import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
        return crateStock.getCountOfItemCached(item);
    }

    @Override
    public void loadInventoryData(ValueInput input) {
        loadAllItems(input, crateStock.getItems(), Constants.TAGSTOCK);
        loadAllItems(input, interactableTrades.getItems(), Constants.TAGINTERACTABLE);
        loadAllItems(input, savedTrades.getItems(), Constants.TAGPRICESALE);
    }

    @Override
    public void saveInventoryData(ValueOutput output) {
        saveAllItems(output, crateStock.getItems(), Constants.TAGSTOCK);
        saveAllItems(output, interactableTrades.getItems(), Constants.TAGINTERACTABLE);
        saveAllItems(output, savedTrades.getItems(), Constants.TAGPRICESALE);
    }

    @Override
    public boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop) {
        return crateStock.updateStackInPaymentSlot(payment, unlimitedShop);
    }

    private void saveAllItems(ValueOutput output, NonNullList<ItemStack> items, String tag) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> typedOutputList = output.list(tag, ItemStackWithSlot.CODEC);

        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = (ItemStack) items.get(i);
            if (!itemStack.isEmpty()) {
                typedOutputList.add(new ItemStackWithSlot(i, itemStack));
            }
        }
    }

    private void loadAllItems(ValueInput input, NonNullList<ItemStack> items, String tag) {
        for (ItemStackWithSlot itemStackWithSlot : input.listOrEmpty(tag, ItemStackWithSlot.CODEC)) {
            if (itemStackWithSlot.isValidInContainer(items.size())) {
                items.set(itemStackWithSlot.slot(), itemStackWithSlot.stack());
            }
        }

    }
}
