package jackdaw.applecrates.datagen;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.exception.WoodException;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class CrateRecipes extends RecipeProvider.Runner {

    private String modId;

    public CrateRecipes(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
        this.modId = modId;
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookup, RecipeOutput output) {
        return new VanillaRecipeProvider(lookup, output) {

            @Override
            protected void buildRecipes() {
                CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modId)).forEach(woodType -> {
                    Block plankBlock = BuiltInRegistries.BLOCK.getValue(AppleCrateAPI.getPlanksResourceLocation().get(woodType));
                    try {
                        if (plankBlock.equals(Blocks.AIR))
                            throw WoodException.INSTANCE.noSuchBlockError(woodType);
                        var crate = (Item) Item.BY_BLOCK.getOrDefault(CrateWoodType.getBlock(woodType), Items.AIR);
                        shaped(RecipeCategory.DECORATIONS, crate)
                                .pattern("s s")
                                .pattern("ppp")
                                .define('s', Ingredient.of(Items.STICK))
                                .define('p', Ingredient.of(plankBlock))
                                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                                .group("crates")
                                .save(output);
                    } catch (WoodException e) {
                        LogUtils.getLogger().error(e.getMessage());
                    }
                });
            }
        };
    }

    @Override
    public String getName() {
        return "CrateRecipeBuilder";
    }
}