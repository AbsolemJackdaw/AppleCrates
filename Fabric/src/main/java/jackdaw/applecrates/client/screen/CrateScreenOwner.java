package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.CrateMenu;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreenOwner extends CommonCrateScreen<CrateMenu> {
    private static final ResourceLocation OWNER = new ResourceLocation(Constants.MODID, "gui/owner.png");

    public CrateScreenOwner(CrateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, Component.translatable(pTitle.getString()));
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
                        guiStartX + 72,
                        guiStartY + 74,
                        91,
                        (button) -> {
                            if (!(menu.interactableTradeSlots.getItem(0).isEmpty() && menu.interactableTradeSlots.getItem(1).isEmpty())
                                    || (menu.crateStock.getItem(Constants.TOTALCRATESTOCKLOTS).isEmpty() || isSamePayout())) //do not allow a change if the payout slot isn't empty or the same item as the current one
                            {
                            }
                            // CrateChannel.NETWORK.sendToServer(new SCrateTradeSync()); //handles switching up items and giving back to player
                            //TODO
                        }));
    }

    protected boolean isSamePayout() {
        ItemStack payout = menu.crateStock.getItem(Constants.TOTALCRATESTOCKLOTS).copy();
        ItemStack give = menu.interactableTradeSlots.getItem(0).copy();
        if (give.isEmpty() || payout.isEmpty())
            return true;

        if (payout.hasTag() && payout.getTag().contains(Constants.TAGSTOCK)) {
            payout.removeTagKey(Constants.TAGSTOCK);
            if (payout.getTag() != null && payout.getTag().isEmpty())
                payout.setTag(null);
        }
        return ItemStack.isSameItemSameTags(payout, give);
    }

    @Override
    public void render(PoseStack poseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.enableBlend();
        if (!(menu.savedTradeSlots.getItem(0).isEmpty() && menu.savedTradeSlots.getItem(1).isEmpty()) && menu.interactableTradeSlots.getItem(0).isEmpty() && menu.interactableTradeSlots.getItem(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        if (!isSamePayout())
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
        blit(poseStack, guiStartX + 125, guiStartY + 79, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(0, guiStartX, guiStartY);
        renderTrade(1, guiStartX, guiStartY);


        if (!menu.crateStock.getItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) {
            ItemStack inSlot = menu.crateStock.getItem(Constants.TOTALCRATESTOCKLOTS);
            if (inSlot.getOrCreateTag().contains(Constants.TAGSTOCK)) {
                int pay = inSlot.getOrCreateTag().getInt(Constants.TAGSTOCK);
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(pay);
            }
        }

        this.renderTooltip(poseStack, pMouseX, pMouseY);
    }


    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if ((!menu.interactableTradeSlots.getItem(slotId).isEmpty()) || !menu.savedTradeSlots.getItem(slotId).isEmpty()) {
            ItemStack saleStack = !menu.interactableTradeSlots.getItem(slotId).isEmpty() ? menu.interactableTradeSlots.getItem(slotId) : menu.savedTradeSlots.getItem(slotId);
            int xo = slotId == 0 ? 76 : 138;
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
    public void onClose() {
        super.onClose();
    }

    private class SaleButtonOwner extends SaleButton {
        private static final Component CANNOT_SWITCH = Component.translatable("cannot.switch.trade");

        public SaleButtonOwner(int x, int y, int width, OnPress press) {
            super(x, y, width, press);
        }

        @Override
        public void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.savedTradeSlots.getItem(slot);
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
}
