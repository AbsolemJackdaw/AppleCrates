package jackdaw.applecrates.api.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateStates extends BlockStateProvider {
    public CrateStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) ->
                horizontalBlock(
                        block.get(),
                        new ModelFile.ExistingModelFile(new ResourceLocation(AppleCrates.MODID, "block/" + woodType.fullName() + "_crate"), models().existingFileHelper)
                        , 180));
    }
}
