package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.client.screen.widget.*;
import jackdaw.applecrates.container.CrateMenuOwner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class CrateScreenOwner extends CrateScreen<CrateMenuOwner> {
    private static final ResourceLocation OWNER = new ResourceLocation(Constants.MODID, "gui/owner.png");

    private AddOwnerEditBox inputField;
    private ConfirmAddOwnerButton confirmAddOwnerButton;
    private AddOwnerButton addOwnerButton;
    private boolean cancelFocusChange = false;

    public CrateScreenOwner(CrateMenuOwner menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable(title.getString()));
        this.imageWidth = 198;
        this.imageHeight = 194;
        this.inventoryLabelX = titleLabelX + 8;
        this.inventoryLabelY = titleLabelY + 90;
    }

    @Override
    public boolean isUnlimitedShop() {
        return menu.isUnlimitedShop;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(
                new SaleButtonOwner(
                        guiStartX + 70,
                        guiStartY + 74,
                        (button) -> {
                            if (!(menu.adapter.getInteractableTradeItem(0).isEmpty() && menu.adapter.getInteractableTradeItem(1).isEmpty())
                                    && (menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).isEmpty() || isSamePayout())) //do not allow a change if the payout slot isn't empty or the same item as the current one
                                Content.ownerGuiButton.accept();//handles switching up items and giving back to player
                        }));

        this.inputField = addWidget( // we manually render this above everything else
                new AddOwnerEditBox(
                        this.font,
                        this.guiStartX + 33,
                        this.guiStartY + 37,
                        113, 12,
                        Component.translatable("crate.add.owner")
                ));

        this.confirmAddOwnerButton = addWidget( // we manually render this above everything else
                new ConfirmAddOwnerButton(
                        this.guiStartX + 151,
                        this.guiStartY + 37,
                        Component.translatable("crate.add.owner.confirm"),
                        button -> {
                            if (!this.inputField.getValue().isBlank())
                                Content.addOwnerButton.accept(this.inputField.getValue());
                            addOwnerButton.isOn = !addOwnerButton.isOn;
                            toggleOverlay();
                        }
                )
        );

        this.addOwnerButton = addRenderableWidget(
                new AddOwnerButton(
                        guiStartX + 138,
                        guiStartY + 74,
                        Component.translatable("crate.add.owner"),
                        button -> toggleOverlay()));
    }

    private void toggleOverlay() {
        boolean isNowOn = addOwnerButton.isOn;
        this.inputField.setEditable(isNowOn);
        this.inputField.setVisible(isNowOn);
        if (isNowOn) {
            this.setFocused(this.inputField);
            this.cancelFocusChange = true;
        }
        this.inputField.setFocused(isNowOn);
        this.confirmAddOwnerButton.visible = isNowOn;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener $$0) { // This method fires after a buttons onClick method to set focus to the button
        if (cancelFocusChange) { // We cancel that under certain conditions so that focus can be transferred to the username text box upon clicking the add owner button, which is just nice.
            cancelFocusChange = false;
            return;
        }
        super.setFocused($$0);
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        // 150, 49 to 329, 125
        if (this.inputField.isVisible() && super.isHovering(10, 15, 180, 54, mouseX, mouseY))
            return false;
        else
            return super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    protected boolean isSamePayout() {
        ItemStack payout = menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).copy();
        ItemStack give = menu.adapter.getInteractableTradeItem(0).copy();
        if (give.isEmpty() || payout.isEmpty())
            return true;

        if (payout.hasTag() && payout.getTag().contains(Constants.TAGSTOCK)) {
            payout.removeTagKey(Constants.TAGSTOCK);
        }
        return ItemStack.isSameItemSameTags(payout, give);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        if (!(menu.adapter.getSavedTradeSlotsItem(0).isEmpty() && menu.adapter.getSavedTradeSlotsItem(1).isEmpty()) && menu.adapter.getInteractableTradeItem(0).isEmpty() && menu.adapter.getInteractableTradeItem(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        if (!isSamePayout())
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        graphics.blit(VILLAGER_UI, guiStartX + 100, guiStartY + 79, /*this.getBlitOffset(),*/ 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(graphics, 0, guiStartX, guiStartY);
        renderTrade(graphics, 1, guiStartX, guiStartY);


        if (!menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) { // moneyslot
            ItemStack inSlot = menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS); // moneyslot
            if (inSlot.getOrCreateTag().contains(Constants.TAGSTOCK)) {
                int pay = inSlot.getOrCreateTag().getInt(Constants.TAGSTOCK);
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(pay);
            }
        }

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 350); // manually render these above everything else.
        this.inputField.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (this.confirmAddOwnerButton.visible)
            this.confirmAddOwnerButton.render(graphics, pMouseX, pMouseY, pPartialTick);
        graphics.pose().popPose();

        this.renderTooltip(graphics, pMouseX, pMouseY);
    }

    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(GuiGraphics graphics, int slotId, int x, int y) {
        if ((!menu.adapter.getInteractableTradeItem(slotId).isEmpty()) || !menu.adapter.getSavedTradeSlotsItem(slotId).isEmpty()) {
            ItemStack saleStack = !menu.adapter.getInteractableTradeItem(slotId).isEmpty() ? menu.adapter.getInteractableTradeItem(slotId) : menu.adapter.getSavedTradeSlotsItem(slotId);
            int xo = slotId == 0 ? 72 : 72 + 41;
            int yo = 75;
            graphics.renderFakeItem(saleStack, x + xo, y + yo);
            graphics.renderItemDecorations(this.font, saleStack, x + xo, y + yo);
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OWNER);
        graphics.blit(OWNER, guiStartX, guiStartY, /*this.getBlitOffset(),*/ 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        this.inputField.renderBgCustom(graphics);

    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.inputField.isVisible())
            this.inputField.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int $$1, int $$2) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.inputField.keyPressed(keyCode, $$1, $$2) || this.inputField.canConsumeInput() || super.keyPressed(keyCode, $$1, $$2);
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

    private class SaleButtonOwner extends SaleButton {
        private static final Component CANNOT_SWITCH = Component.translatable("cannot.switch.trade");

        public SaleButtonOwner(int x, int y, OnPress press) {
            super(x, y, Component.translatable("crate.button.sale.owner"), press);
        }

        @Override
        public void doRenderTip(GuiGraphics graphics, int pMouseX, int pMouseY, int slot) {
        }

        @Override
        public void renderToolTip(GuiGraphics graphics, int pMouseX, int pMouseY) {
            ItemStack pay = menu.adapter.getSavedTradeSlotsItem(0);
            ItemStack result = menu.adapter.getSavedTradeSlotsItem(1);

            if (pMouseX < this.getX() + 20 && !pay.isEmpty()) {
                graphics.renderTooltip(CrateScreenOwner.this.font, CrateScreenOwner.this.getTooltipFromContainerItem(pay), pay.getTooltipImage(), pMouseX, pMouseY);
            } else if (pMouseX > this.getX() + this.width - 25 && !result.isEmpty()) {
                graphics.renderTooltip(CrateScreenOwner.this.font, CrateScreenOwner.this.getTooltipFromContainerItem(result), result.getTooltipImage(), pMouseX, pMouseY);
            } else {
                if (!isSamePayout())
                    graphics.renderTooltip(CrateScreenOwner.this.font, List.of(CANNOT_SWITCH), Optional.empty(), pMouseX, pMouseY);
                else
                    graphics.renderTooltip(Minecraft.getInstance().font, List.of(this.getHovertext()), Optional.empty(), pMouseX, pMouseY);
            }
        }
    }
}
