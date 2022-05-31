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
            generator.addProvider(new CrateRecipes(generator));
        }
        if (event.includeClient()) {
            generator.addProvider(new CrateModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new CrateStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new CrateItems(generator, event.getExistingFileHelper()));
            generator.addProvider(new CrateLanguage(generator, "en_uk"));
            generator.addProvider(new CrateLanguage(generator, "en_us"));
            generator.addProvider(new CrateLanguage(generator, "fr_fr"));
            generator.addProvider(new CrateLanguage(generator, "de_de"));
            generator.addProvider(new CrateLanguage(generator, "en_ca"));
            generator.addProvider(new CrateLanguage(generator, "fr_ca"));


        }
    }
}
