package jackdaw.applecrates.container.inventory;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends GenericStackHandler implements ICrateStock {

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
        for (int i = 0; i < this.getContainerSize(); i++) {
            var stack = this.getItem(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        return slot != Constants.TOTALCRATESTOCKLOTS && super.canPlaceItem(slot, stack);
    }

    @Override
    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) {
            prepPay.setCount(1);
            setItem(Constants.TOTALCRATESTOCKLOTS, prepPay);
        }
        //remove custom tag from money slot stack for comparison with 'virgin' item in the savedStack slot
        ItemStack paymentCompare = getItem(Constants.TOTALCRATESTOCKLOTS).copy();
        if (paymentCompare.has(Content.coinCounter))
            paymentCompare.remove(Content.coinCounter);

        if (!ItemStack.isSameItemSameComponents(payment, paymentCompare))
            return false;

        ItemStack prepXchange = getItem(Constants.TOTALCRATESTOCKLOTS).copy();
        if (prepXchange.has(Content.coinCounter)) {
            prepXchange.set(Content.coinCounter, new CoinCounter(prepXchange.get(Content.coinCounter).count() + payment.getCount()));
        } else {
            prepXchange.set(Content.coinCounter, new CoinCounter(payment.getCount()));
        }
        setItem(Constants.TOTALCRATESTOCKLOTS, prepXchange);
        return true;
    }
}
