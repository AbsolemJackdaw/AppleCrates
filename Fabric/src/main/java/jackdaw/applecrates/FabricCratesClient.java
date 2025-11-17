package jackdaw.applecrates;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class FabricCratesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Content.clientConfig = () -> EnumCrateItemRendering.THREE;
        CrateWoodType.values().forEach(crateWoodType -> {
            BlockEntityRendererRegistry.register(CrateWoodType.getBlockEntityType(crateWoodType), CrateBlockEntitySpecialRenderer::new);
        });
        MenuScreens.register(GeneralRegistry.CRATE_MENU_OWNER, CrateScreenOwner::new);
        MenuScreens.register(GeneralRegistry.CRATE_MENU_BUYER, CrateScreenBuyer::new);

        Content.buyerGuiButton = () -> ClientPlayNetworking.send(new ServerGetSale());
        Content.ownerGuiButton = () -> ClientPlayNetworking.send(new ServerCrateSync());
        Content.addOwnerButton = username -> ClientPlayNetworking.send(new ServerAddOwner(username));
    }
}
