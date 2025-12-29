package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CrateBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrateBlockBase extends Block implements EntityBlock {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    private final CrateWoodType type;

    public CrateBlockBase(CrateWoodType type, ResourceKey<Block> key) {
        super(Properties.ofFullCopy(Blocks.OAK_PLANKS).setId(key).noOcclusion().isValidSpawn(CrateBlockBase::never).isRedstoneConductor(CrateBlockBase::never).isSuffocating(CrateBlockBase::never).isViewBlocking(CrateBlockBase::never));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.type = type;
    }

    private static Boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> type) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection());
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (pPlacer instanceof ServerPlayer serverPlayer && pLevel.getBlockEntity(pPos) instanceof CrateBlockEntityBase crate) {
            crate.addOwner(serverPlayer.getUUID());
        }
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockLevel, BlockPos pos, Direction dir) {
        return Mth.clamp(CrateBlockEntityBase.getStockSignal(blockLevel, pos), 0, 15);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return state.getSignal(level, pos, state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDef) {
        stateDef.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CrateWoodType.getBlockEntityType(type).create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    //only owner can break
    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof CrateBlockEntityBase crate && crate.isOwner(pPlayer))
            return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
        return 0;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CrateBlockEntityBase crate && hand.equals(InteractionHand.MAIN_HAND)) {
            if (level instanceof ServerLevel server && player.getItemInHand(hand).getItem() instanceof DebugStickItem && server.getServer().getPlayerList().isOp(player.nameAndId())) {
                crate.isUnlimitedShop = true;
                player.displayClientMessage(Component.translatable("crate.set.creative"), true);
                crate.setChanged();
            } else {
                boolean owner = !player.isShiftKeyDown() && crate.isOwner(player); //add shift debug testing
                if (player instanceof ServerPlayer serverPlayer) {
                    if (owner)
                        openOwnerUI(serverPlayer, crate);
                    else
                        openBuyerUI(serverPlayer, crate);
                }
                level.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.FAIL;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    public void openOwnerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
    }

    public void openBuyerUI(ServerPlayer serverPlayer, CrateBlockEntityBase commonCrate) {
    }
}
