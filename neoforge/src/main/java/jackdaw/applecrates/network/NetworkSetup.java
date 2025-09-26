package jackdaw.applecrates.network;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkSetup {
    private static final String PROTOCOL = "1.0.1";

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event) {
        var NETWORK = event.registrar(Constants.MODID).versioned(PROTOCOL);

        NETWORK.playToServer(PacketId.SPACKET_ADDOWNER_TYPE, PacketId.SPACKET_ADDOWNER_CODEC, PayLoadHandler::handleAddOwner);
        NETWORK.playToServer(PacketId.SPACKET_TRADE_TYPE, PacketId.SPACKET_TRADE_CODEC, PayLoadHandler::handleCrateSync);
        NETWORK.playToServer(PacketId.SPACKET_SALE_TYPE, PacketId.SPACKET_SALE_CODEC, PayLoadHandler::handleSale);

    }

    private static class PayLoadHandler {
        private static void handleAddOwner(final ServerAddOwner packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                        if (context.player() instanceof ServerPlayer player)
                            new ServerAddOwner(packet.newOwnerUsername()).run(player);
                    })
                    .exceptionally(e -> {
                                context.disconnect(Component.literal("Error when adding owner. Closing connection"));
                                return null;
                            }
                    );
        }

        private static void handleSale(final ServerGetSale packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer player)
                    new ServerGetSale().run(player);
            });
        }

        private static void handleCrateSync(final ServerCrateSync packet, final IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer player)
                    new ServerCrateSync().run(player);
            });
        }
    }
}
