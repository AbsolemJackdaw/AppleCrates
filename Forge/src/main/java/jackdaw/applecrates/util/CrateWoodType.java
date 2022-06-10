package jackdaw.applecrates.util;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import jackdaw.applecrates.compat.api.exception.WoodException;

import java.util.Set;
import java.util.stream.Stream;

public class CrateWoodType {
    private static final Set<CrateWoodType> VALUES = new ObjectArraySet<>();

    static {
        // WoodType.values().forEach(woodType -> register(new CrateWoodType(woodType.name())));
        //dont do this, its assumed this is vanilla, but it isn't. mods can fill it with their own woods.

        register(new CrateWoodType("oak"));
        register(new CrateWoodType("spruce"));
        register(new CrateWoodType("birch"));
        register(new CrateWoodType("acacia"));
        register(new CrateWoodType("jungle"));
        register(new CrateWoodType("dark_oak"));
        register(new CrateWoodType("crimson"));
        register(new CrateWoodType("warped"));
    }

    private final ModWood wood;

    protected CrateWoodType(String pName) {
        this("minecraft", pName);
    }

    protected CrateWoodType(String modId, String name) {
        wood = new ModWood(modId, name);
    }

    public static CrateWoodType register(CrateWoodType pWoodType) {
        if (!VALUES.add(pWoodType))
            throw new WoodException(pWoodType.modId(), pWoodType.name());
        return pWoodType;
    }

    public static Stream<CrateWoodType> values() {
        return VALUES.stream();
    }

    /**
     * Use this to create a new {@link CrateWoodType}.
     */
    public static CrateWoodType create(String modId, String name) {
        return new CrateWoodType(modId, name);
    }

    public String name() {
        return this.wood.getWoodName();
    }

    public String modId() {
        return this.wood.getModId();
    }

    public String fullName() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.modId());
        if (!this.modId().isEmpty())
            builder.append("_");
        builder.append(this.name());
        return builder.toString(); //role exclusion for the underscore separator if the namespace is minecraft/empty
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CrateWoodType that && this.name().equals(that.name()) && this.wood.equals(that.wood);
    }

    @Override
    public int hashCode() {
        return wood.hashCode();
    }
}
