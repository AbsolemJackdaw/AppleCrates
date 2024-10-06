package jackdaw.applecrates.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AbstractOwnerButton extends Button {
    private static final ResourceLocation OWNER_BUTTONS = new ResourceLocation(Constants.MODID, "gui/owner_buttons.png");
    private final float blitOffset;

    public AbstractOwnerButton(int x, int y, int size, float blitOffset, Component message, OnPress onPress) {
        super(x, y, size, size, message, onPress);
        this.blitOffset = blitOffset;
    }

    @Override
    public void renderButton(PoseStack poseStack, int someInt, int otherInt, float $$3) {
        RenderSystem.setShaderTexture(0, OWNER_BUTTONS);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int startY = this.isHoveredOrFocused() ? this.height : 0;
        blit(poseStack, this.x, this.y, this.getBlitOffset(), blitOffset, startY, this.width, this.height, 32, 40);
    }

}
