package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotCrateStock extends Slot {
    private final boolean isOwner;

    public SlotCrateStock(Container container, int index, int xPosition, int yPosition, boolean isOwner) {
        super(container, index, xPosition, yPosition);
        this.isOwner = isOwner;
    }

    @Override
    public boolean isActive() {
        return isOwner;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return getContainerSlot() < Constants.TOTALCRATESTOCKLOTS;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return getContainerSlot() != Constants.TOTALCRATESTOCKLOTS && isOwner;
    }
}
