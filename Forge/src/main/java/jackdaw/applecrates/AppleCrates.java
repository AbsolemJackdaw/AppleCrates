package jackdaw.applecrates;

import jackdaw.applecrates.api.AppleCrateAPI;
import jackdaw.applecrates.api.GeneralRegistry;
import jackdaw.applecrates.client.ClientConfig;
import jackdaw.applecrates.client.ForgeClientConfig;
import jackdaw.applecrates.compat.SectionProtection;
import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.container.GenericItemStackHandler;
import jackdaw.applecrates.network.CrateChannel;
import jackdaw.applecrates.network.PacketId;
import jackdaw.applecrates.network.SCrateTradeSync;
import jackdaw.applecrates.network.SGetSale;
import jackdaw.applecrates.registry.MenuSlotRegistry;
import jackdaw.applecrates.registry.OpenGuiRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class AppleCrates {
    public static final boolean GEN_VANILLA_CRATES = false;

    public AppleCrates() {
        GeneralRegistry.startup();

        if (ModList.get().isLoaded("sectionprotection"))
            SectionProtection.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modConfig);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeClientConfig.SPEC, "apple_crates_client.toml");

        MenuSlotRegistry.init();
        OpenGuiRegistry.init();

        Content.crateStock = crate -> {
            crate.crateStock = new CrateStackHandler();
            crate.priceAndSale = new GenericItemStackHandler(2);
            crate.interactable = new GenericItemStackHandler(2);
        };

        Content.packetHandler = (b) -> {
            switch (b) {
                case PacketId.SPACKET_TRADE -> CrateChannel.NETWORK.sendToServer(new SCrateTradeSync());
                case PacketId.SPACKET_SALE -> CrateChannel.NETWORK.sendToServer(new SGetSale());
            }
        };

        AppleCrateAPI.AppleCrateBuilder.registerVanilla();
        //call after mod compat so it can register new WoodTypes
        GeneralRegistry.prepareForRegistry(Constants.MODID, GeneralRegistry.BLOCKS, GeneralRegistry.ITEMS, GeneralRegistry.BLOCK_ENTITY_TYPES);
    }

    public void modConfig(ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ForgeClientConfig.SPEC)
            ClientConfig.crateItemRendering = ForgeClientConfig.configSpec.get();
    }
}
