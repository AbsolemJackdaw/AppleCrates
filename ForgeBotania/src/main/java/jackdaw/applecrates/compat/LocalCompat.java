package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String botaniaModId = "botania";
        new AppleCrateBuilder(botaniaModId, "dreamwood").register();
        new AppleCrateBuilder(botaniaModId, "livingwood").register();
        new AppleCrateBuilder(botaniaModId, "shimmerwood").register();
        new AppleCrateBuilder(botaniaModId, "mossy_dreamwood").register();
        new AppleCrateBuilder(botaniaModId, "mossy_livingwood").register();
    }
}
