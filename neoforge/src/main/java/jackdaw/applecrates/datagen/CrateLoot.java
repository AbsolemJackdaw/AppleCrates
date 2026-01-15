package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CrateLoot extends LootTableProvider {

    private final String modId;

    public CrateLoot(String modId, DataGenerator pGenerator, CompletableFuture<HolderLookup.Provider> registries) {
        super(pGenerator.getPackOutput(), null, null, registries);
        this.modId = modId;
    }


    @Override
    public List<SubProviderEntry> getTables() {
        return Collections.singletonList(new SubProviderEntry(CrateLootTable::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        /*NOOP*/
    }

    private class CrateLootTable extends BlockLootSubProvider {

        public CrateLootTable(HolderLookup.Provider provider) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
            CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modId)).map(CrateWoodType::getBlock).forEach(
                    block -> {
                        this.add(block, this.createNameableBlockEntityTable(block));
                    }
            );
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modId)).map(CrateWoodType::getBlock).collect(Collectors.toList());
        }
    }
}