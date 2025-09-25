package jackdaw.applecrates.container;

import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.world.entity.player.Inventory;

public class CrateMenuOwnerService extends CrateMenuOwner {
    //client overload
    public CrateMenuOwnerService(int id, Inventory inventory, boolean unlimited) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, new StackHandlerAdapter(), unlimited);
    }

    public CrateMenuOwnerService(int id, Inventory inventory, CrateBlockEntityBase crate) {
        super(GeneralRegistry.CRATE_MENU_OWNER.get(), id, inventory, crate, crate.isUnlimitedShop);
    }
}
