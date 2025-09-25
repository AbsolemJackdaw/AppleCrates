package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CrateItems extends ItemModelProvider {
    public CrateItems(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).forEach(crateWoodType -> {
            withExistingParent(crateWoodType.getBlockRegistryName(), ResourceLocation.fromNamespaceAndPath(modid, "block/" + crateWoodType.getBlockRegistryName()));
        });
    }
}
