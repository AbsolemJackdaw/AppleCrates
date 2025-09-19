package jackdaw.applecrates.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import jackdaw.applecrates.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbstractOwnerButton extends Button {
    private static final ResourceLocation OWNER_BUTTONS = new ResourceLocation(Constants.MODID, "gui/owner_buttons.png");
    private final float blitOffset;

    public AbstractOwnerButton(int x, int y, int size, float blitOffset, Component message, OnPress onPress) {
        super(x, y, size, size, message, onPress, DEFAULT_NARRATION);
        this.blitOffset = blitOffset;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float f) {
        super.render(g, mouseX, mouseY, f);

        RenderSystem.setShaderTexture(0, OWNER_BUTTONS);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int startY = this.isHoveredOrFocused() ? this.height : 0;
        g.blit(OWNER_BUTTONS, this.getX(), this.getY(), /*his.getBlitOffset(),*/ blitOffset, startY, this.width, this.height, 32, 40);
    }

}
