package jackdaw.applecrates.network.packetprocessing;

import jackdaw.applecrates.container.CrateMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ServerCrateSync implements IRunPacket {
    @Override
    public void run(ServerPlayer player) {
        if (player.containerMenu instanceof CrateMenu menu) {
            confirmTrade(menu, player, 0);
            confirmTrade(menu, player, 1);
        }
    }

    private static void confirmTrade(CrateMenu menu, ServerPlayer player, int slot) {
        ItemStack stack = menu.adapter.getInteractableTradeItem(slot).copy();
        menu.adapter.setSavedTradeSlotItem(slot, stack.copy());
        player.getInventory().add(stack.copy());
        menu.adapter.setInteractableTradeItem(slot, ItemStack.EMPTY);
    }
}
