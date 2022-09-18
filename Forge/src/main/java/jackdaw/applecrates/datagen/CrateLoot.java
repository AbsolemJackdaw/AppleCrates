package jackdaw.applecrates.datagen;

import com.mojang.datafixers.util.Pair;
import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CrateLoot extends LootTableProvider {

    private final String modid;

    public CrateLoot(String modid, DataGenerator pGenerator) {
        super(pGenerator);
        this.modid = modid;
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return Collections.singletonList(Pair.of(CrateLootTable::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) { /*NOOP*/ }

    private class CrateLootTable extends BlockLoot {

        @Override
        protected void addTables() {
            CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).map(CrateWoodType::getBlock).forEach(this::dropSelf);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).map(CrateWoodType::getBlock).collect(Collectors.toList());
        }
    }
}
