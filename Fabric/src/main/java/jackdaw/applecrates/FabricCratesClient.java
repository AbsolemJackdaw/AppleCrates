package jackdaw.applecrates;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.client.CrateScreen;
import jackdaw.applecrates.client.IClientConfig;
import jackdaw.applecrates.client.besr.CrateBESR;
import jackdaw.applecrates.network.ClientNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.mixin.client.rendering.BlockEntityRendererFactoriesMixin;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Environment(EnvType.CLIENT)
public class FabricCratesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Content.clientConfig = () -> EnumCrateItemRendering.THREE;
        CrateWoodType.values().forEach(crateWoodType -> {
            BlockEntityRendererRegistry.register(CrateWoodType.getBlockEntityType(crateWoodType), CrateBESR::new);
        });
        ScreenRegistry.register(FabricCrates.CRATETYPE, CrateScreen::new);
        ClientNetwork.registerClientPackets();
    }
}
