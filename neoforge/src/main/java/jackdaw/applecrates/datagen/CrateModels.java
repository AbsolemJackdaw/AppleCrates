package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;
import java.util.Set;

public class CrateModels extends ModelProvider {
    public CrateModels(String modId, DataGenerator generator) {
        super(generator.getPackOutput(), modId);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockgen, ItemModelGenerators itemModel) {

        CrateWoodType.values().forEach(crateWoodType -> {
            try {
                Identifier existingTexture = AppleCrateAPI.getTexturePathFromWood().get(crateWoodType);
                if (existingTexture == null)
                    throw WoodException.INSTANCE.resLocNotFound(crateWoodType);
                var modelBase = Identifier.fromNamespaceAndPath(Constants.MODID, "applecrate");
                var modelConnected = Identifier.fromNamespaceAndPath(Constants.MODID, "applecrate_connected");
                var textureSlots = Set.of(TextureSlot.PARTICLE, TextureSlot.create("0", TextureSlot.TEXTURE));
                var templateBase = new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(modelBase.toString())), Optional.of(""), textureSlots.toArray(textureSlots.toArray(new TextureSlot[0]))).extend().build();
                var templateConnected = new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(modelConnected.toString())), Optional.of(""), textureSlots.toArray(textureSlots.toArray(new TextureSlot[0]))).extend().build();

                var textureMap = new TextureMapping();
                textureSlots.forEach(textureSlot -> textureMap.put(textureSlot, existingTexture));
                var block = CrateWoodType.getBlock(crateWoodType);
                var providerBase = TexturedModel.createDefault(b -> textureMap, templateBase);
                var providerConnected = TexturedModel.createDefault(b -> textureMap, templateConnected);

                MultiVariant mvBase = BlockModelGenerators.plainVariant(providerBase.create(block, blockgen.modelOutput));
                MultiVariant mvConnected = BlockModelGenerators.plainVariant(providerConnected.createWithSuffix(block,"_connected", blockgen.modelOutput));

                blockgen.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.ATTACHED)
                        .select(Direction.NORTH, false, mvBase)
                        .select(Direction.SOUTH, false, mvBase.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.EAST, false, mvBase.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.WEST, false, mvBase.with(BlockModelGenerators.Y_ROT_270))
                        .select(Direction.NORTH, true, mvConnected)
                        .select(Direction.SOUTH, true, mvConnected.with(BlockModelGenerators.Y_ROT_180))
                        .select(Direction.EAST, true, mvConnected.with(BlockModelGenerators.Y_ROT_90))
                        .select(Direction.WEST, true, mvConnected.with(BlockModelGenerators.Y_ROT_270))
                ));
            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}
