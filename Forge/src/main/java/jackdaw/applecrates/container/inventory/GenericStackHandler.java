package jackdaw.applecrates.container.inventory;

import net.minecraftforge.items.ItemStackHandler;

public class GenericStackHandler extends ItemStackHandler implements IGenericInventory {
    public GenericStackHandler(int size) {
        super(size);
    }
}
