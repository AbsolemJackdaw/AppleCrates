package jackdaw.applecrates.api;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.*;
import jackdaw.applecrates.item.CrateItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GeneralRegistry {

    public static final ExtendedScreenHandlerType<CrateMenuOwner> CRATE_MENU_OWNER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuOwnerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    });
    public static final ExtendedScreenHandlerType<CrateMenuBuyer> CRATE_MENU_BUYER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuBuyerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    });

    public static final List<Supplier<BlockEntityType<CrateBlockEntity>>> besrreg = new ArrayList<>();
    private static final List<Block> TAB_BLOCKS = new ArrayList<>();

    public static void prepareForRegistry(String modId) {
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(modId)).forEach(crateWoodType -> {
            var crate = new CrateBlock(crateWoodType);
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(modId, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(modId, crateWoodType.getBlockRegistryName()), new CrateItem(crate));
            var type = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    new ResourceLocation(modId, crateWoodType.getBeRegistryName()),
                    BlockEntityType.Builder.of((blockPos, blockState) -> new CrateBlockEntity(crateWoodType, blockPos, blockState, new StackHandlerAdapter()), crate).build(null));
            besrreg.add(() -> type);
            TAB_BLOCKS.add(crate);
        });
    }

    public static void startUp() {
        var tab = createTab();
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MODID, "crate_menu_owner"), CRATE_MENU_OWNER);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MODID, "crate_menu_buyer"), CRATE_MENU_BUYER);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MODID, "tab.crate"), tab);
        ItemGroupEvents.modifyEntriesEvent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).get()).register(GeneralRegistry::populateTab);
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
