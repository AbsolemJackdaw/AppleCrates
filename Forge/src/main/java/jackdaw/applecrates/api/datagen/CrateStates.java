package jackdaw.applecrates.api.datagen;

import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateStates extends BlockStateProvider {
    public CrateStates(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) ->
                horizontalBlock(
                        block.get(),
                        new ModelFile.ExistingModelFile(modLoc("block/" + woodType.fullName() + "_crate"), models().existingFileHelper)
                        , 180));
    }
}
