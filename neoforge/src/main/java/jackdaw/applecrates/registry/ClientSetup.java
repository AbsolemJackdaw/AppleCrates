package jackdaw.applecrates.registry;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.besr.CrateBlockRenderer;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@EventBusSubscriber(modid = Constants.MODID, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {

        CrateWoodType.values().forEach(crateWoodType -> {
            event.registerBlockEntityRenderer(CrateWoodType.getBlockEntityType(crateWoodType), CrateBlockRenderer::new);
        });

        Content.buyerGuiButton = () -> {
            ClientPacketDistributor.sendToServer(new ServerGetSale());
        };
        Content.ownerGuiButton = () -> {
            ClientPacketDistributor.sendToServer(new ServerCrateSync());
        };
        Content.addOwnerButton = username -> {
            ClientPacketDistributor.sendToServer(new ServerAddOwner(username));
        };
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(GeneralRegistry.CRATE_MENU_OWNER.get(), CrateScreenOwner::new);
        event.register(GeneralRegistry.CRATE_MENU_BUYER.get(), CrateScreenBuyer::new);
    }
}
