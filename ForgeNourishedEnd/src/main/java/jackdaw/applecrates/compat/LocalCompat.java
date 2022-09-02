package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String nourished_end = "nourished_end";
        new AppleCrateBuilder(nourished_end, "cerulean").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "seldge").withParentFolder("blocks/").register();
        new AppleCrateBuilder(nourished_end, "verdant").withParentFolder("blocks/").register();
    }
}
