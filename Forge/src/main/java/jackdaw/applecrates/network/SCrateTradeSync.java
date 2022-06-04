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

    public void encode(FriendlyByteBuf buf) {

    }

    public void decode(FriendlyByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player.containerMenu instanceof CrateMenu menu && player != null) {
                setSlot(menu, player, 0);
                setSlot(menu, player, 1);
            }
        });
        context.get().setPacketHandled(true);
        //Check the fcw for what client packets need, they may need an unsafeWhenRunOn
    }

    private static void setSlot(CrateMenu menu, ServerPlayer player, int slot){
        ItemStack give = menu.interactableSlots.getStackInSlot(slot).copy();
        //ItemStack get = menu.interactableSlots.getStackInSlot(1).copy();
        menu.priceAndSaleSlots.setStackInSlot(slot, give.copy());
        //menu.priceAndSaleSlots.setStackInSlot(1, get.copy());
        player.getInventory().add(give.copy());
        //player.getInventory().add(get.copy());
        menu.interactableSlots.setStackInSlot(slot, ItemStack.EMPTY);
       //menu.interactableSlots.setStackInSlot(1, ItemStack.EMPTY);
    }
}
