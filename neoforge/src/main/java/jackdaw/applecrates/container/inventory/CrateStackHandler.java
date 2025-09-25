package jackdaw.applecrates.container.inventory;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends ItemStackHandler implements ICrateStock {

    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(Constants.TOTALCRATESLOTS);
    }

    public int getCountOfItemCached(Item item) {
        return this.itemCountCache.computeIfAbsent(item, $ -> getCountOfItemImmediately(item));
    }

    @Override
    public int getCountOfItemImmediately(Item item) {
        int count = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            var stack = this.getStackInSlot(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getStackInSlot(Constants.TOTALCRATESTOCKLOTS).isEmpty()) {
            prepPay.setCount(1);
            setStackInSlot(Constants.TOTALCRATESTOCKLOTS, prepPay);
        }

        //remove custom tag from money slot stack for comparison with 'virgin' item in the savedStack slot
        ItemStack paymentCompare = getStackInSlot(Constants.TOTALCRATESTOCKLOTS).copy();
        if (paymentCompare.get(Content.coinCounter) != null)
            paymentCompare.remove(Content.coinCounter);

        if (!ItemStack.isSameItemSameComponents(payment, paymentCompare))
            return false;

        ItemStack prepXchange = getStackInSlot(Constants.TOTALCRATESTOCKLOTS).copy();
        var counter = prepXchange.get(Content.coinCounter);
        if (counter != null)
            prepXchange.set(Content.coinCounter, new CoinCounter(counter.count() + payment.getCount()));
        else
            prepXchange.set(Content.coinCounter, new CoinCounter(payment.getCount()));

        setStackInSlot(Constants.TOTALCRATESTOCKLOTS, prepXchange);
        return true;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot == Constants.TOTALCRATESTOCKLOTS)
            return stack;
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == Constants.TOTALCRATESTOCKLOTS)
            return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot != Constants.TOTALCRATESTOCKLOTS && super.isItemValid(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        this.itemCountCache.clear();
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        /* removed moneypatch : users should run any version before 1.21.1 to apply
         *  the fix for an issue that was caused by updating the slots from 29 to 31 */
        //Content.moneyPatch.apply(nbt);
        super.deserializeNBT(provider, nbt);
        this.itemCountCache.clear();
    }
}
