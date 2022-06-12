package jackdaw.applecrates.registry;

import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.util.CrateWoodType;
import net.minecraft.Util;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
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
    public static final RegistryObject<MenuType<CrateMenu>> CRATE_MENU = MENU_TYPES.register("crate_menu_buyer", () -> IForgeMenuType.create((windowId, inv, data) -> {
        boolean owner = data.readBoolean();
        boolean unlimited = data.readBoolean();
        return new CrateMenu(windowId, inv, owner, unlimited);
    }));

    /**
     * filled on mod startup. not in final declaration, because race condition related to CrateWoodType registry
     */
    public static Map<CrateWoodType, RegistryObject<Block>> BLOCK_MAP;
    public static Map<CrateWoodType, RegistryObject<Item>> ITEM_MAP;
    public static Map<CrateWoodType, RegistryObject<BlockEntityType<CrateBE>>> BE_MAP;

    public static void prepareMaps() {
        BLOCK_MAP = Util.make(() -> {
            Map blockMap = new HashMap<>();
            CrateWoodType.values().forEach(woodType -> {
                blockMap.put(woodType, BLOCKS.register(woodType.fullName() + "_crate", () -> new CrateBlock(woodType)));
            });
            return blockMap;
        });
        ITEM_MAP = Util.make(() ->
        {
            Map itemMap = new HashMap<>();
            BLOCK_MAP.forEach((woodType, block) -> {
                itemMap.put(woodType, ITEMS.register(woodType.fullName() + "_crate", () -> new CrateItem(block.get())));
            });
            return itemMap;
        });
        BE_MAP = Util.make(() -> {
            Map<CrateWoodType, RegistryObject<BlockEntityType<CrateBE>>> blockEntityMap = new HashMap<>();
            BLOCK_MAP.forEach((woodType, block) -> {
                blockEntityMap.put(woodType, BLOCK_ENTITIES.register(woodType.fullName() + "_crate_be", () ->
                        BlockEntityType.Builder.of((pos, state) -> new CrateBE(woodType, pos, state), block.get()).build(null)));
            });

            return blockEntityMap;
        });
    }

    public static void startup() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENU_TYPES.register(bus);
    }

}
