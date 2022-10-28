package jackdaw.applecrates.client;


import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeClientConfig {
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        ClientConfig.crateItemRendering = builder.defineEnum("crate_item_rendering", ClientConfig.CrateItemRendering.THREE).get();
    }
}
