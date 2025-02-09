package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class AddOwnerButton extends AbstractOwnerButton implements HoverTooltipButton {

    public boolean isOn = false;
    private final Component hovertext;

    public AddOwnerButton(int x, int y, Component message, OnPress onPress) {
        super(x, y, 20, 0, Component.empty(), onPress);
        hovertext = message;
    }

    public Component getHovertext() {
        return hovertext;
    }

    @Override
    public void renderToolTip(GuiGraphics g, int x, int y) {
        g.renderTooltip(Minecraft.getInstance().font, List.of(getHovertext()), Optional.empty(), x, y);
    }

    @Override
    public void onPress() {
        this.isOn = !this.isOn;
        super.onPress();
    }
}

