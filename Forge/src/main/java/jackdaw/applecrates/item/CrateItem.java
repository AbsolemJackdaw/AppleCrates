package jackdaw.applecrates.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;

public class CrateItem extends BlockItem {
    public CrateItem(Block block) {
        super(block, new Properties().tab(CreativeModeTab.TAB_DECORATIONS).stacksTo(16));
    }
}
