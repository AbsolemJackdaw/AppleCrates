package jackdaw.applecrates.container.inventory;


import net.neoforged.neoforge.items.ItemStackHandler;

public class GenericStackHandler extends ItemStackHandler implements IGenericInventory {
    public GenericStackHandler(int size) {
        super(size);
    }
}
