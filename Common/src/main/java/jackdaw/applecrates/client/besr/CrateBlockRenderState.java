package jackdaw.applecrates.client.besr;

import jackdaw.applecrates.container.IStackHandlerAdapter;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class CrateBlockRenderState extends BlockEntityRenderState {
    IStackHandlerAdapter stackHandler;
    boolean isUnlimitedShop;
}
