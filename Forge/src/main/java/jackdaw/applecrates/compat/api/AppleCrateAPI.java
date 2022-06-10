package jackdaw.applecrates.compat.api;

import jackdaw.applecrates.compat.api.exception.WoodException;
import jackdaw.applecrates.util.CrateWoodType;
import jackdaw.applecrates.util.ModWood;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppleCrateAPI {
    public static boolean isDev = true;
    /***/
    private static List<ModWood> modWoods = new ArrayList<>();
    private static Map<String, String> pathFromWood = new HashMap<>();

    /**
     * Call in @mod-file constructor to initialize first before anything else;
     *
     * @param modId         : the modid where the origin planks come from
     * @param woodName      : the name of your wood, excludes the '_planks' suffix.
     *                      example : oak, birch, jungle,warped;
     * @param pathToTexture : if the texture isn't in the default texture/block/image.png directory,
     *                      add the extra path options here. pass empty string if none_applicable
     *                      example : "woods/apple/" for a path that is "texture/block/woods/apple/image.png"
     * @throws WoodException when the given wood paired with modid is already registered
     */
    public static void registerForCrate(@Nonnull String modId, @Nonnull String woodName, @Nonnull String pathToTexture) {
        if (ModList.get().isLoaded(modId) || isDev) {
            if (!CrateWoodType.values().anyMatch(woodType -> woodType.name().equals(woodName) && woodType.modId().equals(modId))) {
                CrateWoodType.register(CrateWoodType.create(modId, woodName));
                if (!pathToTexture.isBlank()) pathFromWood.put(woodName, pathToTexture);
            } else throw new WoodException(modId, woodName);
        }
    }

    /**
     * Call in @mod-file constructor.
     * <p>
     * If your block uses another path then block/image.png for their texture, please refer to {@link #registerForCrate(String, String, String) }
     * <p>
     *
     * @param modId    : the modid where the origin planks come from
     * @param woodName : the name of your wood, excludes the '_planks' suffix.
     *                 example : oak, birch, jungle,warped;
     * @throws WoodException when the given wood paired with modid is already registered
     */
    public static void registerForCrate(@Nonnull String modId, @Nonnull String woodName) {
        registerForCrate(modId, woodName, "");
    }


    /**
     * used in datagen to generate recipes and models. you probably do not need this
     */
    public static Map<String, String> getPathFromWood() {
        return pathFromWood;
    }
}
