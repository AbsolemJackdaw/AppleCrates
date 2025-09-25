package jackdaw.applecrates.datagen;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CrateLanguage extends LanguageProvider {
    private final String modid;

    public CrateLanguage(String modid, DataGenerator generator, String locale) {
        super(generator.getPackOutput(), modid, locale);
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

        if (Constants.GEN_VANILLA_CRATES) {
            add("container.crate", "Crate");
            add("container.crate.owner", "My Crate Shop");
            add("cannot.switch.trade", "To switch payment, the payout slot has to be empty");
            add("crate.set.creative", "Crate set to creative shop");
            add("crate.add.owner", "Add owner");
            add("crate.add.owner.confirm", "Confirm add owner");
            add("crate.add.owner.success", "Added %s as an owner!");
            add("crate.add.owner.not_found", "Player %s not found, cannot add as owner");
            add("crate.add.owner.already_owner", "%s is already an owner!");
            add("crate.button.sale.owner", "Set Trade");
            add("crate.button.sale.buyer", "Trade");
            add("tab.crate", "Crates");
        }
    }
}
