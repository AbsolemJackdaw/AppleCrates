package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateTag extends BlockTagsProvider {
    private final String modid;

    public CrateTag(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
        this.modid = modid;
    }

    @Override
    protected void addTags() {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).forEach(crateWoodType -> {
            tag(BlockTags.MINEABLE_WITH_AXE).addOptional(crateWoodType.getFullRegistryResLoc());
            tag(BlockTags.NON_FLAMMABLE_WOOD).addOptional(crateWoodType.getFullRegistryResLoc());
        });
    }
}
