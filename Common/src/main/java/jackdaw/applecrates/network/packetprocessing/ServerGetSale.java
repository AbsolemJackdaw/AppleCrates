package jackdaw.applecrates.network.packetprocessing;

import jackdaw.applecrates.container.CrateMenuBuyer;
import jackdaw.applecrates.network.PacketId;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerGetSale() implements IRunPacket, CustomPacketPayload {
    @Override
    public void run(ServerPlayer player) {
        if (player.containerMenu instanceof CrateMenuBuyer menu) {
            menu.tryMovePaymentToInteraction();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketId.SPACKET_SALE_TYPE;
    }
}
