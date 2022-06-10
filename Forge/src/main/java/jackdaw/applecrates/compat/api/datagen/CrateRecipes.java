package jackdaw.applecrates.compat.api.datagen;

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

import java.util.function.Consumer;

public class CrateRecipes extends RecipeProvider {

    public CrateRecipes(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {

        CrateWoodType.values().forEach(woodType -> {
            ShapedRecipeBuilder.shaped(GeneralRegistry.BLOCK_MAP.get(woodType).get())
                    .define('p', Registry.BLOCK.get(new ResourceLocation(woodType.modId(), woodType.name() + "_planks")))
                    .define('S', Items.STICK)
                    .pattern("S S").pattern("ppp").group("boat").unlockedBy("has_planks", has(ItemTags.PLANKS)).save(pFinishedRecipeConsumer);

        });

    }
}
