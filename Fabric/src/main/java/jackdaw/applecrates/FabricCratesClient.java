package jackdaw.applecrates;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.client.besr.CrateBlockEntitySpecialRenderer;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.network.PacketId;
import jackdaw.applecrates.network.ServerNetwork;
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
        MenuScreens.register(FabricCrates.CRATE_MENU_OWNER, CrateScreenOwner::new);
        MenuScreens.register(FabricCrates.CRATE_MENU_BUYER, CrateScreenBuyer::new);

        Content.buyerGuiButton = () -> ClientPlayNetworking.send(PacketId.CHANNEL, ServerNetwork.sPacketSale());
        Content.ownerGuiButton = () -> ClientPlayNetworking.send(PacketId.CHANNEL, ServerNetwork.sPacketTrade());
    }
}
