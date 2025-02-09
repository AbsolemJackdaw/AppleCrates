package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface HoverTooltipButton {
    Component getHovertext();

    void renderToolTip(GuiGraphics graphics, int pMouseX, int pMouseY);
}
