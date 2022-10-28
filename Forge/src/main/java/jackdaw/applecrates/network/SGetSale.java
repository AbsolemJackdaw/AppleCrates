package jackdaw.applecrates.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SGetSale {
    public SGetSale() {
    }

    public SGetSale(FriendlyByteBuf read) {
    }

    public void encode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ProcessServerPacket.handleSale(context.get().getSender());
        });
        context.get().setPacketHandled(true);
    }
}
