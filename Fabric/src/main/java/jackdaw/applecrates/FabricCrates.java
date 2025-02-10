package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBlockEntity;
import jackdaw.applecrates.container.*;
import jackdaw.applecrates.container.slot.SlotCrateStock;
import jackdaw.applecrates.container.slot.SlotPriceSale;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.network.ServerNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class FabricCrates implements ModInitializer {

    public static final ExtendedScreenHandlerType<CrateMenuOwner> CRATE_MENU_OWNER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuOwnerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    });
    public static final ExtendedScreenHandlerType<CrateMenuBuyer> CRATE_MENU_BUYER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuBuyerService(syncId, inventory, new StackHandlerAdapter(), unlimited);
    });

    public static final List<Supplier<BlockEntityType<CrateBlockEntity>>> besrreg = new ArrayList<>();

    @Override
    public void onInitialize() {
        AppleCrateAPI.AppleCrateBuilder.registerVanilla();

        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MODID, "crate_menu_owner"), CRATE_MENU_OWNER);
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(Constants.MODID, "crate_menu_buyer"), CRATE_MENU_BUYER);
        var items = new ArrayList<ItemStack>();
        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(Constants.MODID)).forEach(crateWoodType -> {
            var crate = new CrateBlock(crateWoodType);
            //use oak crate as tab icon. TODO improve check
            Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), new CrateItem(crate));
            var type = Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    new ResourceLocation(Constants.MODID, crateWoodType.getBeRegistryName()),
                    BlockEntityType.Builder.of((blockPos, blockState) -> new CrateBlockEntity(crateWoodType, blockPos, blockState, new StackHandlerAdapter()), crate).build(null));
            besrreg.add(() -> type);
            //add crate to creative tab
            items.add(new ItemStack(crate));
        });

        var tabBuilder = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("tab.crate"))
                .icon(() -> new ItemStack(CrateWoodType.getBlock(CrateWoodType.values().filter(crateWoodType -> crateWoodType.name().equals("oak")).findFirst().get())))
                .displayItems((itemDisplayParameters, output) -> output.acceptAll(items));

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Constants.MODID, "tab.crate"), tabBuilder.build());

        ServerNetwork.registerServerPackets();

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
}
