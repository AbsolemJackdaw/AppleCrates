package jackdaw.applecrates;

import jackdaw.applecrates.client.CrateScreen;
import jackdaw.applecrates.client.besr.CrateBESR;
import jackdaw.applecrates.network.ClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class FabricCratesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricCrates.besrreg.forEach(blockEntityTypeSupplier ->
                BlockEntityRendererRegistry.register(blockEntityTypeSupplier.get(), CrateBESR::new)
        );
        ScreenRegistry.register(FabricCrates.CRATETYPE, CrateScreen::new);
        ClientNetwork.registerClientPackets();
    }
}
