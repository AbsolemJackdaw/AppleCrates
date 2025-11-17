package jackdaw.applecrates.container.inventory;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends ItemStacksResourceHandler implements ICrateStock {

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
        for (int i = 0; i < this.size(); i++) {
            var stack = this.getResource(i);
            if (stack.is(item)) {
                count += this.getAmountAsInt(i);
            }
        }
        return count;
    }

    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (this.getAmountAsInt(Constants.TOTALCRATESTOCKLOTS) == 0) {
            prepPay.setCount(1);
            this.set(Constants.TOTALCRATESTOCKLOTS, ItemResource.of(prepPay.getItem()), prepPay.getCount());
        }

        //remove custom tag from money slot stack for comparison with 'virgin' item in the savedStack slot
        var paymentCompare = this.getResource(Constants.TOTALCRATESTOCKLOTS);
        if (paymentCompare.get(Content.coinCounter) != null)
            paymentCompare.without(Content.coinCounter);

        if (!paymentCompare.matches(payment))
            return false;
        //make a copy of the stack to manipulate the count without already modifying the actual payout slot
        var prepXchange = getResource(Constants.TOTALCRATESTOCKLOTS).toStack().copy();
        var counter = prepXchange.get(Content.coinCounter);
        if (counter != null)
            prepXchange.set(Content.coinCounter, new CoinCounter(counter.count() + payment.getCount()));
        else
            prepXchange.set(Content.coinCounter, new CoinCounter(payment.getCount()));

        //set payoutslot with the prepared coincounter. item will always be 1 in the slot. prevent cheats or slot manipulation
        this.set(Constants.TOTALCRATESTOCKLOTS, this.getResourceFrom(prepXchange), 1);
        return true;
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index == Constants.TOTALCRATESTOCKLOTS)
            return 0;
        return super.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index == Constants.TOTALCRATESTOCKLOTS)
            return 0;
        return super.extract(index, resource, amount, transaction);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return index != Constants.TOTALCRATESTOCKLOTS && super.isValid(index, resource);
    }

    @Override
    protected void onContentsChanged(int index, ItemStack previousContents) {
        super.onContentsChanged(index, previousContents);
        this.itemCountCache.clear();
    }

    @Override
    public void deserialize(ValueInput input) {
        super.deserialize(input);
        this.itemCountCache.clear();
    }
}
