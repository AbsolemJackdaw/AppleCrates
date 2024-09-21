package jackdaw.applecrates;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.client.screen.CrateScreenBuyer;
import jackdaw.applecrates.client.screen.CrateScreenOwner;
import jackdaw.applecrates.client.besr.CrateBESR;
import jackdaw.applecrates.network.ClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

@Environment(EnvType.CLIENT)
public class FabricCratesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Content.clientConfig = () -> EnumCrateItemRendering.THREE;
        CrateWoodType.values().forEach(crateWoodType -> {
            BlockEntityRendererRegistry.register(CrateWoodType.getBlockEntityType(crateWoodType), CrateBESR::new);
        });
        MenuScreens.register(FabricCrates.CRATE_MENU_OWNER, CrateScreenOwner::new);
        MenuScreens.register(FabricCrates.CRATE_MENU_BUYER, CrateScreenBuyer::new);

        ClientNetwork.registerClientPackets();
    }
}
