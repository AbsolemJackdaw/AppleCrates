package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.ClientConfig;
import jackdaw.applecrates.compat.SectionProtection;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

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
}
