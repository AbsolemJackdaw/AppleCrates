package jackdaw.applecrates.network;

import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SAddOwner(String username) {
    public static SAddOwner decode(FriendlyByteBuf buf) {
        return new SAddOwner(buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(username);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            new ServerAddOwner().run(player, username);
        });
        context.get().setPacketHandled(true);
    }
}
