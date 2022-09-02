package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String fruitTreesModId = "fruittrees";
        new AppleCrateBuilder(fruitTreesModId, "cherry").withSubfolder("fruittrees/").textureInMinecraftDirectory().register();
        new AppleCrateBuilder(fruitTreesModId, "citrus").withSubfolder("fruittrees/").withTextureName("planks").withSuffix("").textureInMinecraftDirectory().register(); //citrus wood is called plank...
    }
}
