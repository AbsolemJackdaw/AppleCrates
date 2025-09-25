package jackdaw.applecrates.registry;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.besr.CrateBlockEntitySpecialRenderer;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.network.SAddOwner;
import jackdaw.applecrates.network.SCrateTradeSync;
import jackdaw.applecrates.network.SGetSale;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {

        CrateWoodType.values().forEach(crateWoodType -> {
            event.registerBlockEntityRenderer(CrateWoodType.getBlockEntityType(crateWoodType), CrateBlockEntitySpecialRenderer::new);
        });

        Content.buyerGuiButton = () -> {
            PacketDistributor.sendToServer(new SGetSale());
        };
        Content.ownerGuiButton = () -> {
            PacketDistributor.sendToServer(new SCrateTradeSync());
        };
        Content.addOwnerButton = username -> {
            PacketDistributor.sendToServer(new SAddOwner(username));
        };
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GeneralRegistry.CRATE_MENU_OWNER.get(), CrateScreenOwner::new);
        event.register(GeneralRegistry.CRATE_MENU_BUYER.get(), CrateScreenBuyer::new);
    }
}
