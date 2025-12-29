package jackdaw.applecrates.item;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlockBase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CrateItem extends BlockItem {
    public CrateItem(Block block, ResourceKey<Item> key) {
        super(block, new Properties().stacksTo(16).setId(key));
    }
}
