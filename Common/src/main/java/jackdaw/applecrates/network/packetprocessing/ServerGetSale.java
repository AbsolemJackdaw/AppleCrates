package jackdaw.applecrates.network.packetprocessing;

import jackdaw.applecrates.container.CrateMenuBuyer;
import net.minecraft.server.level.ServerPlayer;

public class ServerGetSale implements IRunPacket {
    @Override
    public void run(ServerPlayer player) {
        if (player.containerMenu instanceof CrateMenuBuyer menu) {
            menu.tryMovePaymentToInteraction();
        }
    }
}
