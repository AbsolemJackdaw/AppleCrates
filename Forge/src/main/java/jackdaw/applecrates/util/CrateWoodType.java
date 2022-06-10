package jackdaw.applecrates.util;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import jackdaw.applecrates.compat.api.exception.WoodException;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Set;
import java.util.stream.Stream;

public class CrateWoodType {
    private static final Set<CrateWoodType> VALUES = new ObjectArraySet<>();

    static {
        WoodType.values().forEach(woodType -> register(new CrateWoodType(woodType.name())));
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
        return this.modId() + "_" + this.name();
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
