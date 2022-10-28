package jackdaw.applecrates;

import jackdaw.applecrates.block.OpenGui;
import jackdaw.applecrates.block.blockentity.CrateStock;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.MenuSlots;
import jackdaw.applecrates.network.PacketHandler;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class Content {

    public static Supplier<MenuType<CrateMenu>> MENUTYPE;

    public static MenuSlots menuSlots;

    public static OpenGui openGui;

    public static CrateStock crateStock;

    public static PacketHandler packetHandler;
}
