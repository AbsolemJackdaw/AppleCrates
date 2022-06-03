package jackdaw.applecrates.datagen;

import com.mojang.datafixers.util.Pair;
import jackdaw.applecrates.registry.GeneralRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
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

public class CrateLoot extends LootTableProvider {

    public CrateLoot(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        //keep empty
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return Collections.singletonList(Pair.of(CrateLootTable::new, LootContextParamSets.BLOCK));
    }

    private static class CrateLootTable extends BlockLoot {
        @Override
        protected void addTables() {
            WoodType.values().map(woodType -> GeneralRegistry.BLOCK_MAP.get(woodType).get()).forEach(this::dropSelf);
        }
    }
}
