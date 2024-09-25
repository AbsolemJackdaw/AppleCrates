package jackdaw.applecrates.network;

import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SGetSale {
    public SGetSale() {
    }

    public SGetSale(FriendlyByteBuf read) {
        decode(read);
    }

    public void decode(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            new ServerGetSale().run(player);
        });
        context.get().setPacketHandled(true);
    }
}
