package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String malum = "malum";
        new AppleCrateBuilder(malum, "runewood").register();
        new AppleCrateBuilder(malum, "soulwood").register();
    }
}
