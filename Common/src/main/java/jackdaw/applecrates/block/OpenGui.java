package jackdaw.applecrates.block;

import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface OpenGui {
    void accept(ServerPlayer player, CrateBE crate, Component component, boolean isOwner);
}
