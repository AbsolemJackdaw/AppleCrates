package jackdaw.applecrates.network;

import jackdaw.applecrates.container.CrateMenu;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ServerNetwork {

    public static void registerServerPackets() {
        //Handles when server packet is received on server
        ServerPlayNetworking.registerGlobalReceiver(PacketId.CHANNEL, (server, serverPlayer, handler, buf, responseSender) -> {
            byte FORGE_PACKET_ID = buf.readByte();//FORGE PACKET COMPAT
            switch (FORGE_PACKET_ID) {
                case PacketId.SPACKET_TRADE -> server.execute(() -> {
                    if (serverPlayer.containerMenu instanceof CrateMenu menu) {
                        confirmTrade(menu, serverPlayer, 0);
                        confirmTrade(menu, serverPlayer, 1);
                    }
                });
                case PacketId.SPACKET_SALE -> server.execute(() -> {
                    if (serverPlayer.containerMenu instanceof CrateMenu menu) {
                        menu.tryMovePaymentToInteraction();
                    }
                });
            }
        });
    }

    private static void confirmTrade(CrateMenu menu, ServerPlayer player, int slot) {
        ItemStack stack = menu.interactableTradeSlots.getItem(slot).copy();
        menu.savedTradeSlots.setItem(slot, stack.copy());
        player.getInventory().add(stack.copy());
        menu.interactableTradeSlots.setItem(slot, ItemStack.EMPTY);

    }

    public static FriendlyByteBuf sPacketTrade() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(PacketId.SPACKET_TRADE); //FORGE PACKET COMPAT
        return buf;
    }

    public static FriendlyByteBuf sPacketSale() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeByte(PacketId.SPACKET_SALE); //FORGE PACKET COMPAT
        return buf;
    }
}
