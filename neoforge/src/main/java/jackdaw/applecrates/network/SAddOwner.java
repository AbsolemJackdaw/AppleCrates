package jackdaw.applecrates.network;

import io.netty.buffer.ByteBuf;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SAddOwner(String username) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SAddOwner> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "s_addowner"));

    public static final StreamCodec<ByteBuf, SAddOwner> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SAddOwner::username,
            SAddOwner::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public class PayLoadHandler {
        public static void handle(final SAddOwner packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                        if (context.player() instanceof ServerPlayer player)
                            new ServerAddOwner().run(player, packet.username);
                    })
                    .exceptionally(e -> {
                                context.disconnect(Component.literal("Error when adding owner. Closing connection"));
                                return null;
                            }
                    );
        }
    }
}
