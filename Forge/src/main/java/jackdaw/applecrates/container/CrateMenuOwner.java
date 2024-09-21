package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class CrateMenuOwner extends CrateMenu {

    //Registry overload for menu type registry
    //client
    public CrateMenuOwner(int id, Inventory inventory, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, unlimited);
    }

    public CrateMenuOwner(int id, Inventory inventory, CrateBE crate, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, crate, unlimited);
    }

    @Override
    protected boolean isOwner() {
        return true;
    }

    @Override
    protected int playerInventoryX() {
        return 19;
    }

    @Override
    protected int playerInventoryY() {
        return 108;
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        if (slotID == Constants.MONEYSLOT && this.getCarried().isEmpty()) {
            if (click.equals(ClickType.PICKUP)) {
                this.setCarried(pickUpPayment());
            } else if (click.equals(ClickType.QUICK_MOVE)) {
                //custom quickmove code because of locking down the payment slot so noone takes out anything ever
                this.moveItemStackTo(pickUpPayment(), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);
            }
            return;
        }
        super.clicked(slotID, mouseButton, click, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (Constants.isInPlayerInventory(index) && !this.moveItemStackTo(slots.get(index).getItem(), Constants.CRATESTARTSLOT, Constants.CRATEENDSLOTALL, false)) //only enable move to crate if player is the owner
            return ItemStack.EMPTY;
        else if (Constants.isInInteractables(index) && !this.moveItemStackTo(slots.get(index).getItem(), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false)) //only enable move to crate if player is the owner
            return ItemStack.EMPTY;
        else if (index == Constants.MONEYSLOT) {
            //code is never reached.
            //the payement slot (or nr 34) is void form pickup and cannot trigger quickMoveStack.
            //custom quickmove code can be found in #clicked
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(player, index);
    }
}
