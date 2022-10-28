package jackdaw.applecrates.network;

import jackdaw.applecrates.container.CrateMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ProcessServerPacket {
    public static void handleTrade(ServerPlayer serverPlayer) {
        if (serverPlayer.containerMenu instanceof CrateMenu menu) {
            confirmTrade(menu, serverPlayer, 0);
            confirmTrade(menu, serverPlayer, 1);
        }
    }

    private static void confirmTrade(CrateMenu menu, ServerPlayer player, int slot) {
        ItemStack stack = menu.interactableSlots.getItemInSlot(slot).copy();
        menu.priceAndSaleSlots.setItemInSlot(slot, stack.copy());
        player.getInventory().add(stack.copy());
        menu.interactableSlots.setItemInSlot(slot, ItemStack.EMPTY);

    }

    public static void handleSale(ServerPlayer serverPlayer) {
        if (serverPlayer.containerMenu instanceof CrateMenu menu) {
            menu.tryMovePaymentToInteraction();
        }
    }
}
