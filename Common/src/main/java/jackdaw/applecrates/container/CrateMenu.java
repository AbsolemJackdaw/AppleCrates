package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CrateMenu extends AbstractContainerMenu {

    public IStackHandlerAdapter adapter;

    public boolean isUnlimitedShop = false;
    protected Level volatileLevel;
    protected BlockPos volatilePos;

    public CrateMenu(MenuType<?> type, int id, Inventory inventory, CrateBlockEntityBase crate, boolean unlimited) {
        this(type, id, inventory, crate.stackHandler, unlimited);
        volatileLevel = crate.getLevel();
        volatilePos = crate.getBlockPos();
    }

    protected CrateMenu(MenuType<?> type, int id, Inventory inventory, IStackHandlerAdapter adapter, boolean ul) {
        super(type, id);

        this.adapter = adapter;
        this.isUnlimitedShop = ul;

        Content.menuSlots.accept(this);

        for (int y = 0; y < 4; ++y) { //player inventory
            for (int x = 0; x < 9; ++x) {
                int index_rotated = (y + 3) % 4;
                int yPos = playerInventoryY() + index_rotated * (index_rotated == 3 ? 19 : 18);
                int xPos = playerInventoryX() + x * 18;
                int index = x + y * 9;
                this.addSlot(new Slot(inventory, index, xPos, yPos));
            }
        }
    }

    public boolean isOwner() {
        return false;
    }

    protected int playerInventoryX() {
        return isOwner() ? 19 : 8;
    }

    protected int playerInventoryY() {
        return isOwner() ? 108 : 57;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (!slots.get(index).hasItem())
            return ItemStack.EMPTY;

        if (Constants.isInCrateStock(index))
            this.moveItemStackTo(slots.get(index).getItem(), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);

        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player pPlayer) {
        pPlayer.getInventory().placeItemBackInInventory(this.adapter.getInteractableTradeItem(0));
        this.adapter.setInteractableTradeItem(0, ItemStack.EMPTY);

        super.removed(pPlayer);

        if (volatileLevel != null && volatilePos != null)
            if (volatileLevel.getBlockEntity(volatilePos) instanceof CrateBlockEntityBase crate) //includes null check
            {
                volatileLevel.sendBlockUpdated(volatilePos, volatileLevel.getBlockState(volatilePos), volatileLevel.getBlockState(volatilePos), 3);
                crate.setChanged();
            }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        if (slot.index == Constants.OUTSLOT || slot.index == Constants.MONEYSLOT)
            return false;
        else
            return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public @NotNull Slot addSlot(@NotNull Slot slot) {
        return super.addSlot(slot);
    }
}
