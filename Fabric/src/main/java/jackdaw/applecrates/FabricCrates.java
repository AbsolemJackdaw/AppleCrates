package jackdaw.applecrates;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class FabricCrates implements ModInitializer {


    @Override
    public void onInitialize() {
        AppleCrateAPI.AppleCrateBuilder.registerVanilla();

        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(Constants.MODID)).forEach(crateWoodType -> {
            var crate = new CrateBlock();
            Registry.register(Registry.BLOCK, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(Registry.ITEM, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), new BlockItem(crate, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        });
    }
}
