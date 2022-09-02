package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String tinkersconstructmodid = "tconstruct";
        new AppleCrateBuilder(tinkersconstructmodid, "nahuatl").withSubfolder("wood/").withSuffix("").register();
        new AppleCrateBuilder(tinkersconstructmodid, "greenheart").withTextureName("planks").withSuffix("").withSubfolder("wood/greenheart/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "skyroot").withTextureName("planks").withSuffix("").withSubfolder("wood/skyroot/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "bloodshroom").withTextureName("planks").withSuffix("").withSubfolder("wood/bloodshroom/").register();
    }
}
