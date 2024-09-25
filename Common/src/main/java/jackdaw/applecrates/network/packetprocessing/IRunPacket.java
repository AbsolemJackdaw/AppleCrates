package jackdaw.applecrates.network.packetprocessing;

import net.minecraft.server.level.ServerPlayer;

public interface IRunPacket {
    void run(ServerPlayer player);

}
