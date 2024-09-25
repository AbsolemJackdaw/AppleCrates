package jackdaw.applecrates.network;

import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SCrateTradeSync {

    public SCrateTradeSync() {
    }

    public SCrateTradeSync(FriendlyByteBuf read) {
        decode(read);
    }

    public void decode(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            new ServerCrateSync().run(player);
        });
        context.get().setPacketHandled(true);
    }
}
