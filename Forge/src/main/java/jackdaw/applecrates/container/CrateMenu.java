package jackdaw.applecrates.container;

import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import static jackdaw.applecrates.container.CrateStackHandler.TAGSTOCK;

public class CrateMenu extends AbstractContainerMenu {

    public boolean isOwner = false;

    public CrateStackHandler crateStock;
    public ItemStackHandler interactableSlots;
    public ItemStackHandler priceAndSaleSlots;
    public boolean isUnlimitedShop = false;
    private Level volatileLevel;
    private BlockPos volatilePos;

    //Registry overload for menu type registry
//    public CrateMenu(int id, Inventory inv, boolean isOwner, boolean isUnlimitedShop) {
//        this(isOwner ? GeneralRegistry.CRATE_MENU_OWNER.get() : GeneralRegistry.CRATE_MENU_BUYER.get(), id, inv, new ItemStackHandler(2), new ItemStackHandler(2), new CrateStackHandler(), isOwner);
//        this.isUnlimitedShop = isUnlimitedShop;
//    }
//
//    public CrateMenu(MenuType<CrateMenu> type, int id, Inventory inventory, CrateBE crate, boolean isOwner) {
//        this(isOwner ? crate.isUnlimitedShop ? GeneralRegistry.CRATE_MENU_OWNER_UNLIMITED.get() : GeneralRegistry.CRATE_MENU_OWNER.get() :
//                        crate.isUnlimitedShop ? GeneralRegistry.CRATE_MENU_BUYER_UNLIMITED.get() : GeneralRegistry.CRATE_MENU_BUYER.get(),
//                id, inventory, crate.interactable, crate.priceAndSale, crate.crateStock, isOwner);
//        volatileLevel = crate.getLevel();
//        volatilePos = crate.getBlockPos();
//        isUnlimitedShop = crate.isUnlimitedShop;
//    }

    //Registry overload for menu type registry //client
    public CrateMenu(int id, Inventory inventory, boolean owner, boolean unlimited) {
        this(id, inventory, new ItemStackHandler(2), new ItemStackHandler(2), new CrateStackHandler(), owner, unlimited);
    }

    public CrateMenu(int id, Inventory inventory, CrateBE crate, boolean owner, boolean unlimited) {
        this(id, inventory, crate.interactable, crate.priceAndSale, crate.crateStock, owner, unlimited);
        volatileLevel = crate.getLevel();
        volatilePos = crate.getBlockPos();
    }

