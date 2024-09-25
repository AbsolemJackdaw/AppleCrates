package jackdaw.applecrates.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class SlotPriceSale extends Slot {
    public SlotPriceSale(Container container, int i) {
        super(container, i, 0, 0);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }
}
