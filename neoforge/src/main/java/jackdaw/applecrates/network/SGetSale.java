package jackdaw.applecrates.network;

import io.netty.buffer.ByteBuf;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SGetSale() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SGetSale> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "s_getsale"));
    public static final StreamCodec<ByteBuf, SGetSale> STREAM_CODEC = StreamCodec.unit(new SGetSale());

    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public class PayLoadHandler {
        public static void handle(final SGetSale packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer player)
                    new ServerGetSale().run(player);
            });
        }
    }
}
