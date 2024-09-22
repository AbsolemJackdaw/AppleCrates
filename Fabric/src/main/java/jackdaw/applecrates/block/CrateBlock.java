package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenuBuyer;
import jackdaw.applecrates.container.CrateMenuFactory;
import jackdaw.applecrates.container.CrateMenuOwner;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CrateBlock extends CommonCrateBlock {

    public CrateBlock(CrateWoodType type) {
        super(type);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
        if (commonCrate instanceof CrateBE crate)
            serverPlayer.openMenu(
                    new CrateMenuFactory((i, inventory, player) -> new CrateMenuBuyer(i, inventory, crate, crate.isUnlimitedShop),
                            Component.translatable("container.crate"),
                            buf -> buf.writeBoolean(crate.isUnlimitedShop))
            );
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
        if (commonCrate instanceof CrateBE crate)
            serverPlayer.openMenu(
                    new CrateMenuFactory((i, inventory, player) -> new CrateMenuOwner(i, inventory, crate, crate.isUnlimitedShop),
                            Component.translatable("container.crate.owner"),
                            buf -> buf.writeBoolean(crate.isUnlimitedShop))
            );
    }
}