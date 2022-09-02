package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String bygModId = "byg";
        String[] bygWoods = {"aspen", "baobab", "blue_enchanted", "bulbis", "cherry", "cika", "cypress", "ebony", "embur", "ether", "fir", "green_enchanted",
                "holly", "imparius", "jacaranda", "lament", "mahogany", "white_mangrove", "maple", "nightshade", "palm", "pine", "rainbow_eucalyptus",
                "redwood", "skyris", "sythian", "willow", "witch_hazel", "zelkova"};
        for (String woodName : bygWoods)
            new AppleCrateBuilder(bygModId, woodName).withSubfolder(woodName + "/").withTextureName("planks").withSuffix("").register();

    }
}
