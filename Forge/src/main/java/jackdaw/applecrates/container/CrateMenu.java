package jackdaw.applecrates.container;

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
    public ItemStackHandler interactableSlots;
    public ItemStackHandler priceAndSaleSlots;
    public boolean isUnlimitedShop = false;
    private Level volatileLevel;
    private BlockPos volatilePos;

    protected CrateMenu(MenuType type, int id, Inventory inventory, boolean unlimited) {
        this(type, id, inventory, new ItemStackHandler(2), new ItemStackHandler(2), new CrateStackHandler(), unlimited);
    }

    protected CrateMenu(MenuType type, int id, Inventory inventory, ItemStackHandler interaction, ItemStackHandler trade, CrateStackHandler stock, boolean ul) {
        super(type, id);
        this.interactableSlots = interaction;
        this.priceAndSaleSlots = trade;
        this.crateStock = stock;
        this.isUnlimitedShop = ul;

        this.addSlot(new SlotItemHandler(interactableSlots, 0, isOwner() ? 10 : 102, isOwner() ? 76 : 21)); //owner set pay
        this.addSlot(new SlotItemHandler(interactableSlots, 1, isOwner() ? 46 : 142, isOwner() ? 76 : 21) { //owner set item
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isOwner();
            }
        });

        //invisible slots, storage for current trade
        this.addSlot(new SlotPriceSale(priceAndSaleSlots, 0, 0, 0)); //buyer pays
        this.addSlot(new SlotPriceSale(priceAndSaleSlots, 1, 0, 0)); //buyer gets

        for (int y = 0; y < 3; y++) //crate stock
            for (int x = 0; x < 10; x++)
                addSlot(new SlotCrateStock(stock, y * 10 + x, x * 18 + 10, y * 18 + 17, isOwner()));
        addSlot(new SlotCrateStock(stock, crateStock.getLastSlot(), 172, 76, isOwner()));

        for (int i = 0; i < 3; ++i) { //player inventory
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, playerInventoryX() + j * 18, playerInventoryY() + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) { //player hotbar
            this.addSlot(new Slot(inventory, i, playerInventoryX() + i * 18, playerInventoryY() + 58));
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
        ItemStack itemstack = this.interactableSlots.getStackInSlot(0);
        if (!itemstack.isEmpty()) {
            if (!this.moveItemStackTo(itemstack, 34, 34 + (9 * 3), false)) {
                return;
            }
            this.interactableSlots.setStackInSlot(0, itemstack);
        }

        if (this.interactableSlots.getStackInSlot(0).isEmpty()) {
            ItemStack give = this.priceAndSaleSlots.getStackInSlot(0);
            this.moveFromInventoryToPaymentSlot(give);
        }
    }

    private void moveFromInventoryToPaymentSlot(ItemStack give) {
        if (!give.isEmpty()) {
            for (int i = 34; i < (34 + (9 * 4)); ++i) {
                ItemStack stackinSlot = this.slots.get(i).getItem();
                if (!stackinSlot.isEmpty() && ItemStack.isSame(give, stackinSlot)) {
                    ItemStack paymentSlot = this.interactableSlots.getStackInSlot(0);
                    int j = paymentSlot.isEmpty() ? 0 : paymentSlot.getCount();
                    int k = Math.min(give.getMaxStackSize() - j, stackinSlot.getCount());
                    ItemStack copy = stackinSlot.copy();
                    int l = j + k;
                    stackinSlot.shrink(k);
                    copy.setCount(l);
                    this.interactableSlots.setStackInSlot(0, copy);
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

        //slot 34 is money slot
        if (index >= 4 && index <= 34 || index < 2)
            if (this.moveItemStackTo(slots.get(index).getItem(), 35, 70, false)) { //player inv
                if (index == 1) {
                    ItemStack prepPayChange = interactableSlots.getStackInSlot(0).copy();
                    prepPayChange.shrink(priceAndSaleSlots.getStackInSlot(0).getCount());
                    interactableSlots.setStackInSlot(0, prepPayChange);
                    crateStock.updateStackInPaymentSlot(priceAndSaleSlots.getStackInSlot(0), isUnlimitedShop);
                    if (!interactableSlots.getStackInSlot(0).isEmpty()) {
                        updateSellItem();
                        return priceAndSaleSlots.getStackInSlot(1).copy(); //keep looping till pay is empty
                    }
                }
                if (index == 0)
                    updateSellItem();
            } else return ItemStack.EMPTY;
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        if (pSlot.index == 1)
            return false;
        return super.canTakeItemForPickAll(pStack, pSlot);
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
        player.getInventory().placeItemBackInInventory(this.interactableSlots.getStackInSlot(index));
        this.interactableSlots.setStackInSlot(index, ItemStack.EMPTY);
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    protected void updateSellItem() {
        ItemStack payment = interactableSlots.getStackInSlot(0);
        ItemStack toPay = priceAndSaleSlots.getStackInSlot(0);
        if (!payment.isEmpty()) {
            if (!toPay.isEmpty()) {
                if (ItemStack.isSame(toPay, payment) && payment.getCount() >= toPay.getCount()) {
                    if (interactableSlots.getStackInSlot(1).isEmpty()) {
                        movefromStockToSaleSlot(priceAndSaleSlots.getStackInSlot(1).copy()); //check for the item to get
                    }
                }
            }
        } else {
            if (!interactableSlots.getStackInSlot(1).isEmpty()) {
                moveItemStackTo(interactableSlots.getStackInSlot(1), 4, 33, false);
            }
        }
    }

    protected ItemStack pickUpPayment() {
        ItemStack original = crateStock.getStackInSlot(crateStock.getLastSlot()); //do not modify originals !
        int amount = original.hasTag() ? original.getTag().contains(TAGSTOCK) ? original.getTag().getInt(TAGSTOCK) : 0 : 0;

        if (amount > 0 && original.hasTag()) { //Redundant double check, but better safe then sorry
            ItemStack prepPickup = original.copy();
            CompoundTag tag = prepPickup.getTag();
            tag.remove(TAGSTOCK);
            if (tag.isEmpty())
                prepPickup.setTag(null);
            int pickUp = Math.min(amount, prepPickup.getMaxStackSize());
            prepPickup.setCount(pickUp);

            int updatedAmount = amount - pickUp;
            if (updatedAmount <= 0) {
                crateStock.setStackInSlot(crateStock.getLastSlot(), ItemStack.EMPTY);
            } else {
                ItemStack prepUpdate = original.copy();
                prepUpdate.getTag().putInt(TAGSTOCK, updatedAmount);
                crateStock.setStackInSlot(crateStock.getLastSlot(), prepUpdate);
            }
            return prepPickup;
        }
        return ItemStack.EMPTY;
    }

    private void movefromStockToSaleSlot(ItemStack get) {
        if (!get.isEmpty() && (!outOfStock() || isUnlimitedShop)) {
            if (isUnlimitedShop)
                this.interactableSlots.setStackInSlot(1, get.copy());
            else
                for (int i = 4; i < 34; ++i) {
                    ItemStack stackinSlot = this.slots.get(i).getItem();
                    if (!stackinSlot.isEmpty() && ItemStack.isSame(get, stackinSlot)) {
                        ItemStack pickupSlot = this.interactableSlots.getStackInSlot(1);
                        int j = pickupSlot.isEmpty() ? 0 : pickupSlot.getCount();
                        int k = Math.min(get.getCount() - j, stackinSlot.getCount());
                        ItemStack copy = stackinSlot.copy();
                        int l = j + k;
                        stackinSlot.shrink(k);
                        copy.setCount(l);
                        this.interactableSlots.setStackInSlot(1, copy);
                        if (l >= get.getCount()) {
                            break;
                        }
                    }
                }
        }
    }

    public boolean outOfStock() {
        int total = 0;
        var buying = priceAndSaleSlots.getStackInSlot(1);
        for (int i = 0; i < crateStock.getLastSlot() - 1; i++) //last slot is for payment
        {
            var stack = crateStock.getStackInSlot(i);
            if (!stack.isEmpty() && ItemStack.isSame(stack, buying))
                total += stack.getCount();
        }
        return total < buying.getCount();
    }
}
