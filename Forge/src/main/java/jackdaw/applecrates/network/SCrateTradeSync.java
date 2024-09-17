package jackdaw.applecrates.network;

import jackdaw.applecrates.container.CrateMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SCrateTradeSync {

    public SCrateTradeSync() {
    }

    public SCrateTradeSync(FriendlyByteBuf read) {
        decode(read);
    }

    public void decode(FriendlyByteBuf buf) {

    }

    public void encode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player.containerMenu instanceof CrateMenu menu) {
                confirmTrade(menu, player, 0);
                confirmTrade(menu, player, 1);
            }
        });
        context.get().setPacketHandled(true);
    }

    private void confirmTrade(CrateMenu menu, ServerPlayer player, int slot) {
        ItemStack stack = menu.interactableTradeSlots.getStackInSlot(slot).copy();
        menu.savedTradeSlots.setStackInSlot(slot, stack.copy());
        player.getInventory().add(stack.copy());
        menu.interactableTradeSlots.setStackInSlot(slot, ItemStack.EMPTY);

    }
}
