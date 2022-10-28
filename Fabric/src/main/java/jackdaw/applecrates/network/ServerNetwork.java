package jackdaw.applecrates.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

public class ServerNetwork {

    public static void registerServerPackets() {
        //Handles when server packet is received on server
        ServerPlayNetworking.registerGlobalReceiver(PacketId.CHANNEL, (server, serverPlayer, handler, buf, responseSender) -> {
            byte FORGE_PACKET_ID = buf.readByte();//FORGE PACKET COMPAT
            switch (FORGE_PACKET_ID) {
                case PacketId.SPACKET_TRADE -> server.execute(() -> {
                    ProcessServerPacket.handleTrade(serverPlayer);
                });
                case PacketId.SPACKET_SALE -> server.execute(() -> {
                    ProcessServerPacket.handleSale(serverPlayer);
                });
            }
        });
    }

    public static FriendlyByteBuf sPacketTrade() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(PacketId.SPACKET_TRADE); //FORGE PACKET COMPAT
        return buf;
    }

    public static FriendlyByteBuf sPacketSale() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(PacketId.SPACKET_SALE); //FORGE PACKET COMPAT
        return buf;
    }
}
