package jackdaw.applecrates.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;

public class ClientNetwork {


    public static void registerClientPackets() {
        //Handles when client packet is received on client
        ClientPlayNetworking.registerGlobalReceiver(PacketId.CHANNEL, (client, handler, buf, responseSender) -> {
            int FORGE_PACKET_ID = buf.readByte();//FORGE PACKET COMPAT
            if (FORGE_PACKET_ID == PacketId.CPACKET) {//FORGE PACKET COMPAT
            }
        });
    }

    public static FriendlyByteBuf cPacket(int entityId, String[] names) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(PacketId.CPACKET); //FORGE PACKET COMPAT
        return buf;
    }
}
