package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrateModels extends BlockModelProvider {
    public static final String MINDIR = "$minecraft:$";
    protected static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public CrateModels(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        CrateWoodType.values().forEach(crateWoodType -> {
            try {
                ResourceLocation existingTexture = AppleCrateAPI.getTexturePathFromWood().get(crateWoodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(crateWoodType);
                existingFileHelper.trackGenerated(existingTexture, TEXTURE);
                var appleCrateModel = new ResourceLocation(AppleCrates.MODID, "block/applecrate");
                withExistingParent(crateWoodType.getBlockRegistryName(), appleCrateModel).texture("particle", existingTexture).texture("0", existingTexture);
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
