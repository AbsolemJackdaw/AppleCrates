package jackdaw.applecrates.client;

import jackdaw.applecrates.client.besr.CommonClientConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig extends CommonClientConfig {
    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        CommonClientConfig.crateItemRendering = builder.defineEnum("crate_item_rendering", CrateItemRendering.THREE).get();
    }
}
