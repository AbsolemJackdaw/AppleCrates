package jackdaw.applecrates.datagen;

import jackdaw.applecrates.AppleCrates;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = AppleCrates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(new CrateTag(generator, event.getExistingFileHelper()));

        }
        if (event.includeClient()) {
            generator.addProvider(new CrateModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new CrateStates(generator, event.getExistingFileHelper()));

        }
    }
}
