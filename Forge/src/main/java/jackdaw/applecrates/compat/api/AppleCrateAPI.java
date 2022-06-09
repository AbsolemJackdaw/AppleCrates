package jackdaw.applecrates.compat.api;

import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class AppleCrateAPI {
    /***/
    private static Map<String, String> modidFromWood = new HashMap<>();
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
     */
    public static void registerForCrate(String modId, String woodName, String pathToTexture) {
        if (ModList.get().isLoaded(modId)) {
            if (!WoodType.values().anyMatch(woodType -> woodType.name().equals(woodName))) {
                WoodType.register(WoodType.create(woodName));
                modidFromWood.put(woodName, modId);
                pathFromWood.put(woodName, pathToTexture);
            }
        }
    }

    /**
     * used in datagen to generate recipes and models. you probably do not need this
     */
    public static Map<String, String> getModidFromWood() {
        return modidFromWood;
    }

    /**
     * used in datagen to generate recipes and models. you probably do not need this
     */
    public static Map<String, String> getPathFromWood() {
        return pathFromWood;
    }
}
