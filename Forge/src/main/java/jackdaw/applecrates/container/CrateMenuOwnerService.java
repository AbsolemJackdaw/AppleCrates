package jackdaw.applecrates.container;

import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenuOwnerService extends CrateMenuOwner {
    public CrateMenuOwnerService(int id, Inventory inventory, IStackHandlerAdapter adapter, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, adapter, unlimited);
    }

    public CrateMenuOwnerService(int id, Inventory inventory, CommonCrateBE crate, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, crate, unlimited);
    }
}
