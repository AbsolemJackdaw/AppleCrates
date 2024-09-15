package jackdaw.applecrates.network;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.PacketId;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CrateChannel {
    private static final String PROTOCOL = "1.0.0";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Constants.MODID, "network"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public static void init() {
        CrateChannel.NETWORK.registerMessage(PacketId.SPACKET_TRADE, SCrateTradeSync.class, SCrateTradeSync::encode, SCrateTradeSync::new, SCrateTradeSync::handle);
        CrateChannel.NETWORK.registerMessage(PacketId.SPACKET_SALE, SGetSale.class, SGetSale::encode, SGetSale::new, SGetSale::handle);

    }
}
