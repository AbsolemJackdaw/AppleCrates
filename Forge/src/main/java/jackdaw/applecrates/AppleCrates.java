package jackdaw.applecrates;

import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod(AppleCrates.MODID)
public class AppleCrates {
    public static final String MODID = "applecrates";

    public AppleCrates() {
        GeneralRegistry.startup();
    }

}
