package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class StackHandlerAdapter implements IStackHandlerAdapter {
    public CrateStackHandler crateStock = new CrateStackHandler();
    public ItemStackHandler interactableTradeSlots = new net.minecraftforge.items.ItemStackHandler(2);
    public ItemStackHandler savedTradeSlots = new net.minecraftforge.items.ItemStackHandler(2);

    @Override
    public ItemStack getInteractableItem(int slot) {
        return interactableTradeSlots.getStackInSlot(slot);
    }

    @Override
    public ItemStack getPriceAndSaleItem(int slot) {
        return savedTradeSlots.getStackInSlot(slot);
    }

    @Override
    public ItemStack getCratestockItem(int slot) {
        return crateStock.getStackInSlot(slot);
    }

    @Override
    public int getCratestacksTotalItemCount(Item item) {
        return crateStock.getCountOfItem(item);
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
