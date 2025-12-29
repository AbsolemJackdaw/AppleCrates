package jackdaw.applecrates.client.screen.widget;

import jackdaw.applecrates.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbstractOwnerButton extends Button {
    private static final ResourceLocation OWNER_BUTTONS = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "owner_buttons");
    private final int u;

    public AbstractOwnerButton(int x, int y, int size, int blitOffset, Component message, OnPress onPress) {
        super(x, y, size, size, message, onPress, DEFAULT_NARRATION);
        this.u = blitOffset;
    }


    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float f) {
        super.renderWidget(g, mouseX, mouseY, f);

        int v = this.isHoveredOrFocused() ? this.height : 0;
        g.blitSprite(RenderPipelines.GUI_TEXTURED, OWNER_BUTTONS, 32, 40, u, v, this.getX(), this.getY(), this.width, this.height);
    }

}
