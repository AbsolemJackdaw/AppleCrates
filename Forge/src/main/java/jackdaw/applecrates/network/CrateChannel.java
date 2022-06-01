package jackdaw.applecrates.network;

import jackdaw.applecrates.AppleCrates;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CrateChannel {
    private static final String PROTOCOL = "1.0.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(AppleCrates.MODID, "network"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public static void init() {
        int id = 0;
        CrateChannel.NETWORK.registerMessage(id++, SCrateTradeSync.class, SCrateTradeSync::encode, SCrateTradeSync::new, SCrateTradeSync::handle);
        CrateChannel.NETWORK.registerMessage(id++, SGetSale.class, SGetSale::encode, SGetSale::new, SGetSale::handle);

    }
}
