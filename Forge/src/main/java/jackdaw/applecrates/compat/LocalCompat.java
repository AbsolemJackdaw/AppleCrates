package jackdaw.applecrates.compat;

import jackdaw.applecrates.compat.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String botaniaModId = "botania";
        new AppleCrateBuilder(botaniaModId, "dreamwood").register();
        new AppleCrateBuilder(botaniaModId, "livingwood").register();
        new AppleCrateBuilder(botaniaModId, "shimmerwood").register();
        new AppleCrateBuilder(botaniaModId, "mossy_dreamwood").register();
        new AppleCrateBuilder(botaniaModId, "mossy_livingwood").register();

        String integratedDynamicsModId = "integrateddynamics";
        new AppleCrateBuilder(integratedDynamicsModId, "menril").register();

        String solarforgeModId = "solarforge";
        new AppleCrateBuilder(solarforgeModId, "radiant").register();
        new AppleCrateBuilder(solarforgeModId, "runic").register();

        String tinkersconstructmodid = "tconstruct";
        new AppleCrateBuilder(tinkersconstructmodid, "nahuatl").register();
        new AppleCrateBuilder(tinkersconstructmodid, "greenheart").withSubfolder("greenheart/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "skyroot").withSubfolder("skyroot/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "bloodshroom").withSubfolder("bloodshroom/").register();

        String bygModId = "byg";
        String[] bygWoods = {"aspen", "baobab", "blue_enchanted", "bulbis", "cherry", "cika", "cypress", "ebony", "embur", "ether", "fir", "green_enchanted",
                "holly", "imparius", "jacaranda", "lament", "mahogany", "mangrove", "maple", "nightshade", "palm", "pine", "rainbow_eucalyptus",
                "redwood", "skyris", "sythian", "willow", "witch_hazel", "zelkova"};
        for (String woodName : bygWoods)
            new AppleCrateBuilder(bygModId, woodName).register();

        String fruitTreesModId = "fruittrees";
        new AppleCrateBuilder(fruitTreesModId, "cherry").withSubfolder("fruittrees/").textureInMinecraftDirectory().register();
        new AppleCrateBuilder(fruitTreesModId, "citrus").withSubfolder("fruittrees/").withTextureName("planks").withSuffix("").textureInMinecraftDirectory().register(); //citrus wood is called plank...

        String malum = "malum";
        new AppleCrateBuilder(malum, "runewood").register();
        new AppleCrateBuilder(malum, "soulwood").register();

        String premium_wood = "premium_wood";
        new AppleCrateBuilder(premium_wood, "magic").withSubfolder("magic/").register();
        new AppleCrateBuilder(premium_wood, "maple").withSubfolder("maple/").register();
        new AppleCrateBuilder(premium_wood, "purple_heart").withSubfolder("purple_heart/").register();
        new AppleCrateBuilder(premium_wood, "silverbell").withSubfolder("silverbell/").register();
        new AppleCrateBuilder(premium_wood, "tiger").withSubfolder("tiger/").register();
        new AppleCrateBuilder(premium_wood, "willow").withSubfolder("willow/").register();

        String nourished_end = "nourished_end";
        new AppleCrateBuilder(nourished_end, "cerulean").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "seldge").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "verdant").withParentFolder("blocks/").register();

        String forbidden_arcanus = "forbidden_arcanus";
        new AppleCrateBuilder(forbidden_arcanus, "cherrywood").register();
        new AppleCrateBuilder(forbidden_arcanus, "edelwood").register();
        new AppleCrateBuilder(forbidden_arcanus, "fungyss").register();
        new AppleCrateBuilder(forbidden_arcanus, "mysterywood").register();

        String biomesoplenty = "biomesoplenty";
        new AppleCrateBuilder(biomesoplenty, "cherry").register();
        new AppleCrateBuilder(biomesoplenty, "dead").register();
        new AppleCrateBuilder(biomesoplenty, "fir").register();
        new AppleCrateBuilder(biomesoplenty, "hellbark").register();
        new AppleCrateBuilder(biomesoplenty, "jacaranda").register();
        new AppleCrateBuilder(biomesoplenty, "magic").register();
        new AppleCrateBuilder(biomesoplenty, "mahogany").register();
        new AppleCrateBuilder(biomesoplenty, "palm").register();
        new AppleCrateBuilder(biomesoplenty, "redwood").register();
        new AppleCrateBuilder(biomesoplenty, "umbran").register();
        new AppleCrateBuilder(biomesoplenty, "willow").register();


    }
}
