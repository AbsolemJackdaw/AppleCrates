package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.SlotCrateStock;
import jackdaw.applecrates.container.SlotPriceSale;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.network.ServerNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricCrates implements ModInitializer {
    public static final List<Supplier<BlockEntityType<CrateBE>>> besrreg = new ArrayList<>();
    private static final ScreenHandlerRegistry.ExtendedClientHandlerFactory<CrateMenu> factory = (syncId, inventory, buf) -> {
        var level = inventory.player.level;
        boolean owner = buf.readBoolean();
        boolean unlimited = buf.readBoolean();
        BlockPos pos = buf.readBlockPos();
        if (level.getBlockEntity(pos) instanceof CrateBE crateBE)
            return new CrateMenu(syncId, inventory, crateBE, owner, unlimited);
        return null;
    };
    public static final ExtendedScreenHandlerType<CrateMenu> CRATETYPE = new ExtendedScreenHandlerType<>(factory);

    @Override
    public void onInitialize() {
        AppleCrateAPI.AppleCrateBuilder.registerVanilla();

        Registry.register(Registry.MENU, new ResourceLocation(Constants.MODID, "cratemenu"), CRATETYPE);

        Content.menuSlots = menu -> {
            if (menu.interactableSlots instanceof SimpleContainer container) {
                menu.addGenericSlot(new Slot(container, 0, 136, 37));
                menu.addGenericSlot(new Slot(container, 1, 220, 38) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return menu.isOwner;
                    }
                });
            }
            if (menu.priceAndSaleSlots instanceof SimpleContainer container) {
                menu.addGenericSlot(new SlotPriceSale(container, 0, 10, 140));
                menu.addGenericSlot(new SlotPriceSale(container, 1, 74, 140));
            }

            if (menu.crateStock instanceof SimpleContainer container)
                for (int y = 0; y < 6; y++) //crate stock
                    for (int x = 0; x < 5; x++)
                        menu.addGenericSlot(new SlotCrateStock(container, y * 5 + x, x * 18 + 5, y * 18 + 18, menu.isOwner));

        };

        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(Constants.MODID)).forEach(crateWoodType -> {
            var crate = new CrateBlock(crateWoodType);
            Registry.register(Registry.BLOCK, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(Registry.ITEM, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), new CrateItem(crate));
            var type = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new ResourceLocation(Constants.MODID, crateWoodType.getBeRegistryName()),
                    BlockEntityType.Builder.of((blockPos, blockState) -> new CrateBE(crateWoodType, blockPos, blockState), crate).build(null));
            besrreg.add(() -> type);
        });

        ServerNetwork.registerServerPackets();
    }
}
