package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.block.CommonCrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.item.CrateItem;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GeneralRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Constants.MODID);
    public static final RegistryObject<MenuType<CrateMenu>> CRATE_MENU = MENU_TYPES.register("crate_menu_buyer", () -> IForgeMenuType.create((windowId, inv, data) -> {
        boolean owner = data.readBoolean();
        boolean unlimited = data.readBoolean();
        return new CrateMenu(windowId, inv, owner, unlimited);
    }));

    /**
     * @param modId filter through all registered crates. Use your own modid
     */
    public static void prepareForRegistry(String modId, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, DeferredRegister<BlockEntityType<?>> beRegistry) {

        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(modId)).forEach(crateWoodType -> {
            RegistryObject<Block> block = blockRegistry.register(crateWoodType.getBlockRegistryName(), () -> new CommonCrateBlock(crateWoodType));
            itemRegistry.register(crateWoodType.getBlockRegistryName(), () -> new CrateItem(block.get()));
            beRegistry.register(crateWoodType.getBeRegistryName(), () -> BlockEntityType.Builder.of((pos, state) -> new CrateBE(crateWoodType, pos, state, new StackHandlerAdapter()), block.get()).build(null));

        });
    }

    public static void startup() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        MENU_TYPES.register(bus);
    }
}
