package jackdaw.applecrates.datagen;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CrateRecipes extends RecipeProvider {

    private String modId;

    public CrateRecipes(String modId, DataGenerator pGenerator) {
        super(pGenerator);
        this.modId = modId;
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {

        CrateWoodType.values().forEach(woodType -> {
            Block plankBlock = Registry.BLOCK.get(AppleCrateAPI.getPlanksResourceLocation().get(woodType));
            try {
                if (plankBlock.equals(Blocks.AIR))
                    throw WoodException.INSTANCE.noSuchBlockError(woodType);

                Map<Character, Ingredient> define = new HashMap<>();
                define.put('p', Ingredient.of(plankBlock));
                define.put('s', Ingredient.of(Items.STICK));
                pFinishedRecipeConsumer.accept(shaped(
                        woodType.getFullRegistryResLoc(),
                        Item.byBlock(CrateWoodType.getBlock(woodType)),
                        1,
                        Arrays.stream(new String[]{"s s", "ppp"}).toList(),
                        define));

//                        .group("crates")
//                        .unlockedBy("has_planks", has(ItemTags.PLANKS))

            } catch (WoodException e) {
                LogUtils.getLogger().error(e.getMessage());
            }
        });
    }

    public static FinishedRecipe shaped(ResourceLocation recipeId, Item result, int count, List<String> pattern, Map<Character, Ingredient> key) {
        return new ShapedRecipeBuilder.Result(
                recipeId,
                result,
                count,
                "", // recipe book group (not used)
                pattern,
                key,
                null, // advancement
                null) {
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }
        };
    }
}
