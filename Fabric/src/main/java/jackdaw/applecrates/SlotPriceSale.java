package jackdaw.applecrates;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class SlotPriceSale extends Slot {
    public SlotPriceSale(Container container, int i, int j, int k) {
        super(container, i, j, k);
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
