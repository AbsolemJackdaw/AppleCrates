package jackdaw.applecrates.client;


import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeClientConfig {
    public static final ForgeConfigSpec SPEC;
    public static ForgeConfigSpec.EnumValue<ClientConfig.CrateItemRendering> configSpec;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        configSpec = builder.defineEnum("crate_item_rendering", ClientConfig.CrateItemRendering.THREE);
    }
}
