package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class CrateMenuOwner extends CrateMenu {

    //Registry overload for menu type registry
    //client
    public CrateMenuOwner(MenuType<?> type, int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(type, id, inventory, adapter, unlimited);
    }

    public CrateMenuOwner(MenuType<?> type, int id, Inventory inventory, CrateBlockEntityBase crate, boolean unlimited) {
        super(type, id, inventory, crate, unlimited);
    }

    @Override
    public boolean isOwner() {
        return true;
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

    protected ItemStack pickUpPayment() {
        ItemStack original = adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS); //do not modify originals !
        int amount = 0;
        if (original.getOrCreateTag().contains(Constants.TAGSTOCK))
            amount = original.getTag().getInt(Constants.TAGSTOCK);

        if (amount > 0 && original.hasTag()) { //Redundant double check, but better safe then sorry
            ItemStack prepPickup = original.copy();
            CompoundTag tag = prepPickup.getTag();
            tag.remove(Constants.TAGSTOCK);
            if (tag.isEmpty()) //fix empty tag bug where an empty tag and a null tag are distinct items
                tag = null;
            prepPickup.setTag(tag);
            int pickUp = Math.min(amount, prepPickup.getMaxStackSize());
            prepPickup.setCount(pickUp);

            int updatedAmount = amount - pickUp;
            if (updatedAmount <= 0) {
                adapter.setCrateStockItem(Constants.TOTALCRATESTOCKLOTS, ItemStack.EMPTY);
            } else {
                ItemStack prepUpdate = original.copy();
                prepUpdate.getTag().putInt(Constants.TAGSTOCK, updatedAmount);
                adapter.setCrateStockItem(Constants.TOTALCRATESTOCKLOTS, prepUpdate);
            }
            return prepPickup;
        }
        return ItemStack.EMPTY;
    }
}
