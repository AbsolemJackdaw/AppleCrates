package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String forbidden_arcanus = "forbidden_arcanus";
        new AppleCrateBuilder(forbidden_arcanus, "cherrywood").register();
        new AppleCrateBuilder(forbidden_arcanus, "edelwood").register();
        new AppleCrateBuilder(forbidden_arcanus, "fungyss").register();
        new AppleCrateBuilder(forbidden_arcanus, "mysterywood").register();
    }
}
