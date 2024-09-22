package jackdaw.applecrates.container;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;

import java.util.function.Consumer;

public class CrateMenuFactory implements ExtendedScreenHandlerFactory {

    final FriendlyByteBuf buf;
    final MenuConstructor menuConstructor;

    final Component screenTitle;

    public CrateMenuFactory(MenuConstructor menuConstructor, Component component, Consumer<FriendlyByteBuf> buf) {
        this.buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.accept(this.buf);
        this.menuConstructor = menuConstructor;
        this.screenTitle = component;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBytes(this.buf);
    }

    @Override
    public Component getDisplayName() {
        return screenTitle;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return this.menuConstructor.createMenu(id, inventory, player);
    }
}
