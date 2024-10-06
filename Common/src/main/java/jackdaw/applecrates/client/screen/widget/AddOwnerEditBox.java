package jackdaw.applecrates.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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

    @Override
    public void renderBg(PoseStack stack, Minecraft mc, int x, int y) {
        if (!isVisible())
            return;
        var bg = new ResourceLocation("textures/gui/advancements/window.png");
        RenderSystem.setShaderTexture(0, bg);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        stack.translate(0, 0, 350);
        blit(stack, this.x - 15, this.y - 25, this.getBlitOffset(), 0, 0, 80, 40, 256, 256);
        blit(stack, this.x + 65, this.y - 25, this.getBlitOffset(), 172, 0, 80, 40, 256, 256);
        blit(stack, this.x - 15, this.y + 15, this.getBlitOffset(), 0, 122, 80, 40, 256, 256);
        blit(stack, this.x + 65, this.y + 15, this.getBlitOffset(), 172, 122, 80, 40, 256, 256);
        mc.font.draw(stack, Component.translatable("crate.add.owner"), this.x - 8, this.y - 18, 4210752);
        stack.translate(0, 0, -350);
    }
}
