package jackdaw.applecrates.block;

import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.blockentity.CommonCrateBE;
import net.minecraft.server.level.ServerPlayer;

public class CrateBlock extends CommonCrateBlock {

    public CrateBlock(CrateWoodType type) {
        super(type);
    }


    @Override
    public void openBuyerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
    }

    @Override
    public void openOwnerUI(ServerPlayer serverPlayer, CommonCrateBE commonCrate) {
    }
}
