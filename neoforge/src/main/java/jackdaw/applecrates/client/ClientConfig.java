package jackdaw.applecrates.client;

import jackdaw.applecrates.EnumCrateItemRendering;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig implements IClientConfig {

    public static final ClientConfig INSTANCE = new ClientConfig();

    public final ModConfigSpec SPEC;
    private ModConfigSpec.EnumValue<EnumCrateItemRendering> configValue;

    //class load
    {
        ModConfigSpec.Builder configBuilder = new ModConfigSpec.Builder();
        setupConfig(configBuilder);
        SPEC = configBuilder.build();
    }

    @Override
    public EnumCrateItemRendering getCrateItemRenderingValue() {
        return configValue.get();
    }

    private void setupConfig(ModConfigSpec.Builder builder) {
        configValue = builder.defineEnum("crate_item_rendering", EnumCrateItemRendering.THREE);
    }
}
