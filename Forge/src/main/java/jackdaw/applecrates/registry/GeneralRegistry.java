package jackdaw.applecrates.registry;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.item.CrateItem;
import net.minecraft.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class GeneralRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, AppleCrates.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AppleCrates.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, AppleCrates.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, AppleCrates.MODID);
    public static final RegistryObject<MenuType<CrateMenu>> CRATE_MENU_BUYER = MENU_TYPES.register("crate_menu_buyer", () -> new MenuType<>((id, inv) -> new CrateMenu(id, inv, false)));
    public static final RegistryObject<MenuType<CrateMenu>> CRATE_MENU_OWNER = MENU_TYPES.register("crate_menu_owner", () -> new MenuType<>((id, inv) -> new CrateMenu(id, inv, true)));

    public static final Map<WoodType, RegistryObject<Block>> BLOCK_MAP = Util.make(() -> {
        Map blockMap = new HashMap<>();
        WoodType.values().forEach(woodType -> {
            blockMap.put(woodType, BLOCKS.register(woodType.name() + "_crate", () -> new CrateBlock(woodType)));
        });
        return blockMap;
    });
    public static final Map<WoodType, RegistryObject<Item>> ITEM_MAP = Util.make(() ->
    {
        Map itemMap = new HashMap<>();
        BLOCK_MAP.forEach((woodType, block) -> {
            itemMap.put(woodType, ITEMS.register(woodType.name() + "_crate", () -> new CrateItem(block.get())));
        });
        return itemMap;
    });
    public static final Map<WoodType, RegistryObject<BlockEntityType<CrateBE>>> BE_MAP = Util.make(() -> {
        Map<WoodType, RegistryObject<BlockEntityType<CrateBE>>> blockEntityMap = new HashMap<>();
        BLOCK_MAP.forEach((woodType, block) -> {
            blockEntityMap.put(woodType, BLOCK_ENTITIES.register(woodType.name() + "_crate_be", () ->
                    BlockEntityType.Builder.of((pos, state) -> new CrateBE(woodType, pos, state), block.get()).build(null)));
        });

        return blockEntityMap;
    });

    public static void startup() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENU_TYPES.register(bus);
    }


}
