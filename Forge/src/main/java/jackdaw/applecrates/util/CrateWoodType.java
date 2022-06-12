package jackdaw.applecrates.util;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import jackdaw.applecrates.api.exception.WoodException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class CrateWoodType {
    private static final Set<CrateWoodType> VALUES = new ObjectArraySet<>();

    private final String modId;
    private final String woodName;

    protected CrateWoodType(String modId, String woodName) {
        this.modId = modId.equals("minecraft") ? "" : modId;
        this.woodName = woodName;
    }

    /**
     * @throws WoodException : when a duplicate is present in the registry
     */
    public static void register(CrateWoodType woodType) {
        try {
            if (!VALUES.add(woodType))
                throw WoodException.INSTANCE.alreadyInList(woodType);
        } catch (WoodException e) {
            LogUtils.getLogger().error(e.getMessage());
        }
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
        return this.woodName;
    }

    public String modId() {
        return this.modId;
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
        return o instanceof CrateWoodType that && this.name().equals(that.name()) && this.modId().equals(that.modId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(modId, woodName);
    }
}
