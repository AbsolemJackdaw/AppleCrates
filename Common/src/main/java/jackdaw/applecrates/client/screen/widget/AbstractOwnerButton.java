package jackdaw.applecrates.client.screen.widget;

import jackdaw.applecrates.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AbstractOwnerButton extends Button {
    private static final Identifier OWNER_BUTTONS = Identifier.fromNamespaceAndPath(Constants.MODID, "owner_buttons");
    private final int u;

    public AbstractOwnerButton(int x, int y, int size, int blitOffset, Component message, OnPress onPress) {
        super(x, y, size, size, message, onPress, DEFAULT_NARRATION);
        this.u = blitOffset;
    }

    @Override
    protected void renderContents(GuiGraphics g, int i, int i1, float f) {
        int v = this.isHoveredOrFocused() ? this.height : 0;
        g.blitSprite(RenderPipelines.GUI_TEXTURED, OWNER_BUTTONS, 32, 40, u, v, this.getX(), this.getY(), this.width, this.height);

    }

}