    public CrateMenu(int id, Inventory inventory, ItemStackHandler interaction, ItemStackHandler trade, CrateStackHandler stock, boolean owner, boolean ul) {
        super(GeneralRegistry.CRATE_MENU.get(), id);

        this.interactableSlots = interaction;
        this.priceAndSaleSlots = trade;
        this.crateStock = stock;

        this.isOwner = owner;
        this.isUnlimitedShop = ul;

        this.addSlot(new SlotItemHandler(interactableSlots, 0, 136, 37));
        this.addSlot(new SlotItemHandler(interactableSlots, 1, 220, 38) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isOwner;
            }
        });

        this.addSlot(new SlotPriceSale(priceAndSaleSlots, 0, 10, 140));
        this.addSlot(new SlotPriceSale(priceAndSaleSlots, 1, 74, 140));

        for (int y = 0; y < 6; y++) //crate stock
            for (int x = 0; x < 5; x++)
                addSlot(new SlotCrateStock(stock, y * 5 + x, x * 18 + 5, y * 18 + 18, isOwner));

        for (int i = 0; i < 3; ++i) { //player inventory
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) { //player hotbar
            this.addSlot(new Slot(inventory, i, 108 + i * 18, 142));
        }
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        if (slotID == 0 && !isOwner) {
            super.clicked(slotID, mouseButton, click, player);
            updateSellItem();
        } else if (slotID == 1 && !isOwner) {
            ItemStack resultSlotCache = interactableSlots.getStackInSlot(1).copy();//get copy before clicking
            super.clicked(slotID, mouseButton, click, player);
            //if the pickup slot had content, then there was payment enough
            if (ClickType.PICKUP.equals(click) && !resultSlotCache.isEmpty() && interactableSlots.getStackInSlot(1).isEmpty()) {
                interactableSlots.getStackInSlot(0).shrink(priceAndSaleSlots.getStackInSlot(0).getCount());
                crateStock.updateStackInPaymentSlot(priceAndSaleSlots.getStackInSlot(0), isUnlimitedShop);
                updateSellItem();
            }
        } else if (slotID == 33 && this.getCarried().isEmpty()) {
            if (click.equals(ClickType.PICKUP)) {
                this.setCarried(pickUpPayment());
            } else if (click.equals(ClickType.QUICK_MOVE)) {
                //custom quickmove code because of locking down the payment slot so noone takes out anything ever
                this.moveItemStackTo(pickUpPayment(), 34, 70, false);
            }
        } else {
            super.clicked(slotID, mouseButton, click, player);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        if (pSlot.index == 1)
            return false;
        return super.canTakeItemForPickAll(pStack, pSlot);
    }

    private ItemStack pickUpPayment() {
        ItemStack original = crateStock.getStackInSlot(29); //do not modify originals !
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
                crateStock.setStackInSlot(29, ItemStack.EMPTY);
            } else {
                ItemStack prepUpdate = original.copy();
                prepUpdate.getTag().putInt(TAGSTOCK, updatedAmount);
                crateStock.setStackInSlot(29, prepUpdate);
            }
            return prepPickup;
        }
        return ItemStack.EMPTY;
    }

    private void updateSellItem() {
        ItemStack paying = interactableSlots.getStackInSlot(0);
        ItemStack give = priceAndSaleSlots.getStackInSlot(0);
        if (!paying.isEmpty()) {
            if (!give.isEmpty()) {
                if (ItemStack.isSameItemSameTags(give, paying) && paying.getCount() >= give.getCount()) {
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

    @Override
    public void removed(Player pPlayer) {

        pPlayer.getInventory().placeItemBackInInventory(this.interactableSlots.getStackInSlot(0));
        if (!isOwner)
            updateSellItem();
        else
            pPlayer.getInventory().placeItemBackInInventory(this.interactableSlots.getStackInSlot(1));

        super.removed(pPlayer);

        if (volatileLevel != null && volatilePos != null)
            if (volatileLevel.getBlockEntity(volatilePos) instanceof CrateBE crate) //includes null check
            {
                volatileLevel.sendBlockUpdated(volatilePos, volatileLevel.getBlockState(volatilePos), volatileLevel.getBlockState(volatilePos), 3);
                crate.setChanged();
            }
    }

    private void movefromStockToSaleSlot(ItemStack get) {
        if (!get.isEmpty() && (!outOfStock() || isUnlimitedShop)) {
            if (isUnlimitedShop)
                this.interactableSlots.setStackInSlot(1, get.copy());
            else
                for (int i = 4; i < 34; ++i) {
                    ItemStack stackinSlot = this.slots.get(i).getItem();
                    if (!stackinSlot.isEmpty() && ItemStack.isSameItemSameTags(get, stackinSlot)) {
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

    private void moveFromInventoryToPaymentSlot(ItemStack give) {
        if (!give.isEmpty()) {
            for (int i = 34; i < (34 + (9 * 4)); ++i) {
                ItemStack stackinSlot = this.slots.get(i).getItem();
                if (!stackinSlot.isEmpty() && ItemStack.isSameItemSameTags(give, stackinSlot)) {
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

    public boolean outOfStock() {
        for (int i = 0; i < crateStock.getSlots() - 1; i++) //last slot is for payment
            if (!crateStock.getStackInSlot(i).isEmpty() && crateStock.getStackInSlot(i).getCount() >= priceAndSaleSlots.getStackInSlot(1).getCount())
                return false;
        return interactableSlots.getStackInSlot(1).isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        if (slots.get(pIndex).hasItem()) {
            //slot 33 is money slot
            if (pIndex >= 4 && pIndex <= 32 || pIndex < 2)
                if (this.moveItemStackTo(slots.get(pIndex).getItem(), 34, 70, false)) { //player inv
                    if (pIndex == 1) {
                        ItemStack prepPayChange = interactableSlots.getStackInSlot(0).copy();
                        prepPayChange.shrink(priceAndSaleSlots.getStackInSlot(0).getCount());
                        interactableSlots.setStackInSlot(0, prepPayChange);
                        crateStock.updateStackInPaymentSlot(priceAndSaleSlots.getStackInSlot(0), isUnlimitedShop);
                        if (!interactableSlots.getStackInSlot(0).isEmpty()) {
                            updateSellItem();
                            return priceAndSaleSlots.getStackInSlot(1).copy(); //keep looping till pay is empty
                        }
                    }
                    if (pIndex == 0)
                        updateSellItem();
                } else return ItemStack.EMPTY;

            if (isOwner) {
                if (pIndex >= 34 && pIndex <= 70) { //only enable move to crate if player is the owner
                    if (!this.moveItemStackTo(slots.get(pIndex).getItem(), 4, 33, false)) { //Crate slot
                        return ItemStack.EMPTY;
                    }
                }
                if (pIndex == 33) {
                    //code is never reached.
                    //the payement slot (or nr 33) is void form pickup and cannot trigger quickMoveStack.
                    //custom quickmove code can be found in #clicked
                }
            } else {
                if (pIndex >= 34 && pIndex <= 70) { //move to 'give' slot if not owner
                    if (!this.moveItemStackTo(slots.get(pIndex).getItem(), 0, 1, false)) { //
                        return ItemStack.EMPTY;
                    }
                    updateSellItem();
                }
            }

        }

        return ItemStack.EMPTY;
    }
}
