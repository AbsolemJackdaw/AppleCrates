package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class SaleButton extends Button {
    public SaleButton(int x, int y, int width, Button.OnPress press, CreateNarration narration) {
        super(x, y, width, 20, Component.empty(), press, narration);
    }

    @Override
    public @Nullable Tooltip getTooltip() {
        return super.getTooltip();
    }

    //    public void renderToolTip(PoseStack poseStack, int pMouseX, int pMouseY) {
//        if (this.isHovered) {
//            if (pMouseX < this.x + 20) {
//                doRenderTip(poseStack, pMouseX, pMouseY, 0);
//            } else if (pMouseX > this.x + this.width - 25) {
//                doRenderTip(poseStack, pMouseX, pMouseY, 1);
//            }
//        }
//    }

    public abstract void doRenderTip(GuiGraphics pPoseStack, int pMouseX, int pMouseY, int slot);
}
