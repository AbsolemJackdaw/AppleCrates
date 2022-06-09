package jackdaw.applecrates;

import jackdaw.applecrates.compat.SectionProtection;
import jackdaw.applecrates.compat.api.AppleCrateAPI;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Stream;

@Mod(AppleCrates.MODID)
public class AppleCrates {
    public static final String MODID = "applecrates";

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        //test for wood
        String brazier = "projectbrazier";
        AppleCrateAPI.registerForCrate(brazier, "orange", "woods/orange/");
        AppleCrateAPI.registerForCrate(brazier, "apple", "woods/apple/");

        Stream<WoodType> woodType = WoodType.values();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareMaps();
    }
}
