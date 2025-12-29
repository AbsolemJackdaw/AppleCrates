package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CrateModels extends ModelProvider {
    public CrateModels(String modId, DataGenerator generator) {
        super(generator.getPackOutput(), modId);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModel, ItemModelGenerators itemModel) {

        CrateWoodType.values().forEach(crateWoodType -> {
            try {
                ResourceLocation existingTexture = AppleCrateAPI.getTexturePathFromWood().get(crateWoodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(crateWoodType);
                var baseModel = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "applecrate");
                var textureSlots = Set.of(TextureSlot.PARTICLE, TextureSlot.create("0", TextureSlot.TEXTURE));
                var template = new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(baseModel.toString())), Optional.of(""), textureSlots.toArray(textureSlots.toArray(new TextureSlot[0]))).extend().build();
                var textureMap = new TextureMapping();
                textureSlots.forEach(textureSlot -> textureMap.put(textureSlot, existingTexture));
                var block = CrateWoodType.getBlock(crateWoodType);
                var provider = TexturedModel.createDefault(b -> textureMap, template);
                blockModel.createHorizontallyRotatedBlock(block, provider);
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
