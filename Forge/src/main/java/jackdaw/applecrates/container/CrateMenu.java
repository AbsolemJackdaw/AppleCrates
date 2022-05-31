package jackdaw.applecrates.container;

import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CrateMenu extends AbstractContainerMenu {

    public boolean isOwner = false;
    public Optional<CrateBE> blockEntity;

    public ItemStackHandler crateSales = new ItemStackHandler(3);

    //Registry overload for menu type registry
    public CrateMenu(int id, Inventory inv, boolean owner) {
        this(owner ? GeneralRegistry.CRATE_MENU_OWNER.get() : GeneralRegistry.CRATE_MENU_BUYER.get(), id, inv, owner);
    }

    public CrateMenu(MenuType<CrateMenu> type, int id, Inventory inventory, boolean isOwner) {
        super(type, id);

        this.addSlot(new SlotItemHandler(crateSales, 0, 136, 37));
        this.addSlot(new SlotItemHandler(crateSales, 1, 162, 37));
        this.addSlot(new SlotItemHandler(crateSales, 2, 220, 38) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
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
    public boolean stillValid(Player player) {
        return true;
    }
}
