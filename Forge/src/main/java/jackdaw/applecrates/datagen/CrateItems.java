package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateItems extends ItemModelProvider {
    public CrateItems(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).forEach(crateWoodType -> {
            withExistingParent(crateWoodType.getBlockRegistryName(), new ResourceLocation(modid, "block/" + crateWoodType.getBlockRegistryName()));
        });
    }
}
