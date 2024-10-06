package jackdaw.applecrates.network.packetprocessing;

import jackdaw.applecrates.container.CrateMenuOwner;
import net.minecraft.server.level.ServerPlayer;

public class ServerAddOwner {
    public void run(ServerPlayer player, String newOwnerUsername) {
        if (player.containerMenu instanceof CrateMenuOwner menu) {
            menu.addOwner(player, newOwnerUsername);
        }
    }
}
