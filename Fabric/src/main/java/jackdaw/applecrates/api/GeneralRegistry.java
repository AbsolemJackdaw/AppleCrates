package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.*;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GeneralRegistry {

    public record CrateData(boolean unlimited) {
        public static final StreamCodec<RegistryFriendlyByteBuf, CrateData> MENU_CODEC = StreamCodec.composite(
                ByteBufCodecs.BOOL,
                CrateData::unlimited,
                CrateData::new
        );
    }

    public static final ExtendedScreenHandlerType<CrateMenuOwner, ?> CRATE_MENU_OWNER = new ExtendedScreenHandlerType<>((syncId, inventory, data) -> {
        boolean unlimited = data.unlimited();
        return new CrateMenuOwnerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    }, CrateData.MENU_CODEC);
    public static final ExtendedScreenHandlerType<CrateMenuBuyer, ?> CRATE_MENU_BUYER = new ExtendedScreenHandlerType<>((syncId, inventory, data) -> {
        boolean unlimited = data.unlimited();
        return new CrateMenuBuyerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    }, CrateData.MENU_CODEC);

    public static final List<Supplier<BlockEntityType<CrateBlockEntity>>> besrreg = new ArrayList<>();
    private static final List<Block> TAB_BLOCKS = new ArrayList<>();

    public static void prepareForRegistry(String modId) {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(modId)).forEach(crateWoodType -> {
            var crate = new CrateBlock(crateWoodType, makeBlockKey(crateWoodType));
            Registry.register(BuiltInRegistries.BLOCK, Identifier.fromNamespaceAndPath(modId, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(modId, crateWoodType.getBlockRegistryName()), new CrateItem(crate, makeItemKey(crateWoodType)));
            var type = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(modId, crateWoodType.getBeRegistryName()),
                    FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new CrateBlockEntity(crateWoodType, blockPos, blockState, new StackHandlerAdapter()), crate).build());
            besrreg.add(() -> type);
            TAB_BLOCKS.add(crate);
        });
    }

    private static ResourceKey<Block> makeBlockKey(CrateWoodType key) {
        return ResourceKey.create(Registries.BLOCK, key.getFullRegistryResLoc());
    }

    private static ResourceKey<Item> makeItemKey(CrateWoodType key) {
        return ResourceKey.create(Registries.ITEM, key.getFullRegistryResLoc());
    }

    public static void startUp() {
        var tab = createTab();
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Constants.MODID, "crate_menu_owner"), CRATE_MENU_OWNER);
        Registry.register(BuiltInRegistries.MENU, Identifier.fromNamespaceAndPath(Constants.MODID, "crate_menu_buyer"), CRATE_MENU_BUYER);
        Content.populateMenus();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MODID, "tab.crate"), tab);
        ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).get()).register(GeneralRegistry::populateTab);
        var dataComponentRegistryResult = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Identifier.fromNamespaceAndPath(Constants.MODID, "coin_counter"), DataComponentType.<CoinCounter>builder().persistent(Content.DATA_COIN_CODEC).build());
        Content.coinCounter = dataComponentRegistryResult;
    }


    private static void populateTab(FabricItemGroupEntries entries) {
        TAB_BLOCKS.forEach(entries::accept);
    }

    private static CreativeModeTab createTab() {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("tab.crate"))
                .icon(() -> new ItemStack(CrateWoodType.getBlock(CrateWoodType.values().filter(crateWoodType -> crateWoodType.name().equals("oak")).findFirst().get()))).build();
    }
}
