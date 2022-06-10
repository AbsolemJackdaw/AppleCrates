package jackdaw.applecrates;

import jackdaw.applecrates.compat.LocalCompat;
import jackdaw.applecrates.compat.SectionProtection;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(AppleCrates.MODID)
public class AppleCrates {
    public static final String MODID = "applecrates";

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        LocalCompat.INSTANCE.init();

        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareMaps();
    }
}
