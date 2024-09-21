package jackdaw.applecrates.container;

import io.netty.buffer.Unpooled;
import jackdaw.applecrates.FabricCrates;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.function.Consumer;

public class CrateMenuFactory implements ExtendedScreenHandlerFactory {

    final FriendlyByteBuf buf;
    final FriendlyByteBuf buf_opening_data;

    final Component comp;

    public CrateMenuFactory(Component comp, Consumer<FriendlyByteBuf> buf) {
        this.buf = this.buf_opening_data = new FriendlyByteBuf(Unpooled.buffer());
        buf.accept(this.buf);
        buf.accept(this.buf_opening_data);
        this.comp = comp;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeBytes(this.buf_opening_data);
    }

    @Override
    public Component getDisplayName() {
        return comp;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return FabricCrates.CRATE_MENU_OWNER.create(i, inventory, this.buf);
    }
}
