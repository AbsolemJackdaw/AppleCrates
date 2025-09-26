package jackdaw.applecrates.network.packetprocessing;

import jackdaw.applecrates.container.CrateMenuOwner;
import jackdaw.applecrates.network.PacketId;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record ServerAddOwner(String newOwnerUsername) implements IRunPacket, CustomPacketPayload {

    @Override
    public void run(ServerPlayer player) {
        if (player.containerMenu instanceof CrateMenuOwner menu) {
            menu.addOwner(player, newOwnerUsername);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketId.SPACKET_ADDOWNER_TYPE;
    }
}
