package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.datagen.*;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        generatedCrates(Constants.MODID, event);
    }

    public static void generatedCrates(String modid, GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        //datapack  server
        generator.addProvider(event.includeServer(), new CrateTag(generator.getPackOutput(), event.getLookupProvider(), modid, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CrateRecipes(modid, generator, event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new CrateLoot(modid, generator, event.getLookupProvider()));

        //resourcepack  client
        generator.addProvider(event.includeClient(), new CrateModels(modid, generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateStates(modid, generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateItems(modid, generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "en_uk"));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "en_us"));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "fr_fr"));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "de_de"));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "en_ca"));
        generator.addProvider(event.includeClient(), new CrateLanguage(modid, generator, "fr_ca"));
    }
}
