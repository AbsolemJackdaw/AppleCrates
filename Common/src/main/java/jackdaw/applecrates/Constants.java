package jackdaw.applecrates;

import jackdaw.applecrates.container.CrateStockHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

public class Constants {
    public static final String MODID = "applecrates";
    public static final String[] VANILLAWOODS = {"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};
    public static final String TAGSTOCK = "stocked";

    public static Pair<Integer, Integer> payementSlotPosition = new Pair<>(137, 36);
    public static Pair<Integer, Integer> saleSlotPosition = new Pair<>(220, 38);

    public static class CommonCode {
        public static boolean updateStackInPaymentSlot(CrateStockHandler stock, ItemStack payment, boolean isUnlimitedShop) {
            if (isUnlimitedShop)
                return true;

            ItemStack prepPay = payment.copy();

            if (stock.getItemInSlot(29).isEmpty()) {
                prepPay.setCount(1);
                stock.setItemInSlot(29, prepPay);
            }
            if (!ItemStack.isSame(payment, stock.getItemInSlot(29)))
                return false;
            ItemStack prepXchange = stock.getItemInSlot(29).copy();
            CompoundTag tag = prepXchange.getOrCreateTag();
            if (tag.contains(Constants.TAGSTOCK)) {
                tag.putInt(Constants.TAGSTOCK, tag.getInt(Constants.TAGSTOCK) + payment.getCount());
            } else {
                tag.putInt(Constants.TAGSTOCK, payment.getCount());
            }
            stock.setItemInSlot(29, prepXchange);
            return true;
        }

        public static int getCountOfItem(CrateStockHandler stock, Item item) {
            return stock.itemCountCache.computeIfAbsent(item, $ -> {
                int count = 0;
                for (int i = 0; i < stock.numberOfSlots(); i++) {
                    var stack = stock.getItemInSlot(i);
                    if (stack.is(item)) {
                        count += stack.getCount();
                    }
                }
                return count;
            });
        }
    }

}
