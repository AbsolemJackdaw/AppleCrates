package jackdaw.applecrates.block;

import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    private final WoodType type;

    public CrateBlock(WoodType type) {
        super(Properties.copy(Blocks.OAK_PLANKS).noOcclusion().isValidSpawn(CrateBlock::never).isRedstoneConductor(CrateBlock::never).isSuffocating(CrateBlock::never).isViewBlocking(CrateBlock::never));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.type = type;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    private static Boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> type) {
        return false;
    }


    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDef) {
        stateDef.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return this.defaultBlockState().setValue(FACING, placeContext.getHorizontalDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return GeneralRegistry.BE_MAP.get(type).get().create(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (pPlacer instanceof ServerPlayer player && pLevel.getBlockEntity(pPos) instanceof CrateBE crate) {
            crate.setOwner(player);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer instanceof ServerPlayer sp && pLevel.getBlockEntity(pPos) instanceof CrateBE crate && pHand.equals(InteractionHand.MAIN_HAND)) {
            boolean owner = sp.isShiftKeyDown() && crate.getOwner().equals(sp.getGameProfile().getId());
            NetworkHooks.openGui(sp,
                    new SimpleMenuProvider((pContainerId, pInventory, pPlayer1) ->
                            new CrateMenu(owner ? GeneralRegistry.CRATE_MENU_OWNER.get() : GeneralRegistry.CRATE_MENU_BUYER.get(), pContainerId, pInventory, crate, owner),
                            new TranslatableComponent("container.crate" + (owner ? ".owner" : ""))));
            return InteractionResult.CONSUME;//InteractionResult.sidedSuccess(pLevel.isClientSide);
            //Eating a food item while interacting with the block played the eating animation. I dont know what interaction result is correct
        }
        return InteractionResult.FAIL;//super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof CrateBE crate && pLevel instanceof ServerLevel level) {
                for (int i = 0; i < crate.crateStock.getSlots(); i++) {
                    ItemStack stack = crate.crateStock.getStackInSlot(i);
                    if (i == 29) {
                        if ( !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("stocked")) {
                            int pay = stack.getTag().getInt("stocked");
                            ItemStack prepCopy = stack.copy();
                            prepCopy.removeTagKey("stocked");
                            if (prepCopy.getTag() != null && prepCopy.getTag().isEmpty())
                                prepCopy.setTag(null);

                            while (pay > 0) {
                                ItemStack toDrop = prepCopy.copy();
                                if (pay >= prepCopy.getMaxStackSize()) {
                                    toDrop.setCount(prepCopy.getMaxStackSize());
                                    pay -= prepCopy.getMaxStackSize();
                                } else {
                                    toDrop.setCount(pay);
                                    pay = 0; //set to 0. we could count down the last items from the counter, but it's the same
                                }
                                Containers.dropItemStack(level, pPos.getX(), pPos.getY(), pPos.getZ(), toDrop);
                            }
                        }
                    } else if (!stack.isEmpty()) {
                        Containers.dropItemStack(level, pPos.getX(), pPos.getY(), pPos.getZ(), stack);
                    }
                }
                for (int i = 0; i < 2; i++) {
                    ItemStack toDrop = crate.interactable.getStackInSlot(i);
                    Containers.dropItemStack(level, pPos.getX(), pPos.getY(), pPos.getZ(), toDrop);
                }
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    //only owner can break
    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof CrateBE crate && crate.isOwner(pPlayer))//pPlayer.getGameProfile().getId().equals(crate.getOwner()))
                return super.getDestroyProgress(pState, pPlayer, pLevel, pPos);
        return 0;
    }
}
