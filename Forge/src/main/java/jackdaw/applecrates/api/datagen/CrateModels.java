package jackdaw.applecrates.api.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.exception.WoodException;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.Registry;
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
        GeneralRegistry.BLOCK_MAP.forEach((woodType, block) -> {
            try {
                ResourceLocation existingTexture = AppleCrateAPI.getPathFromWood().get(woodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(woodType);
                existingFileHelper.trackGenerated(existingTexture, TEXTURE); //trick datagen into thinking that the file is definitely present
                withExistingParent(
                        Registry.BLOCK.getKey(block.get()).getPath(),
                        new ResourceLocation(AppleCrates.MODID, "block/applecrate")).texture("particle", existingTexture).texture("0", existingTexture);
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
