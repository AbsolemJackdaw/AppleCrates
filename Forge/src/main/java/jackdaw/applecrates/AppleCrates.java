package jackdaw.applecrates;

import jackdaw.applecrates.compat.LocalCompat;
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
    public static final boolean GEN_VANILLA_CRATES = false;

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        LocalCompat.INSTANCE.init();

        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareMaps();
    }
}
