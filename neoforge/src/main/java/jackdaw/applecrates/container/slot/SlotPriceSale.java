package jackdaw.applecrates.container.slot;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class SlotPriceSale extends SlotItemHandler {
    public SlotPriceSale(IItemHandler itemHandler, int index) {
        super(itemHandler, index, 0, 0);
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
