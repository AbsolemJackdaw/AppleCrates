package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.ClientConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MODID)
public class AppleCrates {

    public AppleCrates(ModContainer container) {
        //Adjust constant
        //Constants.IS_DATA_GEN = FMLLoader.getLaunchHandler().isData();
        GeneralRegistry.startup();

        //init config for Forge
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.INSTANCE.SPEC, "apple_crates_client.toml");
        //reference this config to be used in common code
        Content.clientConfig = ClientConfig.INSTANCE;

        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareForRegistry(Constants.MODID, GeneralRegistry.BLOCKS, GeneralRegistry.ITEMS, GeneralRegistry.BLOCK_ENTITY_TYPES);
    }
}
