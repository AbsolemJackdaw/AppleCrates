package jackdaw.applecrates.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateModels extends BlockModelProvider {
    public CrateModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> {
            ResourceLocation plank = new ResourceLocation("minecraft:block/" + woodType.name() + "_planks");
            withExistingParent(block.get().getRegistryName().getPath(),
                    modLoc("block/applecrate")).texture("particle", plank).texture("0", plank);

        });
    }
}
