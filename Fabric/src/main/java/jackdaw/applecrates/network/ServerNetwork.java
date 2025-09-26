package jackdaw.applecrates.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ServerNetwork {

    public static void registerServerPackets() {
        PayloadTypeRegistry.playC2S().register(PacketId.SPACKET_ADDOWNER_TYPE, PacketId.SPACKET_ADDOWNER_CODEC);
        PayloadTypeRegistry.playC2S().register(PacketId.SPACKET_TRADE_TYPE, PacketId.SPACKET_TRADE_CODEC);
        PayloadTypeRegistry.playC2S().register(PacketId.SPACKET_SALE_TYPE, PacketId.SPACKET_SALE_CODEC);
    }

    public static void registerPayloads() {
        ServerPlayNetworking.registerGlobalReceiver(PacketId.SPACKET_SALE_TYPE, (payload, context) -> {
            payload.run(context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(PacketId.SPACKET_TRADE_TYPE, (payload, context) -> {
            payload.run(context.player());
        });
        ServerPlayNetworking.registerGlobalReceiver(PacketId.SPACKET_ADDOWNER_TYPE, (payload, context) -> {
            payload.run(context.player());
        });
    }
}
