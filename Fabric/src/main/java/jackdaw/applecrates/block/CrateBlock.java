package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.CrateMenuBuyerService;
import jackdaw.applecrates.container.CrateMenuOwnerService;
import jackdaw.applecrates.container.factory.CrateMenuFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CrateBlock extends CrateBlockBase {

    public CrateBlock(CrateWoodType type) {
        super(type);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(
                new CrateMenuFactory((i, inventory, player) -> new CrateMenuBuyerService(i, inventory, crate, crate.isUnlimitedShop),
                        Component.translatable("container.crate"),
                        buf -> buf.writeBoolean(crate.isUnlimitedShop))
        );
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(
                new CrateMenuFactory((i, inventory, player) -> new CrateMenuOwnerService(i, inventory, crate, crate.isUnlimitedShop),
                        Component.translatable("container.crate.owner"),
                        buf -> buf.writeBoolean(crate.isUnlimitedShop))
        );
    }
}