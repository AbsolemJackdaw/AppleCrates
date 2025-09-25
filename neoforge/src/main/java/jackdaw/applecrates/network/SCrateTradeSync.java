package jackdaw.applecrates.network;

import io.netty.buffer.ByteBuf;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SCrateTradeSync() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SCrateTradeSync> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "s_cratetradesync"));

    public static final StreamCodec<ByteBuf, SCrateTradeSync> STREAM_CODEC = StreamCodec.unit(new SCrateTradeSync());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public class PayLoadHandler {
        public static void handle(final SCrateTradeSync packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer player)
                    new ServerCrateSync().run(player);
            });
        }
    }
}
