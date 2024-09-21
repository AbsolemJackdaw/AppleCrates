package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.CrateMenuBuyer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreenBuyer extends CommonCrateScreen<CrateMenuBuyer> {
    private static final ResourceLocation BUYER = new ResourceLocation(Constants.MODID, "gui/buyer.png");

    public CrateScreenBuyer(CrateMenuBuyer menu, Inventory pPlayerInventory, Component pTitle) {
        super(menu, pPlayerInventory, Component.translatable(pTitle.getString()));
        this.imageWidth = 176;
        this.imageHeight = 143;
        this.inventoryLabelX = titleLabelX;
        this.inventoryLabelY = titleLabelY + 40;
    }

    @Override
    public boolean isUnlimitedShop() {
        return menu.isUnlimitedShop;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
                new SaleButtonBuyer(
                        this.guiStartX + 14,
                        this.guiStartY + 19,
                        62,
                        (button) -> {
                            if (isUnlimitedShop() || !menu.outOfStock()) {
                                //TODO
                            }
                        }));
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        if (menu.outOfStock() && !isUnlimitedShop())
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
        if (!menu.savedTradeSlots.getItem(slotId).isEmpty()) {
            ItemStack saleStack = menu.savedTradeSlots.getItem(slotId);
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


    @Override
    public void onClose() {
        super.onClose();
    }

    private class SaleButtonBuyer extends SaleButton {
        public SaleButtonBuyer(int x, int y, int width, OnPress press) {
            super(x, y, width, press);
        }

        @Override
        public void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.savedTradeSlots.getItem(slot);
            if (!stack.isEmpty())
                CrateScreenBuyer.this.renderTooltip(pPoseStack, stack, pMouseX, pMouseY);
        }
    }
}
