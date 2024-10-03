package jackdaw.applecrates.network;

import jackdaw.applecrates.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CrateChannel {
    private static final String PROTOCOL = "1.0.1";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Constants.MODID, "network"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();

    public static void init() {
        CrateChannel.NETWORK.registerMessage(PacketId.SPACKET_TRADE, SCrateTradeSync.class, SCrateTradeSync::encode, SCrateTradeSync::decode, SCrateTradeSync::handle);
        CrateChannel.NETWORK.registerMessage(PacketId.SPACKET_SALE, SGetSale.class, SGetSale::encode, SGetSale::decode, SGetSale::handle);
        CrateChannel.NETWORK.registerMessage(PacketId.SPACKET_ADDOWNER, SAddOwner.class, SAddOwner::encode, SAddOwner::decode, SAddOwner::handle);
    }
}
