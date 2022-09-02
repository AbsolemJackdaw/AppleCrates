package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
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
