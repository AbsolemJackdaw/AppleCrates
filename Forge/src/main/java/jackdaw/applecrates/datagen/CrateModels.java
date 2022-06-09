package jackdaw.applecrates.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateModels extends BlockModelProvider {
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public CrateModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, AppleCrates.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> {
            String mod = GeneralRegistry.modidFromWood.getOrDefault(woodType.name(), "minecraft");
            String path = GeneralRegistry.pathFromWood.getOrDefault(woodType.name(), "");

            ResourceLocation plank = new ResourceLocation(mod, String.format("block/%s%s_planks", path, woodType.name()));
            existingFileHelper.trackGenerated(plank, TEXTURE); //trick datagen into thinking that the file is definitly present

            withExistingParent(block.get().getRegistryName().getPath(),
                    modLoc("block/applecrate")).texture("particle", plank).texture("0", plank);
        });
    }
}
