package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.CrateBlockBase;
import jackdaw.applecrates.client.ClientConfig;
import jackdaw.applecrates.compat.SectionProtection;
import jackdaw.applecrates.item.CrateItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MODID)
public class AppleCrates {
    public static final boolean GEN_VANILLA_CRATES = false;

    public AppleCrates() {
        //Adjust constant
        Constants.IS_DATA_GEN = FMLLoader.getLaunchHandler().isData();

        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        //init config for Forge
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.INSTANCE.SPEC, "apple_crates_client.toml");
        //reference this config to be used in common code
        Content.clientConfig = ClientConfig.INSTANCE;

        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareForRegistry(Constants.MODID, GeneralRegistry.BLOCKS, GeneralRegistry.ITEMS, GeneralRegistry.BLOCK_ENTITY_TYPES);

    }

    public void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            for (RegistryObject<Item> item : GeneralRegistry.ITEMS.getEntries())
                event.accept(item);
        }
    }
}
