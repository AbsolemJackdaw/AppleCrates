package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
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
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof CrateBE crate && pHand.equals(InteractionHand.MAIN_HAND)) {
            if (pLevel instanceof ServerLevel server && pPlayer.getItemInHand(pHand).getItem() instanceof DebugStickItem && server.getServer().getPlayerList().isOp(pPlayer.getGameProfile())) {
                crate.isUnlimitedShop = true;
                pPlayer.displayClientMessage(Component.translatable("crate.set.creative"), true);
                crate.setChanged();
            } else {
                boolean owner = !pPlayer.isShiftKeyDown() && crate.isOwner(pPlayer); //add shift debug testing

                if (pPlayer instanceof ServerPlayer sp) {
                    NetworkHooks.openScreen(sp, new SimpleMenuProvider((id, inv, player) ->
                            new CrateMenu(id, inv, crate, owner, crate.isUnlimitedShop), Component.translatable("container.crate" + (owner ? ".owner" : ""))), buf -> {
                        //buffer to read client side
                        buf.writeBoolean(owner);
                        buf.writeBoolean(crate.isUnlimitedShop);

                    });
                }
                pLevel.playSound(pPlayer, pPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return InteractionResult.FAIL;
    }
}
