package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.CrateMenuBuyer;
import jackdaw.applecrates.network.CrateChannel;
import jackdaw.applecrates.network.SGetSale;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreenBuyer extends AbstractContainerScreen<CrateMenuBuyer> {
    private static final ResourceLocation BUYER = new ResourceLocation(Constants.MODID, "gui/buyer.png");
    private static final ResourceLocation VILLAGER_UI = new ResourceLocation("textures/gui/container/villager2.png");
    private final boolean isUnlimitedShop;
    private int guiStartX;
    private int guiStartY;

    public CrateScreenBuyer(CrateMenuBuyer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable(title.getString()));
        this.imageWidth = 176;
        this.imageHeight = 143;
        this.inventoryLabelX = titleLabelX;
        this.inventoryLabelY = titleLabelY + 40;
        this.isUnlimitedShop = menu.isUnlimitedShop;
    }

    @Override
    protected void init() {
        super.init();
        this.guiStartX = (this.width - this.imageWidth) / 2;
        this.guiStartY = (this.height - this.imageHeight) / 2;
        addRenderableWidget(
                new SaleButtonBuyer(
                        guiStartX + 14,
                        guiStartY + 19,
                        62,
                        (button) -> {
                            if (isUnlimitedShop || !menu.outOfStock())
                                CrateChannel.NETWORK.sendToServer(new SGetSale());
                        }));
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        if (menu.outOfStock() && !isUnlimitedShop)
            blit(poseStack, guiStartX + 40, guiStartY + 24, this.getBlitOffset(), 25.0F, 171.0F, 10, 9, 512, 256);
        else
            blit(poseStack, guiStartX + 46, guiStartY + 24, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(0, guiStartX, guiStartY);
        renderTrade(1, guiStartX, guiStartY);

        this.renderTooltip(poseStack, pMouseX, pMouseY);
    }


    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if (!menu.savedTradeSlots.getStackInSlot(slotId).isEmpty()) {
            ItemStack saleStack = menu.savedTradeSlots.getStackInSlot(slotId);
            int xo = slotId == 0 ? 14 + 2 : 75 - 16 - 2;
            int yo = 20;
            this.itemRenderer.renderAndDecorateFakeItem(saleStack, x + xo, y + yo);
            this.itemRenderer.renderGuiItemDecorations(this.font, saleStack, x + xo, y + yo);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BUYER);
        blit(pPoseStack, guiStartX, guiStartY, this.getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    private class SaleButtonBuyer extends SaleButton {
        public SaleButtonBuyer(int x, int y, int width, OnPress press) {
            super(x, y, width, press);
        }

        @Override
        void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.savedTradeSlots.getStackInSlot(slot);
            if (!stack.isEmpty())
                CrateScreenBuyer.this.renderTooltip(pPoseStack, stack, pMouseX, pMouseY);
        }
    }
}
