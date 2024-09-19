package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.Constants;
import jackdaw.applecrates.container.CrateMenuOwner;
import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.network.CrateChannel;
import jackdaw.applecrates.network.SCrateTradeSync;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreenOwner extends AbstractContainerScreen<CrateMenuOwner> {
    private static final ResourceLocation OWNER = new ResourceLocation(Constants.MODID, "gui/owner.png");
    private static final ResourceLocation VILLAGER_UI = new ResourceLocation("textures/gui/container/villager2.png");

    private static final Component CANNOT_SWITCH = Component.translatable("cannot.switch.trade");
    private boolean isUnlimitedShop;
    private int guiStartX;
    private int guiStartY;

    public CrateScreenOwner(CrateMenuOwner menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, Component.translatable(title.getString()));
        this.imageWidth = 198;
        this.imageHeight = 194;
        this.inventoryLabelX = titleLabelX + 8;
        this.inventoryLabelY = titleLabelY + 90;
        this.isUnlimitedShop = menu.isUnlimitedShop;
    }

    @Override
    protected void init() {
        super.init();
        this.guiStartX = (this.width - this.imageWidth) / 2;
        this.guiStartY = (this.height - this.imageHeight) / 2;

        addRenderableWidget(new SaleButton(guiStartX + 72, guiStartY + 74, (button) -> {
            if (!(menu.interactableTradeSlots.getStackInSlot(0).isEmpty() && menu.interactableTradeSlots.getStackInSlot(1).isEmpty())) {
                if (menu.crateStock.getStackInSlot(Constants.TOTALCRATESTOCKLOTS).isEmpty() || isSamePayout()) { //do not allow a change if the payout slot isn't empty or the same item as the current one
                    CrateChannel.NETWORK.sendToServer(new SCrateTradeSync()); //handles switching up items and giving back to player
                }
            }
        }));
    }

    private boolean isSamePayout() {
        ItemStack payout = menu.crateStock.getStackInSlot(Constants.TOTALCRATESTOCKLOTS).copy();
        ItemStack give = menu.interactableTradeSlots.getStackInSlot(0).copy();
        if (give.isEmpty() || payout.isEmpty())
            return true;

        if (payout.hasTag() && payout.getTag().contains(CrateStackHandler.TAGSTOCK)) {
            payout.removeTagKey(CrateStackHandler.TAGSTOCK);
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
        if (!(menu.savedTradeSlots.getStackInSlot(0).isEmpty() && menu.savedTradeSlots.getStackInSlot(1).isEmpty()) && menu.interactableTradeSlots.getStackInSlot(0).isEmpty() && menu.interactableTradeSlots.getStackInSlot(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        if (!isSamePayout())
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_UI);
//        if (menu.outOfStock() && !isOwner && !isUnlimitedShop)
//            blit(pPoseStack, guiStartX + 60, guiStartY + 144 + offSet, this.getBlitOffset(), 25.0F, 171.0F, 10, 9, 256, 256);
//        else
        blit(poseStack, guiStartX + 125, guiStartY + 79, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(0, guiStartX, guiStartY);
        renderTrade(1, guiStartX, guiStartY);


        if (!menu.crateStock.getStackInSlot(menu.crateStock.getSlots() - 1).isEmpty()) {
            ItemStack inSlot = menu.crateStock.getStackInSlot(menu.crateStock.getSlots() - 1);
            if (inSlot.getOrCreateTag().contains(CrateStackHandler.TAGSTOCK)) {
                int pay = inSlot.getOrCreateTag().getInt(CrateStackHandler.TAGSTOCK);
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(pay);
            }
        }

//        for (Slot slot : menu.slots)
//            if (slot.isActive())
//                this.font.draw(poseStack, "" + slot.index, guiStartX + slot.x, guiStartY + slot.y, 4210752);

        this.renderTooltip(poseStack, pMouseX, pMouseY);
    }


    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if ((!menu.interactableTradeSlots.getStackInSlot(slotId).isEmpty()) || !menu.savedTradeSlots.getStackInSlot(slotId).isEmpty()) {
            ItemStack saleStack = !menu.interactableTradeSlots.getStackInSlot(slotId).isEmpty() ? menu.interactableTradeSlots.getStackInSlot(slotId) : menu.savedTradeSlots.getStackInSlot(slotId);
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

    class SaleButton extends Button {

        public SaleButton(int x, int y, Button.OnPress press) {
            super(x, y, 91, 20, Component.empty(), press);
        }

        public void renderToolTip(PoseStack poseStack, int pMouseX, int pMouseY) {
            if (this.isHovered) {
                if (!isSamePayout()) {
                    CrateScreenOwner.this.renderTooltip(poseStack, CANNOT_SWITCH, pMouseX, pMouseY);
                } else if (pMouseX < this.x + 20) {
                    doRenderTip(poseStack, pMouseX, pMouseY, 0);
                } else if (pMouseX > this.x + 65) {
                    doRenderTip(poseStack, pMouseX, pMouseY, 1);
                }
            }
        }

        private void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.savedTradeSlots.getStackInSlot(slot);
            if (!stack.isEmpty())
                CrateScreenOwner.this.renderTooltip(pPoseStack, stack, pMouseX, pMouseY);
        }
    }
}
