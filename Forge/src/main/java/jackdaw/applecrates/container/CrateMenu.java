package jackdaw.applecrates.container;

import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CrateMenu extends AbstractContainerMenu {

    public boolean isOwner = false;

    public ItemStackHandler crateStock;
    public ItemStackHandler interactableSlots;
    public ItemStackHandler priceAndSaleSlots;

    //Registry overload for menu type registry
    public CrateMenu(int id, Inventory inv, boolean owner) {
        this(owner ? GeneralRegistry.CRATE_MENU_OWNER.get() : GeneralRegistry.CRATE_MENU_BUYER.get(), id, inv, new ItemStackHandler(3), new ItemStackHandler(2), new ItemStackHandler(30), owner);
    }

    public CrateMenu(MenuType<CrateMenu> type, int id, Inventory inventory, ItemStackHandler interaction, ItemStackHandler trade, ItemStackHandler stock, boolean isOwner) {
        super(type, id);

        this.interactableSlots = interaction;
        this.priceAndSaleSlots = trade;
        this.crateStock = stock;

        this.addSlot(new SlotItemHandler(interactableSlots, 0, 136, 37));
        this.addSlot(new SlotItemHandler(interactableSlots, 1, 162, 37));
        this.addSlot(new SlotItemHandler(interactableSlots, 2, 220, 38) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

        this.addSlot(new SlotItemHandler(priceAndSaleSlots, 0, 10, 140) {
            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return false;
            }
        });
        this.addSlot(new SlotItemHandler(priceAndSaleSlots, 1, 74, 140) {
            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public boolean mayPickup(Player playerIn) {
                return false;
            }
        });

        for (int y = 0; y < 6; y++)
            for (int x = 0; x < 5; x++)
                addSlot(new SlotItemHandler(stock, y * 5 + x, x * 18 + 5, y * 18 + 18) {
                    @Override
                    public boolean isActive() {
                        return isOwner;
                    }
                });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 108 + i * 18, 142));
        }
    }

    @Override
    public void clicked(int slotID, int mouseButton, ClickType click, Player player) {
        ItemStack resultSlotCache = interactableSlots.getStackInSlot(2).copy();//get copy before clicking
        super.clicked(slotID, mouseButton, click, player);
        if (player instanceof ServerPlayer serverPlayer && slotID < 3) {
            if (slotID == 2 && ClickType.PICKUP.equals(click) && !resultSlotCache.isEmpty() && interactableSlots.getStackInSlot(2).isEmpty()) {
                //TODO give payement to owner
                interactableSlots.getStackInSlot(0).shrink(priceAndSaleSlots.getStackInSlot(0).getCount());
            }
        }
        updateSellItem();

    }

    @Override
    public void slotsChanged(Container pInventory) {
        super.slotsChanged(pInventory);
        updateSellItem();
    }

    private void updateSellItem() {
        ItemStack paying = interactableSlots.getStackInSlot(0);
        ItemStack give = priceAndSaleSlots.getStackInSlot(0);
        if (!paying.isEmpty()) {
            if (!give.isEmpty()) {
                if (ItemStack.isSameItemSameTags(give, paying) && paying.getCount() >= give.getCount()) {
                    if (interactableSlots.getStackInSlot(2).isEmpty()) {
                        movefromStockToSaleSlot(priceAndSaleSlots.getStackInSlot(1).copy()); //check for the item to get
                    }
                }
            }
        } else {
            if (!interactableSlots.getStackInSlot(2).isEmpty()) {
                moveItemStackTo(interactableSlots.getStackInSlot(2), 5, 35, true);
            }
        }
    }

    public void tryMovePayementToInteraction() {
        ItemStack itemstack = this.interactableSlots.getStackInSlot(0);
        if (!itemstack.isEmpty()) {
            if (!this.moveItemStackTo(itemstack, 35, 64, true)) {
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
        super.removed(pPlayer);

        if (pPlayer instanceof ServerPlayer) {
            pPlayer.getInventory().placeItemBackInInventory(this.interactableSlots.getStackInSlot(0));
            pPlayer.getInventory().placeItemBackInInventory(this.interactableSlots.getStackInSlot(1));
        }
    }

    private void movefromStockToSaleSlot(ItemStack get) {
        if (!get.isEmpty()) {
            for (int i = 5; i < 34; ++i) {
                ItemStack stackinSlot = this.slots.get(i).getItem();
                if (!stackinSlot.isEmpty() && ItemStack.isSameItemSameTags(get, stackinSlot)) {
                    ItemStack pickupSlot = this.interactableSlots.getStackInSlot(2);
                    int j = pickupSlot.isEmpty() ? 0 : pickupSlot.getCount();
                    int k = Math.min(get.getCount() - j, stackinSlot.getCount());
                    ItemStack copy = stackinSlot.copy();
                    int l = j + k;
                    stackinSlot.shrink(k);
                    copy.setCount(l);
                    this.interactableSlots.setStackInSlot(2, copy);
                    if (l >= get.getCount()) {
                        break;
                    }
                }
            }
        }
    }

    private void moveFromInventoryToPaymentSlot(ItemStack give) {
        if (!give.isEmpty()) {
            for (int i = 35; i < 71; ++i) {
                ItemStack stackinSlot = this.slots.get(i).getItem();
                if (!stackinSlot.isEmpty() && ItemStack.isSameItemSameTags(give, stackinSlot)) {
                    ItemStack payementSlot = this.interactableSlots.getStackInSlot(0);
                    int j = payementSlot.isEmpty() ? 0 : payementSlot.getCount();
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
    public boolean stillValid(Player player) {
        return true;
    }
}
