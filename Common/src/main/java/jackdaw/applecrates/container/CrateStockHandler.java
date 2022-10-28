package jackdaw.applecrates.container;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface CrateStockHandler extends GenericInventory {

    Map<Item, Integer> itemCountCache = new HashMap<>();

    boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop);

    int getCountOfItem(Item item);

}
