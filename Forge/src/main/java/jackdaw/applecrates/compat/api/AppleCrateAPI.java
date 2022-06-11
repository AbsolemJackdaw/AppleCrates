package jackdaw.applecrates.compat.api;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.compat.api.exception.WoodException;
import jackdaw.applecrates.util.CrateWoodType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class AppleCrateAPI {
    private static Map<CrateWoodType, ResourceLocation> pathFromWood = new HashMap<>();

    /**
     * used in datagen to generate recipes and models. you probably do not need this
     */
    public static Map<CrateWoodType, ResourceLocation> getPathFromWood() {
        return pathFromWood;
    }

    /**
     * Call in @mod-file constructor.
     *
     * @throws WoodException when the given wood paired with modid is already registered
     */
    protected static void registerForCrate(AppleCrateBuilder builder) {
        try {
            if (ModList.get().isLoaded(builder.modId)) {
                CrateWoodType wood = CrateWoodType.create(builder.modId, builder.woodName);
                if (!CrateWoodType.values().anyMatch(wood::equals)) {
                    CrateWoodType.register(wood);
                    pathFromWood.put(wood, builder.getTextureResourceLocation());
                } else throw WoodException.INSTANCE.alreadyInList(wood);
            }
        } catch (WoodException e) {
            LogUtils.getLogger().error(e.getMessage());
        }

    }

    public static class AppleCrateBuilder {

        static {
            String[] vanilla = {"oak", "spruce", "birch", "acacia", "jungle", "dark_oak", "crimson", "warped"};
            for (String wood : vanilla)
                new AppleCrateAPI.AppleCrateBuilder(wood).register();
        }

        protected String modId = "minecraft";
        protected String woodName = "";
        protected String subFolder = "";
        protected String parentFolder = "block/";
        protected String modOrMinecraftDirectory = "minecraft";
        protected String planksSuffix = "_planks";
        protected String texureName = "";

        /**
         * Call in @mod-file constructor.
         * <p>
         * If you do not plan on using the shipped generation files, you only need to use this constructor <p>
         * please do note however that you need to add recipes, blockstate, blockmodel, language file, tool tags, itemmodel and lootable yourself
         * <p>
         * <p>
         * When running the shipped datagen:<p>
         * Block registry is scanned, matching 'woodname' for recipes.
         * ModId is used to :<p>
         * a) determine namespace to find textures <p>
         * b) register a Crate with the Apple Crates mod as unique identifier, paired with 'woodName'.<p>
         * Texture is determined by modid and woodname, but location can be changed by changing texture name, subfolder- and parent-path.<p>
         * for those, use {@link #withSubfolder(String)}, {@link #withParentFolder(String)} {@link #withTextureName(String)}
         * Furthermore, it is assumed that your texture file ends with '_planks'. If this is not the case, change the suffix with {@link #withSuffix(String)}.
         */
        public AppleCrateBuilder(String modId, String woodName) {
            this(woodName);
            this.modId = modId;
            this.modOrMinecraftDirectory = modId;
        }

        protected AppleCrateBuilder(String woodName) {
            this.woodName = woodName;
            this.texureName = woodName;
        }

        /**
         * default location searched is in the block/ parent folder with no subfolder
         */
        public AppleCrateBuilder withSubfolder(String subfolder) {
            this.subFolder = subfolder;
            return this;
        }

        /**
         * default directory is 'block/'
         * change directory here
         */
        public AppleCrateBuilder withParentFolder(String parentFolder) {
            this.parentFolder = parentFolder;
            return this;
        }

        /**
         * Optional
         * for mods with optifine compat, if their textures are in the minecraft namespace
         * (very optional.)
         */
        public AppleCrateBuilder textureInMinecraftDirectory() {
            this.modOrMinecraftDirectory = "minecraft";
            return this;
        }

        /**
         * Optional
         * if your texture isn't called (woodname)'_planks', change '_planks' here
         */
        public AppleCrateBuilder withSuffix(String planksSuffix) {
            this.planksSuffix = planksSuffix;
            return this;
        }

        /**
         * Optional
         * if texture name is not the same as the wood name, set texture name here
         */
        public AppleCrateBuilder withTextureName(String textureName) {
            this.texureName = textureName;
            return this;
        }

        public ResourceLocation getTextureResourceLocation() {
            return new ResourceLocation(modOrMinecraftDirectory, parentFolder.concat(subFolder).concat(texureName).concat(planksSuffix));
        }

        public void register() {
            registerForCrate(this);
        }
    }
}
