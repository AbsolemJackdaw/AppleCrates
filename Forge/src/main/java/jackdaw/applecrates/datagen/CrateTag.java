package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CrateTag extends BlockTagsProvider {
    private final String modid;

    public CrateTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId,  ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
        this.modid = modId;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).forEach(crateWoodType -> {
            tag(BlockTags.MINEABLE_WITH_AXE).addOptional(crateWoodType.getFullRegistryResLoc());
            //tag(BlockTags.).addOptional(crateWoodType.getFullRegistryResLoc());
        });
    }
}