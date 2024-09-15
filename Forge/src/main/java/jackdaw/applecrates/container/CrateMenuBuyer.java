package jackdaw.applecrates.container;

import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CrateMenuBuyer extends CrateMenu {

    //Registry overload for menu type registry
    //client
    public CrateMenuBuyer(int id, Inventory inventory, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_BUYER.get(), id, inventory, unlimited);
    }

    public CrateMenuBuyer(int id, Inventory inventory, CrateBE crate, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_BUYER.get(), id, inventory, crate, unlimited);
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
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        if (pSlot.index == 1)
            return false;
        return super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        if (slotID == 0) {
            super.clicked(slotID, mouseButton, click, player);
            updateSellItem();
        } else if (slotID == 1) {
            if (mouseButton != 0 || click != (ClickType.PICKUP) || interactableSlots.getStackInSlot(1).isEmpty())
                return;
            super.clicked(slotID, mouseButton, click, player);
            interactableSlots.getStackInSlot(0).shrink(priceAndSaleSlots.getStackInSlot(0).getCount());
            crateStock.updateStackInPaymentSlot(priceAndSaleSlots.getStackInSlot(0), isUnlimitedShop);
            updateSellItem();
        } else {
            super.clicked(slotID, mouseButton, click, player);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        super.quickMoveStack(player, index);
        if (index >= 35 && index <= 70) { //move to 'give' slot if not owner
            if (!this.moveItemStackTo(slots.get(index).getItem(), 0, 1, false)) { //
                return ItemStack.EMPTY;
            }
            updateSellItem();
        }
        return ItemStack.EMPTY;
    }
}
