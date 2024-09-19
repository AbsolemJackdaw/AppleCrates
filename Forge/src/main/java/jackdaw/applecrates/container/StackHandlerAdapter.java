package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class StackHandlerAdapter implements IStackHandlerAdapter {
    public CrateStackHandler crateStock = new CrateStackHandler();
    public ItemStackHandler interactable = new net.minecraftforge.items.ItemStackHandler(2);
    public ItemStackHandler priceAndSale = new net.minecraftforge.items.ItemStackHandler(2);

    @Override
    public ItemStack getInteractableItem(int slot) {
        return interactable.getStackInSlot(slot);
    }

    @Override
    public ItemStack getPriceAndSaleItem(int slot) {
        return priceAndSale.getStackInSlot(slot);
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
        tag.put(Constants.TAGINTERACTABLE, interactable.serializeNBT());
        tag.put(Constants.TAGPRICESALE, priceAndSale.serializeNBT());
    }

    @Override
    public void loadInventoryData(CompoundTag tag) {
        crateStock.deserializeNBT((CompoundTag) tag.get(Constants.TAGSTOCK));
        interactable.deserializeNBT((CompoundTag) tag.get(Constants.TAGINTERACTABLE));
        priceAndSale.deserializeNBT((CompoundTag) tag.get(Constants.TAGPRICESALE));
    }

    @Override
    public boolean updatePaymentSlot(ItemStack payment, boolean unlimitedShop) {
        return crateStock.updateStackInPaymentSlot(payment, unlimitedShop);
    }
}
