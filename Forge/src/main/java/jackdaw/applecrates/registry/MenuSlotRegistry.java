package jackdaw.applecrates.registry;

import jackdaw.applecrates.Content;
import jackdaw.applecrates.container.ForgeSlotPriceSale;
import jackdaw.applecrates.container.SlotCrateStock;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class MenuSlotRegistry {

    public static void init() {
        Content.menuSlots = menu -> {
            if (menu.interactableSlots instanceof ItemStackHandler container) {
                menu.addGenericSlot(new SlotItemHandler(container, 0, 136, 37));
                menu.addGenericSlot(new SlotItemHandler(container, 1, 220, 38) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return menu.isOwner;
                    }
                });
            }
            if (menu.priceAndSaleSlots instanceof ItemStackHandler container) {
                menu.addGenericSlot(new ForgeSlotPriceSale(container, 0, 10, 140));
                menu.addGenericSlot(new ForgeSlotPriceSale(container, 1, 74, 140));
            }

            if (menu.crateStock instanceof ItemStackHandler container)
                for (int y = 0; y < 6; y++) //crate stock
                    for (int x = 0; x < 5; x++)
                        menu.addGenericSlot(new SlotCrateStock(container, y * 5 + x, x * 18 + 5, y * 18 + 18, menu.isOwner));

        };
    }
}
