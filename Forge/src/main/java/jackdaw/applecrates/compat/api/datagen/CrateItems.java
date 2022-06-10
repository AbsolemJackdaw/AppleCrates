package jackdaw.applecrates.compat.api.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateItems extends ItemModelProvider {
    public CrateItems(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralRegistry.ITEM_MAP.forEach((woodType, item) -> {
            withExistingParent(item.get().getRegistryName().getPath(), new ResourceLocation(AppleCrates.MODID, "block/" + woodType.fullName() + "_crate"));
        });
    }
}
