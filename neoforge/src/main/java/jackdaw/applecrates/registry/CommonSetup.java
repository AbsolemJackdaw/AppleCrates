package jackdaw.applecrates.registry;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.api.GeneralRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Constants.MODID)
public class CommonSetup {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        Content.coinCounter = GeneralRegistry.COIN_COUNTER.get();
        Content.populateMenus();
    }
}
