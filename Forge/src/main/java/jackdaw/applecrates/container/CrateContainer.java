package jackdaw.applecrates.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public class CrateContainer extends AbstractContainerMenu {


    public CrateContainer(@Nullable MenuType<?> menutype, int id) {
        super(menutype, id);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
