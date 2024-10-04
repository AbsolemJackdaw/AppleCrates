package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CrateMenuBuyer extends CrateMenu {

    //Registry overload for menu type registry
    //client
    public CrateMenuBuyer(MenuType<?> type, int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(type, id, inventory, adapter, unlimited);
    }

    public CrateMenuBuyer(MenuType<?> type, int id, Inventory inventory, CrateBlockEntityBase crate, boolean unlimited) {
        super(type, id, inventory, crate, unlimited);
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        if (slotID == Constants.PAYSLOT) {
            super.clicked(slotID, mouseButton, click, player);
            updateSellItem();
        } else if (slotID == Constants.OUTSLOT) {
            if (mouseButton != 0
                    || (!(click.equals(ClickType.PICKUP) || click.equals(ClickType.QUICK_MOVE)))
                    || adapter.getInteractableTradeItem(1).isEmpty()
                    || (!getCarried().isEmpty() && getCarried().getCount() + adapter.getInteractableTradeItem(1).getCount() > getCarried().getMaxStackSize()))
                return;
            super.clicked(slotID, mouseButton, click, player);
            adapter.getInteractableTradeItem(0).shrink(adapter.getSavedTradeSlotsItem(0).getCount());
            adapter.getCrateStock().updateStackInPaymentSlot(adapter.getSavedTradeSlotsItem(0), isUnlimitedShop);
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
            moveItemStackTo(adapter.getInteractableTradeItem(0), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);
            updateSellItem();
        }
        //nota : quickmove works in tandem with 'clicked', which already deducts payment.
        if (index == Constants.OUTSLOT) {
            //payment is processed in 'clicked'
            moveItemStackTo(adapter.getInteractableTradeItem(1), Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false);
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(player, index);
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        //move the output slot back to crate stock
        updateSellItem();
    }

    protected void updateSellItem() {
        ItemStack payment = this.adapter.getInteractableTradeItem(0);
        ItemStack toPay = this.adapter.getSavedTradeSlotsItem(0);
        var isBuySlotEmpty = this.adapter.getInteractableTradeItem(1).isEmpty();
        var isSamePay = ItemStack.isSameItemSameTags(toPay, payment) && !toPay.isEmpty();
        var hasEnough = payment.getCount() >= toPay.getCount();

        if (isBuySlotEmpty && isSamePay && hasEnough)
            movefromStockToSaleSlot(this.adapter.getSavedTradeSlotsItem(1).copy());
        else if (!isBuySlotEmpty && !hasEnough || payment.isEmpty())
            moveItemStackTo(this.adapter.getInteractableTradeItem(1), Constants.CRATESTARTSLOT, Constants.CRATEENDSLOT, false);
    }

    public void tryMovePaymentToInteraction() {
        //if there's a stack that isn't payment in the slot, put it back in the inventory, then pop in new items that are payment
        ItemStack itemstack = this.adapter.getInteractableTradeItem(0);
        if (!itemstack.isEmpty()) {
            if (!this.moveItemStackTo(itemstack, Constants.PLAYERSTARTSLOT, Constants.PLAYERENDSLOT, false)) {
                return;
            }
            this.adapter.setInteractableTradeItem(0, itemstack);
        }

        if (this.adapter.getInteractableTradeItem(0).isEmpty()) {
            ItemStack give = this.adapter.getSavedTradeSlotsItem(0);
            this.moveFromInventoryToPaymentSlot(give);
        }
    }

    protected void moveFromInventoryToPaymentSlot(ItemStack give) {
        if (!give.isEmpty()) {
            for (int i = Constants.PLAYERSTARTSLOT; i < Constants.PLAYERENDSLOT; ++i) {
                ItemStack stackinSlot = this.slots.get(i).getItem();
                if (!stackinSlot.isEmpty() && ItemStack.isSameItemSameTags(give, stackinSlot)) {
                    ItemStack paymentSlot = this.adapter.getInteractableTradeItem(0);
                    int j = paymentSlot.isEmpty() ? 0 : paymentSlot.getCount();
                    int k = Math.min(give.getMaxStackSize() - j, stackinSlot.getCount());
                    ItemStack copy = stackinSlot.copy();
                    int l = j + k;
                    stackinSlot.shrink(k);
                    copy.setCount(l);
                    this.adapter.setInteractableTradeItem(0, copy);
                    if (l >= give.getMaxStackSize()) {
                        break;
                    }
                }
            }
            updateSellItem();
        }
    }

    protected void movefromStockToSaleSlot(ItemStack get) {
        if (!get.isEmpty() && (!outOfStock() || isUnlimitedShop)) {
            if (isUnlimitedShop)
                this.adapter.setInteractableTradeItem(1, get.copy());
            else
                for (int i = Constants.CRATESTARTSLOT; i < Constants.CRATEENDSLOT; ++i) {
                    ItemStack stackinSlot = this.slots.get(i).getItem();
                    if (!stackinSlot.isEmpty() && ItemStack.isSame(get, stackinSlot)) {
                        ItemStack pickupSlot = this.adapter.getInteractableTradeItem(1);
                        int j = pickupSlot.isEmpty() ? 0 : pickupSlot.getCount();
                        int k = Math.min(get.getCount() - j, stackinSlot.getCount());
                        ItemStack copy = stackinSlot.copy();
                        int l = j + k;
                        stackinSlot.shrink(k);
                        copy.setCount(l);
                        this.adapter.setInteractableTradeItem(1, copy);
                        if (l >= get.getCount()) {
                            break;
                        }
                    }
                }
        }
    }

    public boolean outOfStock() {
        int total = 0;
        var buying = adapter.getSavedTradeSlotsItem(1);
        for (int i = 0; i < Constants.TOTALCRATESTOCKLOTS - 1; i++) //last slot is for payment
        {
            var stack = this.adapter.getCrateStockItem(i);
            if (!stack.isEmpty() && ItemStack.isSame(stack, buying))
                total += stack.getCount();
        }
        return total < buying.getCount();
    }
}
