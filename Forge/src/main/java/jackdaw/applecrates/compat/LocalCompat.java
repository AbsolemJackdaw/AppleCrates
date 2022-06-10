package jackdaw.applecrates.compat;

import jackdaw.applecrates.compat.api.AppleCrateAPI;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        //test for wood
        String brazier = "projectbrazier";
        AppleCrateAPI.registerForCrate(brazier, "orange", "woods/orange/");
        AppleCrateAPI.registerForCrate(brazier, "apple", "woods/apple/");

        String botaniaModId = "botania";
        AppleCrateAPI.registerForCrate(botaniaModId, "dreamwood");
        AppleCrateAPI.registerForCrate(botaniaModId, "livingwood");
        AppleCrateAPI.registerForCrate(botaniaModId, "shimmerwood");
        AppleCrateAPI.registerForCrate(botaniaModId, "mossy_dreamwood");
        AppleCrateAPI.registerForCrate(botaniaModId, "mossy_livingwood");

        String integratedDynamicsModId = "integrateddynamics";
        AppleCrateAPI.registerForCrate(integratedDynamicsModId, "menril");

        String solarforgeModId = "solarforge";
        AppleCrateAPI.registerForCrate(solarforgeModId, "radiant");
        AppleCrateAPI.registerForCrate(solarforgeModId, "runic");

        String tinkersconstructmodid = "tconstruct";
        AppleCrateAPI.registerForCrate(tinkersconstructmodid, "nahuatl");
        AppleCrateAPI.registerForCrate(tinkersconstructmodid, "greenheart", "greenheart/");
        AppleCrateAPI.registerForCrate(tinkersconstructmodid, "skyroot", "skyroot/");
        AppleCrateAPI.registerForCrate(tinkersconstructmodid, "bloodshroom", "bloodshroom/");

        String bygModId = "byg";
        String[] bygWoods = {"aspen", "baobab", "blue_enchanted", "bulbis", "cherry", "cika", "cypress", "ebony", "embur", "ether", "fir", "green_enchanted",
                "holly", "imparius", "jacaranda", "lament", "mahogany", "mangrove", "maple", "nightshade", "palm", "palo_verde", "prine", "rainbow_eucalyptus",
                "redwood", "skyris", "sythian", "willow", "witch_hazel", "withering_oak", "zelkova"};
        for (String woodName : bygWoods)
            AppleCrateAPI.registerForCrate(bygModId, woodName);

        String fruitTreesModId = "fruittrees";
        AppleCrateAPI.registerForCrate(fruitTreesModId, "cherry", "fruittrees/");
        AppleCrateAPI.registerForCrate(fruitTreesModId, "citrus", "fruittrees/");

        String malum = "malum";
        AppleCrateAPI.registerForCrate(malum, "runewood");
        AppleCrateAPI.registerForCrate(malum, "soulwood");

        String abundance = "abundance";
        AppleCrateAPI.registerForCrate(abundance, "jacaranda");
        AppleCrateAPI.registerForCrate(abundance, "redbud");

        String forbidden_arcanus = "forbidden_arcanus";
        AppleCrateAPI.registerForCrate(forbidden_arcanus, "cherrywood");
        AppleCrateAPI.registerForCrate(forbidden_arcanus, "edelwood");
        AppleCrateAPI.registerForCrate(forbidden_arcanus, "fungyss");
        AppleCrateAPI.registerForCrate(forbidden_arcanus, "mysterywood");


    }
}
