package jackdaw.applecrates.registry;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.CrateChannel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        CrateChannel.init();
    }
}
