package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import jackdaw.applecrates.container.CrateMenuBuyerService;
import jackdaw.applecrates.container.CrateMenuOwnerService;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.block.Block;

public class CrateBlock extends CrateBlockBase {

    public CrateBlock(CrateWoodType type, ResourceKey<Block> id) {
        super(type, id);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(new SimpleMenuProvider((id, inv, interactingPlayer) ->
                new CrateMenuBuyerService(id, inv, crate, crate.isUnlimitedShop), commonCrate.getDisplayName()), buf -> {
            //buffer to read client side
            buf.writeBoolean(crate.isUnlimitedShop);
        });
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        serverPlayer.openMenu(new SimpleMenuProvider((id, inv, interactingPlayer) ->
                new CrateMenuOwnerService(id, inv, crate, crate.isUnlimitedShop), commonCrate.getDisplayName()), buf -> {
            //buffer to read client side
            buf.writeBoolean(crate.isUnlimitedShop);
        });
    }
}
