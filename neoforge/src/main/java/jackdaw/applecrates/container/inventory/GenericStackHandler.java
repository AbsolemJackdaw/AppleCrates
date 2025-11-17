package jackdaw.applecrates.container.inventory;


import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class GenericStackHandler extends ItemStacksResourceHandler implements IGenericInventory {
    public GenericStackHandler(int size) {
        super(size);
    }
}
