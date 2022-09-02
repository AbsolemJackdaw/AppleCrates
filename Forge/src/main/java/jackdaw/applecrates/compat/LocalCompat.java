package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String integratedDynamicsModId = "integrateddynamics";
        new AppleCrateBuilder(integratedDynamicsModId, "menril").register();

        String solarforgeModId = "solarforge";
        new AppleCrateBuilder(solarforgeModId, "radiant").register();
        new AppleCrateBuilder(solarforgeModId, "runic").register();



        String fruitTreesModId = "fruittrees";
        new AppleCrateBuilder(fruitTreesModId, "cherry").withSubfolder("fruittrees/").textureInMinecraftDirectory().register();
        new AppleCrateBuilder(fruitTreesModId, "citrus").withSubfolder("fruittrees/").withTextureName("planks").withSuffix("").textureInMinecraftDirectory().register(); //citrus wood is called plank...

        String malum = "malum";
        new AppleCrateBuilder(malum, "runewood").register();
        new AppleCrateBuilder(malum, "soulwood").register();

        String nourished_end = "nourished_end";
        new AppleCrateBuilder(nourished_end, "cerulean").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "seldge").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "verdant").withParentFolder("blocks/").register();


    }
}
