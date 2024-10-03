package jackdaw.applecrates.compat;

import com.matyrobbrt.sectionprotection.api.ActionType;
import com.matyrobbrt.sectionprotection.api.SectionProtectionAPI;
import com.mojang.logging.LogUtils;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;

public class SectionProtection {

    public static void init() {
//        SectionProtectionAPI.INSTANCE.registerPredicate(ActionType.BLOCK_INTERACTION, (player, interactionType, hand, level, pos, state) -> {
//            final var entity = level.getBlockEntity(pos);
//            if (entity instanceof CrateBlockEntity)
//                return ActionType.Result.ALLOW;
//            return ActionType.Result.CONTINUE;
//        });
//        LogUtils.getLogger().debug("Enabled SectionProtection integration: crates can now be used by everyone in claimed chunks.");
    }
}