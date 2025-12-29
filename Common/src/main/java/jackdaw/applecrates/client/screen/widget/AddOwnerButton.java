package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

import java.util.List;

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
        g.setComponentTooltipForNextFrame(Minecraft.getInstance().font, List.of(getHovertext()), x, y);
    }

    @Override
    public void onPress(InputWithModifiers input) {
        this.isOn = !this.isOn;
        super.onPress(input);
    }
}

