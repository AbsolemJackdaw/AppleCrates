package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.*;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.StacksResourceHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GeneralRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Constants.MODID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Constants.MODID);
    private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Constants.MODID);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Constants.MODID);
    private static final List<DeferredHolder<Block, CrateBlock>> TAB_REGISTRY = new ArrayList<>();
    private static final List<DeferredHolder<BlockEntityType<?>, BlockEntityType<CrateBlockEntity>>> CAPABILITY_REGISTRY = new ArrayList<DeferredHolder<BlockEntityType<?>, BlockEntityType<CrateBlockEntity>>>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CRATE_TAB = TABS.register("tab.crate", () ->
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .title(Component.translatable("tab.crate"))
                    //.displayItems((itemDisplayParameters, output) -> GeneralRegistry.BLOCKS.getEntries().stream().map(blockRegistryObject -> blockRegistryObject.get()).forEach(output::accept))
                    .icon(() -> new ItemStack(CrateWoodType.getBlock(CrateWoodType.values().filter(crateWoodType -> crateWoodType.name().equals("oak")).findFirst().get())))
                    .build());
    public static final BlockCapability<StacksResourceHandler, @Nullable Direction> CRATE_CAPABILITY_HOPPER =
            BlockCapability.create(
                    ResourceLocation.fromNamespaceAndPath(Constants.MODID, "crate_hopper_handler"),
                    StacksResourceHandler.class,
                    Direction.class);
    public static final DeferredHolder<MenuType<?>, MenuType<CrateMenuOwner>> CRATE_MENU_OWNER = MENU_TYPES.register("crate_menu_owner", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
        boolean unlimited = data.readBoolean();
        return new CrateMenuOwnerService(windowId, inv, new StackHandlerAdapter(), unlimited);
    }));
    public static final DeferredHolder<MenuType<?>, MenuType<CrateMenuBuyer>> CRATE_MENU_BUYER = MENU_TYPES.register("crate_menu_buyer", () -> IMenuTypeExtension.create((windowId, inv, data) -> {
        boolean unlimited = data.readBoolean();
        return new CrateMenuBuyerService(windowId, inv, new StackHandlerAdapter(), unlimited);
    }));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CoinCounter>> COIN_COUNTER = DATA_COMPONENTS.registerComponentType(
            "coin_counter",
            builder -> builder
                    // The codec to read/write the data to disk
                    .persistent(Content.DATA_COIN_CODEC)
                    // The codec to read/write the data across the network
                    .networkSynchronized(Content.NETWORK_COIN_CODEC)
    );

    /**
     * @param modId filter through all registered crates. Use your own modid
     */
    public static void prepareForRegistry(String modId, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, DeferredRegister<BlockEntityType<?>> beRegistry) {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(modId)).forEach(crateWoodType -> {
            DeferredHolder<Block, CrateBlock> block = blockRegistry.register(crateWoodType.getBlockRegistryName(), () -> new CrateBlock(crateWoodType, makeBlockKey(crateWoodType)));
            itemRegistry.register(crateWoodType.getBlockRegistryName(), () -> new CrateItem(block.get(), makeItemKey(crateWoodType)));
            var be = beRegistry.register(crateWoodType.getBeRegistryName(), () -> new BlockEntityType<>((pos, state) -> new CrateBlockEntity(crateWoodType, pos, state), false, block.get()));
            TAB_REGISTRY.add(block);
            CAPABILITY_REGISTRY.add(be);
        });
    }

    private static ResourceKey<Block> makeBlockKey(CrateWoodType key) {
        return ResourceKey.create(Registries.BLOCK, key.getFullRegistryResLoc());
    }

    private static ResourceKey<Item> makeItemKey(CrateWoodType key) {
        return ResourceKey.create(Registries.ITEM, key.getFullRegistryResLoc());
    }

    public static void startup() {
        var bus = ModList.get().getModContainerById(Constants.MODID).get().getEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        MENU_TYPES.register(bus);
        TABS.register(bus);
        DATA_COMPONENTS.register(bus);
    }


    @EventBusSubscriber(modid = Constants.MODID)
    private class EventHandler {
        @SubscribeEvent
        public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey().equals(GeneralRegistry.CRATE_TAB.getKey())) {
                for (DeferredHolder<Block, CrateBlock> block : GeneralRegistry.TAB_REGISTRY)
                    event.accept(block.get());
            }
        }

        @SubscribeEvent  // on the mod event bus
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (var BE : CAPABILITY_REGISTRY)
                event.registerBlockEntity(
                        CRATE_CAPABILITY_HOPPER, // capability to register for
                        BE.get(), // block entity type to register for
                        CrateBlockEntity::getCapability);
        }
    }
}
