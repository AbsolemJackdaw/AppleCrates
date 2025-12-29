package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.container.IStackHandlerAdapter;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CrateBlockEntityBase extends BlockEntity {

    public final IStackHandlerAdapter stackHandler;
    public boolean isUnlimitedShop = false;
    private Set<UUID> owners = new HashSet<>();

    public CrateBlockEntityBase(CrateWoodType type, BlockPos pos, BlockState state, IStackHandlerAdapter stackHandler) {
        super(CrateWoodType.getBlockEntityType(type), pos, state);
        this.stackHandler = stackHandler;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        loadCrateDataFromTag(input);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        saveCrateDataToTag(output);
    }

    /**
     * sync on data change
     */
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);// just defers to getUpdateTag
    }

    /**
     * sync on login : getUpdateTag / handleUpdateTag
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        TagValueOutput valueOutput = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        saveCrateDataToTag(valueOutput);
        return valueOutput.buildResult();
    }

    protected void saveCrateDataToTag(ValueOutput output) {
        stackHandler.saveInventoryData(output);
        output.putBoolean(Constants.TAGUNLIMITED, isUnlimitedShop);
        if (!owners.isEmpty()) {
            var list = output.list(Constants.TAGOWNER, UUIDUtil.CODEC);
            owners.forEach(list::add);
        }
    }

    protected void loadCrateDataFromTag(ValueInput input) {
        stackHandler.loadInventoryData(input);
        isUnlimitedShop = input.getBooleanOr(Constants.TAGUNLIMITED, false);
        var owner = input.listOrEmpty(Constants.TAGOWNER, UUIDUtil.CODEC);
        owners = owner.stream().collect(Collectors.toCollection(HashSet::new));
    }


    public Set<UUID> getOwners() {
        return this.owners;
    }

    public void addOwner(UUID player) {
        this.owners.add(player);
    }

    public boolean isOwner(UUID uuid) {
        return owners.isEmpty() || owners.contains(uuid);
    }

    //defaults to true without owner to prevent unbreakable blocks, even though the owner should always be set
    public boolean isOwner(Player player) {
        return owners.isEmpty() || player != null && owners.contains(player.getGameProfile().id());
    }

    public static int getStockSignal(BlockGetter blockLevel, BlockPos pos) {
        if (blockLevel.getBlockState(pos).hasBlockEntity() && blockLevel.getBlockEntity(pos) instanceof CrateBlockEntityBase crate)
            return crate.getStockLevel();
        return 0;
    }

    public int getStockLevel() {
        var outputStack = stackHandler.getSavedTradeSlotsItem(1);
        if (!outputStack.isEmpty()) {
            double count = stackHandler.getCrateStock().getCountOfItemImmediately(outputStack.getItem());
            double totalPossible = outputStack.getMaxStackSize() * Constants.TOTALCRATESTOCKLOTS;
            var ratio = 15 * (count / totalPossible);
            return (int) ratio;
        }
        return 0;
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (this.getLevel().getBlockEntity(pos) instanceof CrateBlockEntityBase crate && getLevel() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < Constants.TOTALCRATESLOTS; i++) {
                ItemStack stack = crate.stackHandler.getCrateStockItem(i);
                if (i == Constants.TOTALCRATESTOCKLOTS) {
                    if (!stack.isEmpty() && stack.getOrDefault(Content.coinCounter, new CoinCounter(0)).count() > 0) {
                        int pay = stack.get(Content.coinCounter).count();
                        ItemStack prepCopy = stack.copy();
                        prepCopy.remove(Content.coinCounter);

                        while (pay > 0) {
                            ItemStack toDrop = prepCopy.copy();
                            if (pay >= prepCopy.getMaxStackSize()) {
                                toDrop.setCount(prepCopy.getMaxStackSize());
                                pay -= prepCopy.getMaxStackSize();
                            } else {
                                toDrop.setCount(pay);
                                pay = 0; //set to 0. we could count down the last items from the counter, but it's the same
                            }
                            Containers.dropItemStack(serverLevel, pos.getX(), pos.getY(), pos.getZ(), toDrop);
                        }
                    }
                } else if (!stack.isEmpty()) {
                    Containers.dropItemStack(serverLevel, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
            }
            for (int i = 0; i < 2; i++) {
                ItemStack toDrop = crate.stackHandler.getInteractableTradeItem(i);
                Containers.dropItemStack(serverLevel, pos.getX(), pos.getY(), pos.getZ(), toDrop);
            }
            getLevel().updateNeighbourForOutputSignal(pos, state.getBlock());
        }
    }
}
