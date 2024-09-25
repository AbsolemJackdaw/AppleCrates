package jackdaw.applecrates.registry;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.besr.CrateBESR;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.network.CrateChannel;
import jackdaw.applecrates.network.SCrateTradeSync;
import jackdaw.applecrates.network.SGetSale;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeneralClientRegistry {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {

        CrateWoodType.values().forEach(crateWoodType -> {
            event.registerBlockEntityRenderer(CrateWoodType.getBlockEntityType(crateWoodType), CrateBESR::new);
        });
        MenuScreens.register(GeneralRegistry.CRATE_MENU_OWNER.get(), CrateScreenOwner::new);
        MenuScreens.register(GeneralRegistry.CRATE_MENU_BUYER.get(), CrateScreenBuyer::new);

        Content.buyerGuiButton = () -> {
            CrateChannel.NETWORK.sendToServer(new SGetSale());
        };
        Content.ownerGuiButton = () -> {
            CrateChannel.NETWORK.sendToServer(new SCrateTradeSync());
        };
    }
}
