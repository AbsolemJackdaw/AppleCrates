package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class SaleButton extends Button implements HoverTooltipButton {
    protected final Component narratorText;

    public SaleButton(int x, int y, Component message, Button.OnPress press) {
        super(x, y, 62, 20, Component.empty(), press, DEFAULT_NARRATION);
        this.narratorText = message;
    }

    @Override
    public Component getHovertext() {
        return narratorText;
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return wrapDefaultNarrationMessage(narratorText);
    }

    public void renderToolTip(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    public abstract void doRenderTip(GuiGraphics graphics, int pMouseX, int pMouseY, int slot);
}
