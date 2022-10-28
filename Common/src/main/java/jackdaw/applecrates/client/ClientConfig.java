package jackdaw.applecrates.client;

public class ClientConfig {
    public static CrateItemRendering crateItemRendering = CrateItemRendering.THREE;

    public enum CrateItemRendering {
        THREE,
        MANY
    }
}
