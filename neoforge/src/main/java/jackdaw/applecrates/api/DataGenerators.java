package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.datagen.*;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Constants.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        generatedCrates(Constants.MODID, event);
    }

    public static void generatedCrates(String modid, GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(true, new CrateModels(modid, generator));
        generator.addProvider(true, new CrateLanguage(modid, generator, "en_uk"));
        generator.addProvider(true, new CrateLanguage(modid, generator, "en_us"));
        generator.addProvider(true, new CrateLanguage(modid, generator, "fr_fr"));
        generator.addProvider(true, new CrateLanguage(modid, generator, "de_de"));
        generator.addProvider(true, new CrateLanguage(modid, generator, "en_ca"));
        generator.addProvider(true, new CrateLanguage(modid, generator, "fr_ca"));
        generator.addProvider(true, new CrateTag(generator.getPackOutput(), event.getLookupProvider(), modid));
        generator.addProvider(true, new CrateRecipes(modid, generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(true, new CrateLoot(modid, generator, event.getLookupProvider()));
    }
}
