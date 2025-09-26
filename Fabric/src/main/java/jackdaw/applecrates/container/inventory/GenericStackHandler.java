package jackdaw.applecrates.container.inventory;

import net.minecraft.world.SimpleContainer;

public class GenericStackHandler extends SimpleContainer implements IGenericInventory {

    public GenericStackHandler(int i) {
        super(i);
    }

//    @Override
//    public ListTag createTag(HolderLookup.Provider levelRegistry) {
//        CompoundTag nbt = new CompoundTag();
//        ContainerHelper.saveAllItems(nbt, items, levelRegistry);
//        return super.createTag(levelRegistry);
//    }
//
//    @Override
//    public void fromTag(ListTag tag, HolderLookup.Provider levelRegistry) {
//        super.fromTag(tag, levelRegistry);
//        ContainerHelper.loadAllItems(tag, items, levelRegistry);
//    }
}
