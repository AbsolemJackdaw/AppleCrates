package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.client.screen.widget.HoverTooltipButton;
import jackdaw.applecrates.client.screen.widget.SaleButton;
import jackdaw.applecrates.container.CrateMenuBuyer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreenBuyer extends CrateScreen<CrateMenuBuyer> {
    private static final ResourceLocation BUYER = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "gui/buyer.png");

    public CrateScreenBuyer(CrateMenuBuyer menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable(title.getString()));
        this.imageWidth = 176;
        this.imageHeight = 143;
        this.inventoryLabelX = titleLabelX;
        this.inventoryLabelY = titleLabelY + 40;
    }

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
                        (button) -> {
                            if (isUnlimitedShop() || !menu.outOfStock())
                                Content.buyerGuiButton.accept();
                        })
        );
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        if (menu.outOfStock() && !isUnlimitedShop())
            graphics.blit(VILLAGER_UI, guiStartX + 40, guiStartY + 24, /*this.getBlitOffset(),*/ 25.0F, 171.0F, 10, 9, 512, 256);
        else
            graphics.blit(VILLAGER_UI, guiStartX + 46, guiStartY + 24, /*this.getBlitOffset(),*/ 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(graphics, 0, guiStartX, guiStartY);
        renderTrade(graphics, 1, guiStartX, guiStartY);

        this.renderTooltip(graphics, pMouseX, pMouseY);
    }

    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(GuiGraphics graphics, int slotId, int x, int y) {
        if (!menu.adapter.getSavedTradeSlotsItem(slotId).isEmpty()) {
            ItemStack saleStack = menu.adapter.getSavedTradeSlotsItem(slotId);
            int xo = slotId == 0 ? 16 : 57;
            int yo = 20;
            graphics.renderFakeItem(saleStack, x + xo, y + yo);
            graphics.renderItemDecorations(this.font, saleStack, x + xo, y + yo);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BUYER);
        graphics.blit(BUYER, guiStartX, guiStartY, /*this.getBlitOffset(),*/ 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderTooltip(GuiGraphics g, int x, int y) {
        super.renderTooltip(g, x, y);
        for (var ren : children()) {
            if (ren instanceof HoverTooltipButton htb && ren instanceof AbstractWidget widget) {
                if (widget.active && widget.isHovered() && widget.visible) {
                    htb.renderToolTip(g, x, y);
                }
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private class SaleButtonBuyer extends SaleButton {
        public SaleButtonBuyer(int x, int y, OnPress press) {
            super(x, y, Component.translatable("crate.button.sale.buyer"), press);
        }

        @Override
        public void doRenderTip(GuiGraphics graphics, int pMouseX, int pMouseY, int slot) {
        }

        @Override
        public void renderToolTip(GuiGraphics graphics, int pMouseX, int pMouseY) {
            ItemStack pay = menu.adapter.getSavedTradeSlotsItem(0);
            ItemStack result = menu.adapter.getSavedTradeSlotsItem(1);

            if (pMouseX < this.getX() + 20 && !pay.isEmpty()) {
                graphics.renderTooltip(CrateScreenBuyer.this.font, CrateScreenBuyer.this.getTooltipFromContainerItem(pay), pay.getTooltipImage(), pMouseX, pMouseY);
            } else if (pMouseX > this.getX() + this.width - 25 && !result.isEmpty()) {
                graphics.renderTooltip(CrateScreenBuyer.this.font, CrateScreenBuyer.this.getTooltipFromContainerItem(result), result.getTooltipImage(), pMouseX, pMouseY);
            }
        }
    }
}
