package jackdaw.applecrates;

import jackdaw.applecrates.client.ClientConfig;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.compat.SectionProtection;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

@Mod(AppleCrates.MODID)
public class AppleCrates {
    public static final String MODID = "applecrates";
    public static final String[] VANILLAWOODS = {"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};
    public static final boolean GEN_VANILLA_CRATES = false;

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "apple_crates_client.toml");

        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareForRegistry(MODID, GeneralRegistry.BLOCKS, GeneralRegistry.ITEMS, GeneralRegistry.BLOCK_ENTITY_TYPES);
    }
}
