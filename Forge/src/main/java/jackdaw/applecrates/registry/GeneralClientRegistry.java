package jackdaw.applecrates.registry;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.besr.CrateBESR;
import jackdaw.applecrates.client.screen.CrateScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AppleCrates.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeneralClientRegistry {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {

        CrateWoodType.values().forEach(crateWoodType -> {
            event.registerBlockEntityRenderer(CrateWoodType.getBlockEntityType(crateWoodType), CrateBESR::new);
        });
        MenuScreens.register(GeneralRegistry.CRATE_MENU.get(), CrateScreen::new);
    }
}
