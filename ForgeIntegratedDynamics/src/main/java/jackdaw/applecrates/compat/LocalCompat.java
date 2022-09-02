package jackdaw.applecrates.compat;

import jackdaw.applecrates.api.AppleCrateAPI.AppleCrateBuilder;

public enum LocalCompat {

    INSTANCE;

    public void init() {
        String integratedDynamicsModId = "integrateddynamics";
        new AppleCrateBuilder(integratedDynamicsModId, "menril").register();
    }
}
