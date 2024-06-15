package jackdaw.applecrates.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.EnumValue<CrateItemRendering> crateItemRendering;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        crateItemRendering = builder.defineEnum("crate_item_rendering", CrateItemRendering.THREE);
    }

    public static enum CrateItemRendering {
        ONE,
        THREE,
        MANY
    }
}
