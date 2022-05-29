package jackdaw.applecrates.registry;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.client.besr.CrateBESR;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AppleCrates.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeneralClientRegistry {

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        GeneralRegistry.BE_MAP.forEach((woodType, blockEntity) -> event.registerBlockEntityRenderer(blockEntity.get(), CrateBESR::new));
        event.registerBlockEntityRenderer(GeneralRegistry.BE_MAP.get(WoodType.OAK).get(), CrateBESR::new);
    }

}
