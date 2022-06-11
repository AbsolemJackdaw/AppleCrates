package jackdaw.applecrates.compat.api;

import com.mojang.logging.LogUtils;
import jackdaw.applecrates.AppleCrates;
import jackdaw.applecrates.compat.api.exception.WoodException;
import jackdaw.applecrates.util.CrateWoodType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

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

    protected static void registerForCrate(AppleCrateBuilder builder) {
        //only add 'vanilla' crates to the list if we're not datagenning or skip them when we are
        if ((((!AppleCrates.VANILLAWOODSLIST.contains(builder.woodName) && FMLLoader.getLaunchHandler().isData()) || AppleCrates.GEN_VANILLA_CRATES) || !FMLLoader.getLaunchHandler().isData())) {
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
    }

    public static class AppleCrateBuilder {

        static {
            for (String wood : AppleCrates.VANILLAWOODS)
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
