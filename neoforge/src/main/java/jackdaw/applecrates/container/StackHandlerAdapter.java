package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.inventory.CrateStackHandler;
import jackdaw.applecrates.container.inventory.GenericStackHandler;
import jackdaw.applecrates.container.inventory.ICrateStock;
import jackdaw.applecrates.container.inventory.IGenericInventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

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
        return getStackFromInSlot(interactableTradeSlots, slot);
    }

    @Override
    public void setInteractableTradeItem(int slot, ItemStack stack) {
        setStackFromInSlot(interactableTradeSlots, slot, stack);
    }

    @Override
    public ItemStack getSavedTradeSlotsItem(int slot) {
        return getStackFromInSlot(savedTradeSlots, slot);
    }

    @Override
    public void setSavedTradeSlotItem(int slot, ItemStack stack) {
        setStackFromInSlot(savedTradeSlots, slot, stack);
    }

    @Override
    public ItemStack getCrateStockItem(int slot) {
        return getStackFromInSlot(crateStock, slot);
    }

    @Override
    public void setCrateStockItem(int slot, ItemStack stack) {
        setStackFromInSlot(crateStock, slot, stack);
    }

    @Override
    public void loadInventoryData(ValueInput input) {
        input.child(Constants.TAGSTOCK).ifPresent(crateStock::deserialize);
        input.child(Constants.TAGINTERACTABLE).ifPresent(interactableTradeSlots::deserialize);
        input.child(Constants.TAGPRICESALE).ifPresent(savedTradeSlots::deserialize);
    }

    @Override
    public void saveInventoryData(ValueOutput output) {
        output.putChild(Constants.TAGSTOCK, crateStock);
        output.putChild(Constants.TAGINTERACTABLE, interactableTradeSlots);
        output.putChild(Constants.TAGPRICESALE, savedTradeSlots);
    }

    @Override
    public int getCratestacksTotalItemCount(Item item) {
        return crateStock.getCountOfItemCached(item);
    }

    @Override
    public boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop) {
        return crateStock.updateStackInPaymentSlot(payment, unlimitedShop);
    }

    private ItemStack getStackFromInSlot(StacksResourceHandler<ItemStack, ItemResource> handler, int slot) {
        var itemresource = handler.getResource(slot);
        var count = handler.getAmountAsInt(slot);
        return itemresource.toStack(count);
    }

    private void setStackFromInSlot(StacksResourceHandler<ItemStack, ItemResource> handler, int slot, ItemStack stack) {
        handler.set(slot, ItemResource.of(stack), stack.getCount());
    }
}
