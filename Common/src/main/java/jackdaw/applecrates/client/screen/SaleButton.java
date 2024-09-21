package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

abstract class SaleButton extends Button {
    public SaleButton(int x, int y, int width, Button.OnPress press) {
        super(x, y, width, 20, Component.empty(), press);
    }

    public void renderToolTip(PoseStack poseStack, int pMouseX, int pMouseY) {
        if (this.isHovered) {
            if (pMouseX < this.x + 20) {
                doRenderTip(poseStack, pMouseX, pMouseY, 0);
            } else if (pMouseX > this.x + this.width - 25) {
                doRenderTip(poseStack, pMouseX, pMouseY, 1);
            }
        }
    }

    abstract void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot);
}
