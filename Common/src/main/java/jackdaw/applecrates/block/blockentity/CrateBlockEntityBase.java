package jackdaw.applecrates.block.blockentity;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.container.IStackHandlerAdapter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class CrateBlockEntityBase extends BlockEntity {

    public final IStackHandlerAdapter stackHandler;
    public boolean isUnlimitedShop = false;
    private UUID owner;

    public CrateBlockEntityBase(CrateWoodType type, BlockPos pos, BlockState state, IStackHandlerAdapter stackHandler) {
        super(CrateWoodType.getBlockEntityType(type), pos, state);
        this.stackHandler = stackHandler;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadCrateDataFromTag(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveCrateDataToTag(tag);
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
    public CompoundTag getUpdateTag() {
        return saveCrateDataToTag(new CompoundTag());
    }

    protected CompoundTag saveCrateDataToTag(CompoundTag tag) {
        stackHandler.saveInventoryData(tag);
        tag.putBoolean(Constants.TAGUNLIMITED, isUnlimitedShop);
        if (owner != null)
            tag.putUUID(Constants.TAGOWNER, owner);
        return tag;
    }

    protected void loadCrateDataFromTag(CompoundTag tag) {
        stackHandler.loadInventoryData(tag);
        if (tag.contains(Constants.TAGUNLIMITED))
            isUnlimitedShop = tag.getBoolean(Constants.TAGUNLIMITED);
        if (tag.contains(Constants.TAGOWNER))
            owner = tag.getUUID(Constants.TAGOWNER);
    }


    public UUID getOwner() {
        return owner;
    }

    public void setOwner(ServerPlayer player) {
        this.owner = player.getGameProfile().getId();
    }

    //defaults to true without owner to prevent unbreakable blocks, even though the owner should always be set
    public boolean isOwner(Player player) {
        return owner == null || player != null && owner.equals(player.getGameProfile().getId());
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
}
