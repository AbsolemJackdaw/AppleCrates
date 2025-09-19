package jackdaw.applecrates.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

public class AddOwnerEditBox extends EditBox {
    private static final ResourceLocation BG = new ResourceLocation("textures/gui/container/gamemode_switcher.png");

    public AddOwnerEditBox(Font font, int x, int y, int w, int h, Component comp) {
        super(font, x, y, w, h, comp);
        this.setMaxLength(16); // Maximum length of a Java username. https://minecraft.wiki/w/Player#Username
        this.setVisible(false);
        this.setEditable(false);
        this.setCanLoseFocus(false);
        this.setValue("username");
        this.setFilter(ServerLoginPacketListenerImpl::isValidUsername);
    }

    public void renderBgCustom(GuiGraphics graphics) {
        if (!isVisible())
            return;
        var bg = new ResourceLocation("textures/gui/advancements/window.png");
        RenderSystem.setShaderTexture(0, bg);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        graphics.pose().translate(0, 0, 350);
        graphics.blit(bg, this.getX() - 15, this.getY() - 25, /*this.getBlitOffset(),*/ 0, 0, 80, 40, 256, 256);
        graphics.blit(bg, this.getX() + 65, this.getY() - 25, /*this.getBlitOffset(),*/ 172, 0, 80, 40, 256, 256);
        graphics.blit(bg, this.getX() - 15, this.getY() + 15, /*this.getBlitOffset(),*/ 0, 122, 80, 40, 256, 256);
        graphics.blit(bg, this.getX() + 65, this.getY() + 15, /*this.getBlitOffset(),*/ 172, 122, 80, 40, 256, 256);
        graphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() - 8, this.getY() - 18, 4210752, false);
        graphics.pose().translate(0, 0, -350);
    }
}
