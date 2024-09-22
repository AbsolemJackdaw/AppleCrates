package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.FabricCrates;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class CrateMenuBuyer extends CrateMenu {

    //Registry overload for menu type registry
    //client
    public CrateMenuBuyer(int id, Inventory inventory, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_BUYER, id, inventory, unlimited);
    }

    public CrateMenuBuyer(int id, Inventory inventory, CrateBE crate, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_BUYER, id, inventory, crate, unlimited);
    }

    @Override
    protected int playerInventoryX() {
        return 8;
    }

    @Override
    protected int playerInventoryY() {
        return 57;
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        if (slotID == Constants.PAYSLOT) {
            super.clicked(slotID, mouseButton, click, player);
            updateSellItem();
        } else if (slotID == Constants.OUTSLOT) {
            if (mouseButton != 0
                    || (!(click.equals(ClickType.PICKUP) || click.equals(ClickType.QUICK_MOVE)))
                    || interactableTradeSlots.getItem(1).isEmpty()
                    || getCarried().getCount() + interactableTradeSlots.getItem(1).getCount() >= getCarried().getMaxStackSize())
                return;
            super.clicked(slotID, mouseButton, click, player);
            interactableTradeSlots.getItem(0).shrink(savedTradeSlots.getItem(0).getCount());
            crateStock.updateStackInPaymentSlot(savedTradeSlots.getItem(0), isUnlimitedShop);
            updateSellItem();
        } else {
            super.clicked(slotID, mouseButton, click, player);
            updateSellItem();
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        //if payment is updated or a stack from the player inventory is quickmoved into interactables
        if ((Constants.isInPlayerInventory(index) && this.moveItemStackTo(slots.get(index).getItem(), Constants.PAYSLOT, Constants.OUTSLOT, false))) {
            moveItemStackTo(slots.get(index).getItem(), Constants.PAYSLOT, Constants.OUTSLOT, false);
            updateSellItem();
        } else if (index == Constants.PAYSLOT) {
            moveItemStackTo(interactableTradeSlots.getItem(0), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);
            updateSellItem();
        }
        //nota : quickmove works in tandem with 'clicked', which already deducts payment.
        if (index == Constants.OUTSLOT) {
            //payment is processed in 'clicked'
            moveItemStackTo(interactableTradeSlots.getItem(1), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(player, index);
    }
}
