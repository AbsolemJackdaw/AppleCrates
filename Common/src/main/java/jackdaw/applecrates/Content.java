package jackdaw.applecrates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import jackdaw.applecrates.client.IClientConfig;
import jackdaw.applecrates.container.IMenuSlots;
import jackdaw.applecrates.container.StackHandlerAdapter;
import jackdaw.applecrates.container.slot.SlotCrateStock;
import jackdaw.applecrates.container.slot.SlotPriceSale;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import jackdaw.applecrates.network.IPacketOnButtonPress;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Content {

    public static final StreamCodec<ByteBuf, CoinCounter> NETWORK_COIN_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CoinCounter::count,
            CoinCounter::new
    );
    public static final Codec<CoinCounter> DATA_COIN_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("count").forGetter(CoinCounter::count)
            ).apply(instance, CoinCounter::new)
    );

    public static DataComponentType<CoinCounter> coinCounter;
    public static IMenuSlots menuSlots;
    public static IClientConfig clientConfig;
    public static Consumer<String> addOwnerButton;
    public static IPacketOnButtonPress ownerGuiButton;
    public static IPacketOnButtonPress buyerGuiButton;
//    public static IMoneyPatch moneyPatch = nbt -> {
//        //patch from old 30 save slot to new 31 save slot
//        if (nbt != null && nbt.contains("Size") && nbt.getInt("Size") < Constants.TOTALCRATESLOTS && nbt.contains("Items")) {
//            //update size to new inflated 31 instead of 30
//            nbt.putInt("Size", Constants.TOTALCRATESLOTS);
//            //get saved slots from crate's nbt
//            ListTag tagList = nbt.getList("Items", 10);
//            //get last saved itemstack
//            var lastTag = tagList.getCompound(tagList.size() - 1);
//            //check if the last saved tag is the moneyslot.
//            if (lastTag.getInt("Slot") == Constants.TOTALCRATESTOCKLOTS - 1) {
//                //move the moneyslot to the last slot of the new stackhandler
//                lastTag.putInt("Slot", Constants.TOTALCRATESTOCKLOTS);
//
//                try {
//                    ItemStack stack = ItemStack.of(lastTag);
//                    var stackTag = stack.getOrCreateTag();
//                    //move from old tag name to new tag name
//                    if (stackTag.contains("stocked")) {
//                        var amount = stackTag.getInt("stocked");
//                        stackTag.putInt(Constants.TAGSTOCK, amount);
//                        stackTag.remove("stocked");
//                    }
//                } catch (Exception e) {
//                    System.err.println("couldnt parse money slot after attempting to save it");
//                }
//            }
//        }
//    };

    public static void populateMenus() {
        menuSlots = menu -> {
            if (!(menu.adapter instanceof StackHandlerAdapter stackHandlerAdapter))
                return;
            menu.addSlot(new Slot(stackHandlerAdapter.interactableTrades, 0, menu.isOwner() ? 10 : 102, menu.isOwner() ? 76 : 21));
            menu.addSlot(new Slot(stackHandlerAdapter.interactableTrades, 1, menu.isOwner() ? 46 : 142, menu.isOwner() ? 76 : 21) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return menu.isOwner();
                }
            });

            menu.addSlot(new SlotPriceSale(stackHandlerAdapter.savedTrades, 0));//buyer pays
            menu.addSlot(new SlotPriceSale(stackHandlerAdapter.savedTrades, 1));//buyer gets

            for (int y = 0; y < 3; y++) //crate stock
                for (int x = 0; x < 10; x++)
                    menu.addSlot(new SlotCrateStock(stackHandlerAdapter.crateStock, y * 10 + x, x * 18 + 10, y * 18 + 17, menu.isOwner()));
            menu.addSlot(new SlotCrateStock(stackHandlerAdapter.crateStock, Constants.TOTALCRATESTOCKLOTS, 172, 76, menu.isOwner()));
        };
    }
}
