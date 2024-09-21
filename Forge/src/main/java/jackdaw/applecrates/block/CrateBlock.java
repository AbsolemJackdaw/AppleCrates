package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenuBuyer;
import jackdaw.applecrates.container.CrateMenuOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class CrateBlock extends CommonCrateBlock {

    public CrateBlock(CrateWoodType type) {
        super(type);
    }

    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
        if (commonCrate instanceof CrateBE crate)
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                    new CrateMenuBuyer(id, inv, crate, crate.isUnlimitedShop), Component.translatable("container.crate")), buf -> {
                //buffer to read client side
                buf.writeBoolean(crate.isUnlimitedShop);
            });
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
        if (commonCrate instanceof CrateBE crate)
            NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                    new CrateMenuOwner(id, inv, crate, crate.isUnlimitedShop), Component.translatable("container.crate.owner")), buf -> {
                //buffer to read client side
                buf.writeBoolean(crate.isUnlimitedShop);
            });
    }
}
