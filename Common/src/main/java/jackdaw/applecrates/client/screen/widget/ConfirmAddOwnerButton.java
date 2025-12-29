package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ConfirmAddOwnerButton extends AbstractOwnerButton implements HoverTooltipButton {

    private final Component hoverText;

    public ConfirmAddOwnerButton(int x, int y, Component message, OnPress onPress) {
        super(x, y, 12, 20, Component.empty(), onPress);
        this.visible = false;
        this.hoverText = message;
    }

    @Override
    public Component getHovertext() {
        return hoverText;
    }

    @Override
    public void renderToolTip(GuiGraphics g, int x, int y) {
        g.setComponentTooltipForNextFrame(Minecraft.getInstance().font, List.of(getHovertext()), x, y);
    }
}
