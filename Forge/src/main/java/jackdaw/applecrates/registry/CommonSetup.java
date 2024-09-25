package jackdaw.applecrates.registry;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.container.slot.SlotCrateStock;
import jackdaw.applecrates.container.slot.SlotPriceSale;
import jackdaw.applecrates.network.CrateChannel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        CrateChannel.init();

        Content.menuSlots = menu -> {
            if (!(menu.adapter instanceof StackHandlerAdapter stackHandler))
                return;
            menu.addSlot(new SlotItemHandler(stackHandler.interactableTradeSlots, 0, menu.isOwner() ? 10 : 102, menu.isOwner() ? 76 : 21)); //owner set pay
            menu.addSlot(new SlotItemHandler(stackHandler.interactableTradeSlots, 1, menu.isOwner() ? 46 : 142, menu.isOwner() ? 76 : 21) { //owner set item
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return menu.isOwner();
                }
            });

            //invisible slots, storage for current trade
            menu.addSlot(new SlotPriceSale(stackHandler.savedTradeSlots, 0)); //buyer pays
            menu.addSlot(new SlotPriceSale(stackHandler.savedTradeSlots, 1)); //buyer gets

            for (int y = 0; y < 3; y++) //crate stock
                for (int x = 0; x < 10; x++)
                    menu.addSlot(new SlotCrateStock(stackHandler.crateStock, y * 10 + x, x * 18 + 10, y * 18 + 17, menu.isOwner()));
            menu.addSlot(new SlotCrateStock(stackHandler.crateStock, Constants.TOTALCRATESTOCKLOTS, 172, 76, menu.isOwner()));
        };
    }
}
