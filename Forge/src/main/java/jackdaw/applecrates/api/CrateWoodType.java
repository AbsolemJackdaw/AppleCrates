package jackdaw.applecrates.api;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import jackdaw.applecrates.api.exception.WoodException;
import jackdaw.applecrates.block.blockentity.CrateBE;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class CrateWoodType {
    private static final Set<CrateWoodType> VALUES = new ObjectArraySet<>();

    private final String compatModId;
    private final String woodName;
    private final String yourModId;

    private String beName;
    private ResourceLocation resourceLocation;
    private ResourceLocation resourceLocationBe;


    protected CrateWoodType(String modId, String woodName, String yourModId) {
        this.compatModId = modId.equals("minecraft") ? "" : modId;
        this.woodName = woodName;
        this.yourModId = yourModId;

        beName = getBlockRegistryName() + "_be";
        resourceLocation = new ResourceLocation(yourModId, getBlockRegistryName());
        resourceLocationBe = new ResourceLocation(yourModId, getBeRegistryName());
    }

    public String getBlockRegistryName() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.compatModId());
        if (!this.compatModId().isEmpty())
            builder.append("_");
        builder.append(this.name());
        builder.append("_crate");
        return builder.toString(); //role exclusion for the underscore separator if the namespace is minecraft/empty
    }

    public String getBeRegistryName() {
        return beName;
    }

    public String compatModId() {
        return this.compatModId;
    }

    public String name() {
        return this.woodName;
    }

    /**
     * @throws WoodException : when a duplicate is present in the registry
     */
    public static void register(CrateWoodType woodType) {
        try {
            if (!VALUES.add(woodType)) {
                throw WoodException.INSTANCE.alreadyInList(woodType);
            }
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
    public static CrateWoodType create(String compatModId, String name, String yourModId) {
        return new CrateWoodType(compatModId, name, yourModId);
    }

    public static Block getBlock(CrateWoodType type) {
        return Registry.BLOCK.get(type.getFullRegistryResLoc());
    }

    /**
     * yourmodid : modid + woodname + _crate
     */
    public ResourceLocation getFullRegistryResLoc() {
        return resourceLocation;
    }

    public static BlockEntityType<CrateBE> getBlockEntityType(CrateWoodType type) {
        return (BlockEntityType<CrateBE>) Registry.BLOCK_ENTITY_TYPE.get(type.getFullBeRegistryResLoc());
    }

    public ResourceLocation getFullBeRegistryResLoc() {
        return resourceLocationBe;
    }

    public boolean isFrom(String modId) {
        return this.getYourModId().equals(modId);
    }

    public String getYourModId() {
        return yourModId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compatModId, woodName, yourModId);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CrateWoodType that && this.name().equals(that.name()) && this.compatModId().equals(that.compatModId()) && this.getYourModId().equals(that.getYourModId());
    }
}
