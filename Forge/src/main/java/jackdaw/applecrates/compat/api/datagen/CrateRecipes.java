package jackdaw.applecrates.compat.api.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.compat.api.exception.WoodException;
import jackdaw.applecrates.registry.GeneralRegistry;
import jackdaw.applecrates.util.CrateWoodType;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class CrateRecipes extends RecipeProvider {

    public CrateRecipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {

        CrateWoodType.values().forEach(woodType -> {
            Block findBlock = Registry.BLOCK.get(new ResourceLocation(woodType.modId(), woodType.name() + "_planks"));
            try {
                if (findBlock.equals(Blocks.AIR))
                    throw WoodException.INSTANCE.noSuchBlockError(woodType);
                ShapedRecipeBuilder.shaped(GeneralRegistry.BLOCK_MAP.get(woodType).get())
                        .define('p', findBlock)
                        .define('S', Items.STICK)
                        .pattern("S S").pattern("ppp").group("crates").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(pFinishedRecipeConsumer);

            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });

    }
}
