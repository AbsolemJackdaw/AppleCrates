//package jackdaw.applecrates.datagen;
//
//import jackdaw.applecrates.api.CrateWoodType;
//import net.minecraft.data.DataGenerator;
//import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
//import net.neoforged.neoforge.client.model.generators.ModelFile;
//import net.neoforged.neoforge.common.data.ExistingFileHelper;
//
//public class CrateStates extends BlockStateProvider {
//    public CrateStates(String modid, DataGenerator generator, ExistingFileHelper existingFileHelper) {
//        super(generator.getPackOutput(), modid, existingFileHelper);
//    }
//
//    @Override
//    protected void registerStatesAndModels() {
//        CrateWoodType.values().forEach(crateWoodType -> {
//            horizontalBlock(CrateWoodType.getBlock(crateWoodType), new ModelFile.ExistingModelFile(modLoc("block/" + crateWoodType.getBlockRegistryName()), models().existingFileHelper), 180);
//        });
//    }
//}
