package jackdaw.applecrates.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrateLanguage extends LanguageProvider {
    private final String modid;

    public CrateLanguage(String modid, DataGenerator generator, String locale) {
        super(generator, modid, locale);
        this.modid = modid;
    }

    @Override
    protected void addTranslations() {

        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).forEach(crateWoodType -> {
            String capitalized = Stream.of(((crateWoodType.name() + "_crate")).replace("_", " ").trim().split("\\s"))
                    .filter(word -> word.length() > 0)
                    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                    .collect(Collectors.joining(" "));
            add(CrateWoodType.getBlock(crateWoodType), capitalized);
        });

        if (AppleCrates.GEN_VANILLA_CRATES) {
            add("container.crate", "Crate");
            add("container.crate.owner", "My Crate Shop");
            add("cannot.switch.trade", "To switch payment item, your green payout slot has to be empty");
            add("crate.set.creative", "Crate set to creative shop");
        }
    }
}
