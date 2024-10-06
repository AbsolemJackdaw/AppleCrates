package jackdaw.applecrates;

import jackdaw.applecrates.client.IClientConfig;
import jackdaw.applecrates.container.IMenuSlots;
import jackdaw.applecrates.container.inventory.IMoneyPatch;
import jackdaw.applecrates.network.IPacketOnButtonPress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class Content {

    public static IMenuSlots menuSlots;
    public static IClientConfig clientConfig;
    public static Consumer<String> addOwnerButton;
    public static IPacketOnButtonPress ownerGuiButton;
    public static IPacketOnButtonPress buyerGuiButton;
    public static IMoneyPatch moneyPatch = nbt -> {
        //patch from old 30 save slot to new 31 save slot
        if (nbt != null && nbt.contains("Size") && nbt.getInt("Size") < Constants.TOTALCRATESLOTS && nbt.contains("Items")) {
            //update size to new inflated 31 instead of 30
            nbt.putInt("Size", Constants.TOTALCRATESLOTS);
            //get saved slots from crate's nbt
            ListTag tagList = nbt.getList("Items", 10);
            //get last saved itemstack
            var lastTag = tagList.getCompound(tagList.size() - 1);
            //check if the last saved tag is the moneyslot.
            if (lastTag.getInt("Slot") == Constants.TOTALCRATESTOCKLOTS - 1) {
                //move the moneyslot to the last slot of the new stackhandler
                lastTag.putInt("Slot", Constants.TOTALCRATESTOCKLOTS);

                try {
                    ItemStack stack = ItemStack.of(lastTag);
                    var stackTag = stack.getOrCreateTag();
                    //move from old tag name to new tag name
                    if (stackTag.contains("stocked")) {
                        var amount = stackTag.getInt("stocked");
                        stackTag.putInt(Constants.TAGSTOCK, amount);
                        stackTag.remove("stocked");
                    }
                } catch (Exception e) {
                    System.err.println("couldnt parse money slot after attempting to save it");
                }
            }
        }
    };
}
