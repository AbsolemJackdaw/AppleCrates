package jackdaw.applecrates.container;

import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenuBuyerService extends CrateMenuBuyer {
    public CrateMenuBuyerService(int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_BUYER.get(), id, inventory, adapter, unlimited);
    }

    public CrateMenuBuyerService(int id, Inventory inventory, CrateBlockEntityBase crate, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_BUYER.get(), id, inventory, crate, unlimited);
    }
}
