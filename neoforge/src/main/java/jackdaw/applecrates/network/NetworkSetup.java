package jackdaw.applecrates.network;

import jackdaw.applecrates.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkSetup {
    private static final String PROTOCOL = "1.0.1";

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event) {
        var NETWORK = event.registrar(Constants.MODID).versioned(PROTOCOL);

        NETWORK.playToServer(SAddOwner.TYPE, SAddOwner.STREAM_CODEC, new DirectionalPayloadHandler<>(SAddOwner.PayLoadHandler::handle, SAddOwner.PayLoadHandler::handle));
        NETWORK.playToServer(SCrateTradeSync.TYPE, SCrateTradeSync.STREAM_CODEC, new DirectionalPayloadHandler<>(SCrateTradeSync.PayLoadHandler::handle, SCrateTradeSync.PayLoadHandler::handle));
        NETWORK.playToServer(SGetSale.TYPE, SGetSale.STREAM_CODEC, new DirectionalPayloadHandler<>(SGetSale.PayLoadHandler::handle, SGetSale.PayLoadHandler::handle));

    }
}
