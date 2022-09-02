package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String tinkersconstructmodid = "tconstruct";
        new AppleCrateBuilder(tinkersconstructmodid, "nahuatl").register();
        new AppleCrateBuilder(tinkersconstructmodid, "greenheart").withSubfolder("greenheart/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "skyroot").withSubfolder("skyroot/").register();
        new AppleCrateBuilder(tinkersconstructmodid, "bloodshroom").withSubfolder("bloodshroom/").register();
    }
}
