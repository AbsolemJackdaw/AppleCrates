package jackdaw.applecrates.datagen;

import jackdaw.applecrates.api.CrateWoodType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrateLoot extends LootTableProvider {

    private final String modId;

    public CrateLoot(String modId, DataGenerator pGenerator) {
        super(pGenerator.getPackOutput(), null, null);
        this.modId = modId;
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return super.getTables();
    }

    //    @Override
//    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
//        return Collections.singletonList(Pair.of(CrateLootTable::new, LootContextParamSets.BLOCK));
//    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) { /*NOOP*/ }

    private class CrateLootTable extends VanillaBlockLoot {

        @Override
        protected void generate() {
            CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modId)).map(CrateWoodType::getBlock).forEach(this::dropSelf);
        }

//        @Override
//        protected void addTables() {
//            CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modid)).map(CrateWoodType::getBlock).forEach(this::dropSelf);
//        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrateWoodType.values().filter(crateWoodType -> crateWoodType.isFrom(modId)).map(CrateWoodType::getBlock).collect(Collectors.toList());
        }
    }
}
