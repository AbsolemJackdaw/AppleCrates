package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class StackHandlerAdapter implements IStackHandlerAdapter {
    public CrateStackHandler crateStock = new CrateStackHandler();
    public SimpleContainerNBT interactable = new SimpleContainerNBT(2);
    public SimpleContainerNBT priceAndSale = new SimpleContainerNBT(2);

    @Override
    public int getContainerSize() {
        return crateStock.getContainerSize();
    }

    @Override
    public ItemStack getInteractableItem(int slot) {
        return interactable.getItem(slot);
    }

    @Override
    public ItemStack getPriceAndSaleItem(int slot) {
        return priceAndSale.getItem(slot);
    }

    @Override
    public ItemStack getCratestockItem(int slot) {
        return crateStock.getItem(slot);
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
