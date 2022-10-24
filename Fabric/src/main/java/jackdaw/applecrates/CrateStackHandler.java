package jackdaw.applecrates;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CrateStackHandler extends SimpleContainer {

    public static final String TAGSTOCK = "stocked";
    private final Map<Item, Integer> itemCountCache = new HashMap<>();

    public CrateStackHandler() {
        super(30);
    }

    public int getCountOfItem(Item item) {
        return this.itemCountCache.computeIfAbsent(item, $ -> {
            int count = 0;
            for (int i = 0; i < this.getContainerSize(); i++) {
                var stack = this.getItem(i);
                if (stack.is(item)) {
                    count += stack.getCount();
                }
            }
            return count;
        });
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return slot != 29;
    }


    public boolean updateStackInPaymentSlot(ItemStack payment, boolean isUnlimitedShop) {
        if (isUnlimitedShop)
            return true;

        ItemStack prepPay = payment.copy();

        if (getItem(29).isEmpty()) {
            prepPay.setCount(1);
            setItem(29, prepPay);
        }
        if (!ItemStack.isSame(payment, getItem(29)))
            return false;
        ItemStack prepXchange = getItem(29).copy();
        CompoundTag tag = prepXchange.getOrCreateTag();
        if (tag.contains(TAGSTOCK)) {
            tag.putInt(TAGSTOCK, tag.getInt(TAGSTOCK) + payment.getCount());
        } else {
            tag.putInt(TAGSTOCK, payment.getCount());
        }
        setItem(29, prepXchange);
        return true;
    }

    public void deserializeNBT(CompoundTag nbt) {
        //setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : items.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < items.size()) {
                items.set(slot, ItemStack.of(itemTags));
            }
        }
    }

    public CompoundTag serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < getContainerSize(); i++) {
            if (!items.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                items.get(i).save(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", items.size());
        return nbt;
    }
}
