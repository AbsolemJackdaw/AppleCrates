package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String solarforgeModId = "solarforge";
        new AppleCrateBuilder(solarforgeModId, "radiant").register();
        new AppleCrateBuilder(solarforgeModId, "runic").register();
    }
}
