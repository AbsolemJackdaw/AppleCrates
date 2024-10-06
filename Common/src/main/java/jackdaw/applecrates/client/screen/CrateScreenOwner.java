package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.container.CrateMenuOwner;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CrateScreenOwner extends CrateScreen<CrateMenuOwner> {
    private static final ResourceLocation OWNER = new ResourceLocation(Constants.MODID, "gui/owner.png");
    private static final ResourceLocation OWNER_BUTTONS = new ResourceLocation(Constants.MODID, "gui/owner_buttons.png");

    private EditBox username;
    private ConfirmAddOwnerButton confirmAddOwner;
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
                        54,
                        (button) -> {
                            if (!(menu.adapter.getInteractableTradeItem(0).isEmpty() && menu.adapter.getInteractableTradeItem(1).isEmpty())
                                    || (menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).isEmpty() || isSamePayout())) //do not allow a change if the payout slot isn't empty or the same item as the current one
                                Content.ownerGuiButton.accept();//handles switching up items and giving back to player
                        }));
        this.username = addWidget( // we manually render this above everything else
                new EditBox(
                        this.font,
                        this.guiStartX + 33,
                        this.guiStartY + 37,
                        113, 12,
                        Component.translatable("crate.add.owner")
                ));
        this.username.setMaxLength(16); // Maximum length of a Java username. https://minecraft.wiki/w/Player#Username
        this.username.setVisible(false);
        this.username.setEditable(false);
        this.username.setCanLoseFocus(false);
        this.username.setValue("username");
        this.username.setFilter(ServerLoginPacketListenerImpl::isValidUsername);
        this.confirmAddOwner = addWidget( // we manually render this above everything else
                new ConfirmAddOwnerButton(
                        this.guiStartX + 151,
                        this.guiStartY + 37,
                        Component.translatable("crate.add.owner.confirm"),
                        button -> {
                            if (!this.username.getValue().isBlank())
                                Content.addOwnerButton.accept(this.username.getValue());
                            boolean isNowOn = addOwnerButton.on = !addOwnerButton.on;
                            this.username.setEditable(isNowOn);
                            this.username.setVisible(isNowOn);
                            if (isNowOn) {
                                this.setFocused(this.username);
                                this.cancelFocusChange = true;
                            }
                            this.username.setFocus(isNowOn);
                            this.confirmAddOwner.visible = isNowOn;
                        }
                )
        );
        this.confirmAddOwner.visible = false;

        this.addOwnerButton = addRenderableWidget(
                new AddOwnerButton(
                        guiStartX + 138,
                        guiStartY + 74,
                        Component.translatable("crate.add.owner"),
                        button -> {
                            boolean isNowOn = addOwnerButton.on;
                            this.username.setEditable(isNowOn);
                            this.username.setVisible(isNowOn);
                            if (isNowOn) {
                                this.setFocused(this.username);
                                this.cancelFocusChange = true;
                            }
                            this.username.setFocus(isNowOn);
                            this.confirmAddOwner.visible = isNowOn;
                        }));
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
        if (this.username.isVisible() && super.isHovering(10, 15, 180, 54, mouseX, mouseY))
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
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        if (!(menu.adapter.getSavedTradeSlotsItem(0).isEmpty() && menu.adapter.getSavedTradeSlotsItem(1).isEmpty()) && menu.adapter.getInteractableTradeItem(0).isEmpty() && menu.adapter.getInteractableTradeItem(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        if (!isSamePayout())
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        blit(poseStack, guiStartX + 92, guiStartY + 79, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(0, guiStartX, guiStartY);
        renderTrade(1, guiStartX, guiStartY);


        if (!menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) { // moneyslot
            ItemStack inSlot = menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS); // moneyslot
            if (inSlot.getOrCreateTag().contains(Constants.TAGSTOCK)) {
                int pay = inSlot.getOrCreateTag().getInt(Constants.TAGSTOCK);
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(pay);
            }
        }

        poseStack.pushPose();
        poseStack.translate(0, 0, 350); // manually render these above everything else.
        this.username.render(poseStack, pMouseX, pMouseY, pPartialTick);
        this.confirmAddOwner.render(poseStack, pMouseX, pMouseY, pPartialTick);
        poseStack.popPose();

        this.renderTooltip(poseStack, pMouseX, pMouseY);
    }

    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if ((!menu.adapter.getInteractableTradeItem(slotId).isEmpty()) || !menu.adapter.getSavedTradeSlotsItem(slotId).isEmpty()) {
            ItemStack saleStack = !menu.adapter.getInteractableTradeItem(slotId).isEmpty() ? menu.adapter.getInteractableTradeItem(slotId) : menu.adapter.getSavedTradeSlotsItem(slotId);
            int xo = slotId == 0 ? 74 : 104;
            int yo = 75;
            this.itemRenderer.renderAndDecorateFakeItem(saleStack, x + xo, y + yo);
            this.itemRenderer.renderGuiItemDecorations(this.font, saleStack, x + xo, y + yo);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, OWNER);
        blit(pPoseStack, guiStartX, guiStartY, this.getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (this.username.isVisible())
            this.username.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int $$1, int $$2) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.username.keyPressed(keyCode, $$1, $$2) || this.username.canConsumeInput() || super.keyPressed(keyCode, $$1, $$2);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    private class SaleButtonOwner extends SaleButton {
        private static final Component CANNOT_SWITCH = Component.translatable("cannot.switch.trade");

        public SaleButtonOwner(int x, int y, int width, OnPress press) {
            super(x, y, width, Component.translatable("crate.button.sale.owner"), press);
        }

        @Override
        public void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.adapter.getSavedTradeSlotsItem(slot);
            if (!stack.isEmpty())
                CrateScreenOwner.this.renderTooltip(pPoseStack, stack, pMouseX, pMouseY);

        }

        @Override
        public void renderToolTip(PoseStack poseStack, int pMouseX, int pMouseY) {
            super.renderToolTip(poseStack, pMouseX, pMouseY);
            if (this.isHovered)
                if (!isSamePayout())
                    CrateScreenOwner.this.renderTooltip(poseStack, CANNOT_SWITCH, pMouseX, pMouseY);
        }
    }

    private static class AddOwnerButton extends Button {

        protected boolean on = false;

        public AddOwnerButton(int x, int y, Component message, OnPress onPress) {
            super(x, y, 20, 20, message, onPress);
        }

        @Override
        public void onPress() {
            this.on = !this.on;
            super.onPress();
        }

        @Override
        public void renderButton(PoseStack poseStack, int someInt, int otherInt, float $$3) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, OWNER_BUTTONS);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int startY = this.isHoveredOrFocused() | this.on ? 20 : 0;
            blit(poseStack, this.x, this.y, this.getBlitOffset(), 0.0F, startY, 20, 20, 32, 40);
        }
    }

    private static class ConfirmAddOwnerButton extends Button {
        public ConfirmAddOwnerButton(int x, int y, Component message, OnPress onPress) {
            super(x, y, 12, 12, message, onPress);
        }

        @Override
        public void renderButton(PoseStack poseStack, int someInt, int otherInt, float $$3) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, OWNER_BUTTONS);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            int startY = this.isHoveredOrFocused() ? 12 : 0;
            blit(poseStack, this.x, this.y, this.getBlitOffset(), 20F, startY, 12, 12, 32, 40);
        }
    }
}
