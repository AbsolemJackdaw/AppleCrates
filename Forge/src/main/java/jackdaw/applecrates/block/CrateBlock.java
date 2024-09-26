package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.CrateMenuBuyerService;
import jackdaw.applecrates.container.CrateMenuOwnerService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class CrateBlock extends CrateBlockBase {

    public CrateBlock(CrateWoodType type) {
        super(type);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                new CrateMenuBuyerService(id, inv, crate, crate.isUnlimitedShop), Component.translatable("container.crate")), buf -> {
            //buffer to read client side
            buf.writeBoolean(crate.isUnlimitedShop);
        });
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
        if (!(commonCrate instanceof CrateBlockEntity crate)) return;
        NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                new CrateMenuOwnerService(id, inv, crate), Component.translatable("container.crate.owner")), buf -> {
            //buffer to read client side
            buf.writeBoolean(crate.isUnlimitedShop);
        });
    }
}
