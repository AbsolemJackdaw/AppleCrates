package jackdaw.applecrates.client;

public class ClientConfig {
    public static CrateItemRendering crateItemRendering = CrateItemRendering.THREE;

    public static enum CrateItemRendering {
        ONE,
        THREE,
        MANY
    }
}
