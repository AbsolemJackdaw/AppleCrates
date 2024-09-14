package jackdaw.applecrates;

import com.mojang.datafixers.kinds.Const;
import jackdaw.applecrates.client.ClientConfig;
import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.compat.SectionProtection;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Arrays;
import java.util.List;

@Mod(Constants.MODID)
public class AppleCrates {
    public static final boolean GEN_VANILLA_CRATES = false;


    public AppleCrates() {
        //Adjust constant
        Constants.IS_DATA_GEN = !FMLLoader.getLaunchHandler().isData();

        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, "apple_crates_client.toml");

        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareForRegistry(Constants.MODID, GeneralRegistry.BLOCKS, GeneralRegistry.ITEMS, GeneralRegistry.BLOCK_ENTITY_TYPES);
    }
}
