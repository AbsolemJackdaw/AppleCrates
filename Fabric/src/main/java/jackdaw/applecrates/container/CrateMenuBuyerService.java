package jackdaw.applecrates.container;

import jackdaw.applecrates.FabricCrates;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenuBuyerService extends CrateMenuBuyer {
    public CrateMenuBuyerService(int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_BUYER, id, inventory, adapter, unlimited);
    }

    public CrateMenuBuyerService(int id, Inventory inventory, CommonCrateBE crate, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_BUYER, id, inventory, crate, unlimited);
    }
}
