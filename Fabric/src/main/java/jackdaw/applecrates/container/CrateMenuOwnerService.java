package jackdaw.applecrates.container;

import jackdaw.applecrates.FabricCrates;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenuOwnerService extends CrateMenuOwner {
    public CrateMenuOwnerService(int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_OWNER, id, inventory, adapter, unlimited);
    }

    public CrateMenuOwnerService(int id, Inventory inventory, CommonCrateBE crate, boolean unlimited) {
        super(FabricCrates.CRATE_MENU_OWNER, id, inventory, crate, unlimited);
    }
}
