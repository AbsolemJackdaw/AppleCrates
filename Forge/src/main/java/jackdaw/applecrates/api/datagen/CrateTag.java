package jackdaw.applecrates.api.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CrateTag extends BlockTagsProvider {
    public CrateTag(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> tag(BlockTags.MINEABLE_WITH_AXE).addOptional(Registry.BLOCK.getKey(block.get())));
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> tag(BlockTags.NON_FLAMMABLE_WOOD).addOptional(Registry.BLOCK.getKey(block.get())));

    }
}
