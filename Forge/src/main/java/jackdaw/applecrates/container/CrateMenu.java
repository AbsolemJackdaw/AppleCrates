package jackdaw.applecrates.container;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static jackdaw.applecrates.container.CrateStackHandler.TAGSTOCK;

public class CrateMenu extends AbstractContainerMenu {

    public CrateStackHandler crateStock;
    public ItemStackHandler interactableTradeSlots;
    public ItemStackHandler savedTradeSlots;
    public boolean isUnlimitedShop = false;
    private Level volatileLevel;
    private BlockPos volatilePos;

    protected CrateMenu(MenuType type, int id, Inventory inventory, boolean unlimited) {
        this(type, id, inventory, new ItemStackHandler(2), new ItemStackHandler(2), new CrateStackHandler(), unlimited);
    }

    protected CrateMenu(MenuType type, int id, Inventory inventory, ItemStackHandler interaction, ItemStackHandler trade, CrateStackHandler stock, boolean ul) {
        super(type, id);
        this.interactableTradeSlots = interaction;
        this.savedTradeSlots = trade;
        this.crateStock = stock;
        this.isUnlimitedShop = ul;

        this.addSlot(new SlotItemHandler(interactableTradeSlots, 0, isOwner() ? 10 : 102, isOwner() ? 76 : 21)); //owner set pay
        this.addSlot(new SlotItemHandler(interactableTradeSlots, 1, isOwner() ? 46 : 142, isOwner() ? 76 : 21) { //owner set item
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isOwner();
            }
        });

        //invisible slots, storage for current trade
        this.addSlot(new SlotPriceSale(savedTradeSlots, 0, 0, 0)); //buyer pays
        this.addSlot(new SlotPriceSale(savedTradeSlots, 1, 0, 0)); //buyer gets

        for (int y = 0; y < 3; y++) //crate stock
            for (int x = 0; x < 10; x++)
                addSlot(new SlotCrateStock(stock, y * 10 + x, x * 18 + 10, y * 18 + 17, isOwner()));
        addSlot(new SlotCrateStock(stock, Constants.TOTALCRATESTOCKLOTS, 172, 76, isOwner()));

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

    protected boolean isOwner() {
        return false;
    }

    protected int playerInventoryX() {
        return 0;
    }

    protected int playerInventoryY() {
        return 0;
    }

    public CrateMenu(MenuType type, int id, Inventory inventory, CrateBE crate, boolean unlimited) {
        this(type, id, inventory, crate.getInteractable(), crate.getPriceAndSale(), crate.getCrateStock(), unlimited);
        volatileLevel = crate.getLevel();
        volatilePos = crate.getBlockPos();
    }

    public void tryMovePaymentToInteraction() {
        //if there's a stack that isnt payment in the slot, put it back in the inventory, then pop in new items that are payment
        ItemStack itemstack = this.interactableTradeSlots.getStackInSlot(0);
        if (!itemstack.isEmpty()) {
            if (!this.moveItemStackTo(itemstack, 34, 34 + (9 * 3), false)) {
                return;
            }
            this.interactableTradeSlots.setStackInSlot(0, itemstack);
        }

        if (this.interactableTradeSlots.getStackInSlot(0).isEmpty()) {
            ItemStack give = this.savedTradeSlots.getStackInSlot(0);
            this.moveFromInventoryToPaymentSlot(give);
        }
    }

