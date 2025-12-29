package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import jackdaw.applecrates.container.CrateMenuBuyerService;
import jackdaw.applecrates.container.CrateMenuOwnerService;
import jackdaw.applecrates.container.factory.CrateMenuFactory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;

public class CrateBlock extends CrateBlockBase {

    public CrateBlock(CrateWoodType type, ResourceKey<Block> id) {
        super(type, id);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(
                new CrateMenuFactory((i, inventory, player) -> new CrateMenuBuyerService(i, inventory, crate, crate.isUnlimitedShop),
                        Component.translatable("container.crate"),
                        (crate.isUnlimitedShop))
        );
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(
                new CrateMenuFactory((i, inventory, player) -> new CrateMenuOwnerService(i, inventory, crate, crate.isUnlimitedShop),
                        Component.translatable("container.crate.owner"),
                        (crate.isUnlimitedShop))
        );
    }
}