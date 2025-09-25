package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class CrateRecipes extends RecipeProvider {

    private String modId;

    public CrateRecipes(String modId, DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> registries) {
        super(pGenerator.getPackOutput(), registries);
        this.modId = modId;
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        CrateWoodType.values().forEach(woodType -> {
            Block plankBlock = BuiltInRegistries.BLOCK.get(AppleCrateAPI.getPlanksResourceLocation().get(woodType));
            try {
                if (plankBlock.equals(Blocks.AIR))
                    throw WoodException.INSTANCE.noSuchBlockError(woodType);
                var crate = (Item) Item.BY_BLOCK.getOrDefault(CrateWoodType.getBlock(woodType), Items.AIR);
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, crate)
                        .pattern("s s")
                        .pattern("ppp")
                        .define('s', Ingredient.of(Items.STICK))
                        .define('p', Ingredient.of(plankBlock))
                        .unlockedBy("has_planks", has(ItemTags.PLANKS))
                        .group("crates")
                        .save(recipeOutput);

            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }
}