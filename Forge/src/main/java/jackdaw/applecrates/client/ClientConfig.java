package jackdaw.applecrates.client;

import jackdaw.applecrates.EnumCrateItemRendering;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig implements IClientConfig {

    public static final ClientConfig INSTANCE = new ClientConfig();

    public final ForgeConfigSpec SPEC;
    private ForgeConfigSpec.EnumValue<EnumCrateItemRendering> configValue;

    //class load
    {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    @Override
    public EnumCrateItemRendering getCrateItemRenderingValue() {
        return configValue.get();
    }

    private void setupConfig(ForgeConfigSpec.Builder builder) {
        configValue = builder.defineEnum("crate_item_rendering", EnumCrateItemRendering.THREE);
    }
}
