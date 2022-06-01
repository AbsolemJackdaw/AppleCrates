package jackdaw.applecrates.network;

import jackdaw.applecrates.container.CrateMenu;
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

    public void encode(FriendlyByteBuf buf) {

    }

    public void decode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player.containerMenu instanceof CrateMenu menu) {
                menu.tryMovePayementToInteraction();
            }
        });
        context.get().setPacketHandled(true);
    }
}
