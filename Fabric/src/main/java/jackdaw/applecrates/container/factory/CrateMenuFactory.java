package jackdaw.applecrates.container.factory;

import jackdaw.applecrates.api.GeneralRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;

public record CrateMenuFactory(MenuConstructor menuConstructor, Component screenTitle,
                               boolean isunlimited) implements ExtendedScreenHandlerFactory<GeneralRegistry.CrateData> {

    @Override
    public GeneralRegistry.CrateData getScreenOpeningData(ServerPlayer player) {
        return new GeneralRegistry.CrateData(isunlimited);
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
