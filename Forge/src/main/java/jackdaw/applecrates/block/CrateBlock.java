package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CrateBE crate && hand.equals(InteractionHand.MAIN_HAND)) {
            if (level instanceof ServerLevel server && player.getItemInHand(hand).getItem() instanceof DebugStickItem && server.getServer().getPlayerList().isOp(player.getGameProfile())) {
                crate.isUnlimitedShop = true;
                player.displayClientMessage(Component.translatable("crate.set.creative"), true);
                crate.setChanged();
            } else {
                boolean owner = !player.isShiftKeyDown() && crate.isOwner(player); //add shift debug testing

                if (player instanceof ServerPlayer sp) {
                    if (owner)
                        NetworkHooks.openScreen(sp, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                                new CrateMenuOwner(id, inv, crate, crate.isUnlimitedShop), Component.translatable("container.crate.owner")), buf -> {
                            //buffer to read client side
                            buf.writeBoolean(crate.isUnlimitedShop);
                        });
                    else
                        NetworkHooks.openScreen(sp, new SimpleMenuProvider((id, inv, interactingPlayer) ->
                                new CrateMenuBuyer(id, inv, crate, crate.isUnlimitedShop), Component.translatable("container.crate")), buf -> {
                            //buffer to read client side
                            buf.writeBoolean(crate.isUnlimitedShop);
                        });
                }
                level.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.FAIL;
    }
}
