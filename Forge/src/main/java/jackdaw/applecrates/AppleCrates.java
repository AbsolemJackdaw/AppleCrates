package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.compat.SectionProtection;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

@Mod(AppleCrates.MODID)
public class AppleCrates {
    public static final String MODID = "applecrates";
    public static final String[] VANILLAWOODS = {"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};
    public static final List<String> VANILLAWOODSLIST = Arrays.asList(AppleCrates.VANILLAWOODS);
    public static final boolean GEN_VANILLA_CRATES = true;

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        AppleCrateAPI.AppleCrateBuilder.classLoader();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareMaps();
    }
}
