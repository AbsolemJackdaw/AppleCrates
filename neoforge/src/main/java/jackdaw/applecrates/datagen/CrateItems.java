//package jackdaw.applecrates.datagen;
//
//import jackdaw.applecrates.Constants;
//import jackdaw.applecrates.api.CrateWoodType;
//import net.minecraft.client.data.models.BlockModelGenerators;
//import net.minecraft.client.data.models.ItemModelGenerators;
//import net.minecraft.client.data.models.ModelProvider;
//import net.minecraft.data.PackOutput;
//import net.minecraft.resources.ResourceLocation;
//
//public class CrateItems extends ModelProvider {
//    public CrateItems(PackOutput output, String modid) {
//        super(output, modid);
//    }
//
//    @Override
//    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
//        CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(this.modId)).forEach(crateWoodType -> {
//            withExistingParent(crateWoodType.getBlockRegistryName(), ResourceLocation.fromNamespaceAndPath(this.modId, "block/" + crateWoodType.getBlockRegistryName()));
//        });
//    }
//}
