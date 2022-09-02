package jackdaw.applecrates.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.api.datagen.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AppleCrates.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Gen {

    @SubscribeEvent
    public static void gen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new CrateTag(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new CrateRecipes(generator));
        generator.addProvider(event.includeServer(), new CrateLoot(generator));
        generator.addProvider(event.includeClient(), new CrateModels(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateStates(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateItems(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "en_uk"));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "en_us"));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "fr_fr"));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "de_de"));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "en_ca"));
        generator.addProvider(event.includeClient(), new CrateLanguage(generator, "fr_ca"));
    }
}
