package jackdaw.applecrates.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;

public class AddOwnerEditBox extends EditBox {
    private static final ResourceLocation BG = ResourceLocation.withDefaultNamespace("textures/gui/advancements/window.png");

    public AddOwnerEditBox(Font font, int x, int y, int w, int h, Component comp) {
        super(font, x, y, w, h, comp);
        this.setMaxLength(16); // Maximum length of a Java username. https://minecraft.wiki/w/Player#Username
        this.setVisible(false);
        this.setEditable(false);
        this.setCanLoseFocus(false);
        this.setValue("username");
        this.setFilter(StringUtil::isValidPlayerName);
    }

    public void renderBgCustom(GuiGraphics graphics) {
        if (!isVisible())
            return;
        graphics.nextStratum();
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG, this.getX() - 15, this.getY() - 25, /*this.getBlitOffset(),*/ 0, 0, 80, 40, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG, this.getX() + 65, this.getY() - 25, /*this.getBlitOffset(),*/ 172, 0, 80, 40, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG, this.getX() - 15, this.getY() + 15, /*this.getBlitOffset(),*/ 0, 122, 80, 40, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG, this.getX() + 65, this.getY() + 15, /*this.getBlitOffset(),*/ 172, 122, 80, 40, 256, 256);
        graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() - 8, this.getY() - 18, 4210752, false);
    }
}
