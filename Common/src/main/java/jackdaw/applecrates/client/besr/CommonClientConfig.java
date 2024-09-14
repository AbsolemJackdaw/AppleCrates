package jackdaw.applecrates.client.besr;

public class CommonClientConfig {
    public static CrateItemRendering crateItemRendering = CrateItemRendering.THREE;

    public static enum CrateItemRendering {
        ONE,
        THREE,
        MANY
    }
}
