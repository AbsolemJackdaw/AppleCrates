package jackdaw.applecrates.registry;

import jackdaw.applecrates.Content;
import jackdaw.applecrates.container.CrateMenu;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraftforge.network.NetworkHooks;

public class OpenGuiRegistry {

    public static void init() {
        Content.openGui = (sp, crate, component, isOwner) -> {
            NetworkHooks.openScreen(sp, new SimpleMenuProvider((id, inv, player) ->
                    new CrateMenu(id, inv, crate, crate.isOwner(player), crate.isUnlimitedShop), component), buf -> {
                buf.writeBoolean(isOwner);
                buf.writeBoolean(crate.isUnlimitedShop);
            });
        };
    }
}
