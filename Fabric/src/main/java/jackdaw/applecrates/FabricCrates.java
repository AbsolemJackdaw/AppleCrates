package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.CrateWoodType;
import jackdaw.applecrates.block.CrateBlock;
import jackdaw.applecrates.block.blockentity.CrateBE;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.CrateMenuBuyer;
import jackdaw.applecrates.container.CrateMenuOwner;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.item.CrateItem;
import jackdaw.applecrates.network.ServerNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FabricCrates implements ModInitializer {

    public static final ExtendedScreenHandlerType<CrateMenuOwner> CRATE_MENU_OWNER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuOwner(syncId, inventory, unlimited);
    });
    public static final ExtendedScreenHandlerType<CrateMenuBuyer> CRATE_MENU_BUYER = new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
        boolean unlimited = buf.readBoolean();
        return new CrateMenuBuyer(syncId, inventory, unlimited);
    });

    public static final List<Supplier<BlockEntityType<CrateBE>>> besrreg = new ArrayList<>();

    @Override
    public void onInitialize() {
        AppleCrateAPI.AppleCrateBuilder.registerVanilla();

        Registry.register(Registry.MENU, new ResourceLocation(Constants.MODID, "crate_menu_owner"), CRATE_MENU_OWNER);
        Registry.register(Registry.MENU, new ResourceLocation(Constants.MODID, "crate_menu_buyer"), CRATE_MENU_BUYER);

        CrateWoodType.values().filter(crateWoodType -> crateWoodType.getYourModId().equals(Constants.MODID)).forEach(crateWoodType -> {
            var crate = new CrateBlock(crateWoodType);
            Registry.register(Registry.BLOCK, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), crate);
            Registry.register(Registry.ITEM, new ResourceLocation(Constants.MODID, crateWoodType.getBlockRegistryName()), new CrateItem(crate));
            var type = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new ResourceLocation(Constants.MODID, crateWoodType.getBeRegistryName()),
                    BlockEntityType.Builder.of((blockPos, blockState) -> new CrateBE(crateWoodType, blockPos, blockState, new StackHandlerAdapter()), crate).build(null));
            besrreg.add(() -> type);
        });
        ServerNetwork.registerServerPackets();
    }
}
