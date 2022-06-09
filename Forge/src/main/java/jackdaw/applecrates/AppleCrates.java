package jackdaw.applecrates;

import jackdaw.applecrates.compat.SectionProtection;
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
        GeneralRegistry.addModSupport("projectbrazier", "orange", "woods/orange/");
        GeneralRegistry.addModSupport("projectbrazier", "apple", "woods/apple/");

        Stream<WoodType> woodType = WoodType.values();
        //call after mod compat so it can reg new WoodTypes
        GeneralRegistry.prepareMaps();
    }
}
