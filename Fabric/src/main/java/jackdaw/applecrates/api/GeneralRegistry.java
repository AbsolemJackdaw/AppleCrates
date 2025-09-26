package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.*;
import jackdaw.applecrates.container.slot.SlotCrateStock;
import jackdaw.applecrates.container.slot.SlotPriceSale;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

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
            var crate = new CrateBlock(crateWoodType);
            Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(modId, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, crateWoodType.getBlockRegistryName()), new CrateItem(crate));
            var type = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(modId, crateWoodType.getBeRegistryName()),
                    BlockEntityType.Builder.of((blockPos, blockState) -> new CrateBlockEntity(crateWoodType, blockPos, blockState, new StackHandlerAdapter()), crate).build(null));
            besrreg.add(() -> type);
            TAB_BLOCKS.add(crate);
        });
    }

    public static void startUp() {
        var tab = createTab();
        Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "crate_menu_owner"), CRATE_MENU_OWNER);
        Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "crate_menu_buyer"), CRATE_MENU_BUYER);
        GeneralRegistry.initializeMenus();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "tab.crate"), tab);
        ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).get()).register(GeneralRegistry::populateTab);
        var dataComponentRegistryResult = Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MODID, "coin_counter"), DataComponentType.<CoinCounter>builder().persistent(Content.DATA_COIN_CODEC).build());
        Content.coinCounter = dataComponentRegistryResult;
    }

    private static void initializeMenus() {
        Content.menuSlots = menu -> {
            if (!(menu.adapter instanceof StackHandlerAdapter stackHandlerAdapter))
                return;
            menu.addSlot(new Slot(stackHandlerAdapter.interactableTrades, 0, menu.isOwner() ? 10 : 102, menu.isOwner() ? 76 : 21));
            menu.addSlot(new Slot(stackHandlerAdapter.interactableTrades, 1, menu.isOwner() ? 46 : 142, menu.isOwner() ? 76 : 21) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return menu.isOwner();
                }
            });

            menu.addSlot(new SlotPriceSale(stackHandlerAdapter.savedTrades, 0));//buyer pays
            menu.addSlot(new SlotPriceSale(stackHandlerAdapter.savedTrades, 1));//buyer gets

            for (int y = 0; y < 3; y++) //crate stock
                for (int x = 0; x < 10; x++)
                    menu.addSlot(new SlotCrateStock(stackHandlerAdapter.crateStock, y * 10 + x, x * 18 + 10, y * 18 + 17, menu.isOwner()));
            menu.addSlot(new SlotCrateStock(stackHandlerAdapter.crateStock, Constants.TOTALCRATESTOCKLOTS, 172, 76, menu.isOwner()));
        };
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
