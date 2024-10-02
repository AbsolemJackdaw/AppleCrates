package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public abstract class SaleButton extends Button {
    protected final Component narratorText;
    public SaleButton(int x, int y, int width, Component message, Button.OnPress press) {
        super(x, y, width, 20, Component.empty(), press);
        this.narratorText = message;
    }

    @NotNull
    @Override
    protected MutableComponent createNarrationMessage() {
        return wrapDefaultNarrationMessage(narratorText);
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

    public abstract void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot);
}
