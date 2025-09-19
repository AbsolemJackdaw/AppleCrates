package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.network.ServerNetwork;
import net.fabricmc.api.ModInitializer;

public class FabricCrates implements ModInitializer {


    @Override
    public void onInitialize() {
        GeneralRegistry.startUp();
        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        GeneralRegistry.prepareForRegistry(Constants.MODID);
        ServerNetwork.registerServerPackets();
    }
}