    private void moveFromInventoryToPaymentSlot(ItemStack give) {
        if (!give.isEmpty()) {
            for (int i = Constants.PLAYERSTARTSLOT; i < Constants.PLAYERENDSLOT; ++i) {
                ItemStack stackInSlot = this.slots.get(i).getItem();
                if (!stackInSlot.isEmpty() && ItemStack.isSame(give, stackInSlot)) {
                    ItemStack paymentSlot = this.interactableTradeSlots.getStackInSlot(0);
                    int j = paymentSlot.isEmpty() ? 0 : paymentSlot.getCount();
                    int k = Math.min(give.getMaxStackSize() - j, stackInSlot.getCount());
                    ItemStack copy = stackInSlot.copy();
                    int l = j + k;
                    stackInSlot.shrink(k);
                    copy.setCount(l);
                    this.interactableTradeSlots.setStackInSlot(0, copy);
                    if (l >= give.getMaxStackSize()) {
                        break;
                    }
                }
            }
            updateSellItem();
        }
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
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        if (slot.index == Constants.OUTSLOT || slot.index == Constants.MONEYSLOT)
            return false;
        else
            return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void removed(Player pPlayer) {
        this.returnItemToInventory(pPlayer, 0);
        updateSellItem();

        super.removed(pPlayer);

        if (volatileLevel != null && volatilePos != null)
            if (volatileLevel.getBlockEntity(volatilePos) instanceof CrateBE crate) //includes null check
            {
                volatileLevel.sendBlockUpdated(volatilePos, volatileLevel.getBlockState(volatilePos), volatileLevel.getBlockState(volatilePos), 3);
                crate.setChanged();
            }
    }

    private void returnItemToInventory(Player player, int index) {
        player.getInventory().placeItemBackInInventory(this.interactableTradeSlots.getStackInSlot(index));
        this.interactableTradeSlots.setStackInSlot(index, ItemStack.EMPTY);
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    protected void updateSellItem() {
        ItemStack payment = interactableTradeSlots.getStackInSlot(0);
        ItemStack toPay = savedTradeSlots.getStackInSlot(0);
        if (!payment.isEmpty()) {
            if (!toPay.isEmpty() && ItemStack.isSame(toPay, payment) && payment.getCount() >= toPay.getCount() && interactableTradeSlots.getStackInSlot(1).isEmpty()) {
                //move allowed amount of stock to out slot
                movefromStockToSaleSlot(savedTradeSlots.getStackInSlot(1).copy()); //check for the item to get
            }
        } else if (!interactableTradeSlots.getStackInSlot(1).isEmpty()) {
            //move out slot back to crate stock
            moveItemStackTo(interactableTradeSlots.getStackInSlot(1), Constants.CRATESTARTSLOT, Constants.CRATEENDSLOT, false);
        }
    }

    protected ItemStack pickUpPayment() {
        ItemStack original = crateStock.getStackInSlot(Constants.TOTALCRATESTOCKLOTS); //do not modify originals !
        int amount = original.hasTag() ? original.getTag().contains(Constants.TAGSTOCK) ? original.getTag().getInt(Constants.TAGSTOCK) : 0 : 0;

        if (amount > 0 && original.hasTag()) { //Redundant double check, but better safe then sorry
            ItemStack prepPickup = original.copy();
            CompoundTag tag = prepPickup.getTag();
            tag.remove(Constants.TAGSTOCK);
            if (tag.isEmpty())
                prepPickup.setTag(null);
            int pickUp = Math.min(amount, prepPickup.getMaxStackSize());
            prepPickup.setCount(pickUp);

            int updatedAmount = amount - pickUp;
            if (updatedAmount <= 0) {
                crateStock.setStackInSlot(Constants.TOTALCRATESTOCKLOTS, ItemStack.EMPTY);
            } else {
                ItemStack prepUpdate = original.copy();
                prepUpdate.getTag().putInt(Constants.TAGSTOCK, updatedAmount);
                crateStock.setStackInSlot(Constants.TOTALCRATESTOCKLOTS, prepUpdate);
            }
            return prepPickup;
        }
        return ItemStack.EMPTY;
    }

    private void movefromStockToSaleSlot(ItemStack get) {
        if (!get.isEmpty() && (!outOfStock() || isUnlimitedShop)) {
            if (isUnlimitedShop)
                this.interactableTradeSlots.setStackInSlot(1, get.copy());
            else
                for (int i = Constants.CRATESTARTSLOT; i < Constants.CRATEENDSLOT; ++i) {
                    ItemStack stackinSlot = this.slots.get(i).getItem();
                    if (!stackinSlot.isEmpty() && ItemStack.isSame(get, stackinSlot)) {
                        ItemStack pickupSlot = this.interactableTradeSlots.getStackInSlot(1);
                        int j = pickupSlot.isEmpty() ? 0 : pickupSlot.getCount();
                        int k = Math.min(get.getCount() - j, stackinSlot.getCount());
                        ItemStack copy = stackinSlot.copy();
                        int l = j + k;
                        stackinSlot.shrink(k);
                        copy.setCount(l);
                        this.interactableTradeSlots.setStackInSlot(1, copy);
                        if (l >= get.getCount()) {
                            break;
                        }
                    }
                }
        }
    }

    public boolean outOfStock() {
        int total = 0;
        var buying = savedTradeSlots.getStackInSlot(1);
        for (int i = 0; i < Constants.TOTALCRATESTOCKLOTS - 1; i++) //last slot is for payment
        {
            var stack = crateStock.getStackInSlot(i);
            if (!stack.isEmpty() && ItemStack.isSame(stack, buying))
                total += stack.getCount();
        }
        return total < buying.getCount();
    }
}
