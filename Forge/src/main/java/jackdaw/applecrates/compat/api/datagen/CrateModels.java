package jackdaw.applecrates.compat.api.datagen;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.compat.api.AppleCrateAPI;
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
            String path = AppleCrateAPI.getPathFromWood().getOrDefault(woodType.name(), "");

            ResourceLocation existingTexture = new ResourceLocation(woodType.modId(), String.format("block/%s%s_planks", path, woodType.name()));
            existingFileHelper.trackGenerated(existingTexture, TEXTURE); //trick datagen into thinking that the file is definitly present

            withExistingParent(block.get().getRegistryName().getPath(),
                    modLoc("block/applecrate")).texture("particle", existingTexture).texture("0", existingTexture);
        });
    }
}
