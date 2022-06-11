package jackdaw.applecrates.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import jackdaw.applecrates.compat.api.exception.WoodException;

import java.util.Set;
import java.util.stream.Stream;

public class CrateWoodType {
    private static final Set<CrateWoodType> VALUES = new ObjectArraySet<>();

    private final ModWood wood;

    protected CrateWoodType(String name) {
        this("minecraft", name);
    }

    protected CrateWoodType(String modId, String name) {
        wood = new ModWood(modId, name);
    }

    public static CrateWoodType register(CrateWoodType woodType) {
        try {
            if (!VALUES.add(woodType))
                throw WoodException.INSTANCE.alreadyInList(woodType);
        } catch (WoodException e) {
            LogUtils.getLogger().error(e.getMessage());
        }
        return woodType;
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
