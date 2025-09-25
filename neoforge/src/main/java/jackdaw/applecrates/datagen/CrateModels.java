package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CrateModels extends BlockModelProvider {
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public CrateModels(String modId, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), modId, existingFileHelper);
    }


    @Override
    protected void registerModels() {

        CrateWoodType.values().forEach(crateWoodType -> {
            try {
                ResourceLocation existingTexture = AppleCrateAPI.getTexturePathFromWood().get(crateWoodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(crateWoodType);
                existingFileHelper.trackGenerated(existingTexture, TEXTURE);
                var appleCrateModel = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "block/applecrate");
                existingFileHelper.trackGenerated(appleCrateModel, MODEL);
                withExistingParent(crateWoodType.getBlockRegistryName(), appleCrateModel).texture("particle", existingTexture).texture("0", existingTexture);
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
