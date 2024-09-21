package jackdaw.applecrates.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.network.PacketId;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.container.CrateStackHandler;
import jackdaw.applecrates.network.ServerNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateScreen extends AbstractContainerScreen<CrateMenu> {
    private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager2.png");
    private static final Component CANNOT_SWITCH = Component.translatable("cannot.switch.trade");
    private boolean isOwner;
    private boolean isUnlimitedShop;
    private int guiStartX;
    private int guiStartY;

    public CrateScreen(CrateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, Component.translatable(pTitle.getString()));
        this.isOwner = pMenu.isOwner;
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
        this.isUnlimitedShop = pMenu.isUnlimitedShop;
    }

    public static CrateScreen forOwner(CrateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        CrateScreen crateScreen = new CrateScreen(pMenu, pPlayerInventory, pTitle);
        crateScreen.isOwner = true;
        return crateScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.guiStartX = (this.width - this.imageWidth) / 2;
        this.guiStartY = (this.height - this.imageHeight) / 2;
        if (isOwner) {
            addRenderableWidget(new SaleButton(guiStartX + 4, guiStartY + 138, (button) -> {
                if (!(menu.interactableSlots.getItem(0).isEmpty() && menu.interactableSlots.getItem(1).isEmpty())) {
                    if (menu.crateStock.getItem(29).isEmpty() || isSamePayout()) { //do not allow a change if the payout slot isn't empty or the same item as the current one
                        ClientPlayNetworking.send(PacketId.CHANNEL, ServerNetwork.sPacketTrade());
                    }
                }
            }));
        } else {
            addRenderableWidget(new SaleButton(guiStartX + 4, guiStartY + 17, (button) -> {
                if (isUnlimitedShop || !menu.outOfStock())
                    ClientPlayNetworking.send(PacketId.CHANNEL, ServerNetwork.sPacketSale());
            }));
        }
    }

    private boolean isSamePayout() {
        ItemStack payout = menu.crateStock.getItem(29).copy();
        ItemStack give = menu.interactableSlots.getItem(0).copy();
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
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        RenderSystem.enableBlend();
        if (isOwner && !(menu.priceAndSaleSlots.getItem(0).isEmpty() && menu.priceAndSaleSlots.getItem(1).isEmpty()) && menu.interactableSlots.getItem(0).isEmpty() && menu.interactableSlots.getItem(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        if (!isSamePayout())
            RenderSystem.setShaderColor(1.0F, 0.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
        int offSet = isOwner ? 0 : -140 + 19;
        if (menu.outOfStock() && !isOwner && !isUnlimitedShop)
            blit(pPoseStack, guiStartX + 60, guiStartY + 144 + offSet, this.getBlitOffset(), 25.0F, 171.0F, 10, 9, 512, 256);
        else
            blit(pPoseStack, guiStartX + 60, guiStartY + 144 + offSet, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderTrade(0, guiStartX, guiStartY + offSet);
        renderTrade(1, guiStartX, guiStartY + offSet);

        if (!menu.crateStock.getItem(29).isEmpty() && isOwner) {
            ItemStack inSlot = menu.crateStock.getItem(29);
            if (inSlot.getOrCreateTag().contains(CrateStackHandler.TAGSTOCK)) {
                int pay = inSlot.getOrCreateTag().getInt(CrateStackHandler.TAGSTOCK);
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(pay);
            }
        }
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }


    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if ((!menu.interactableSlots.getItem(slotId).isEmpty() && isOwner) || !menu.priceAndSaleSlots.getItem(slotId).isEmpty()) {
            ItemStack saleStack = isOwner && !menu.interactableSlots.getItem(slotId).isEmpty() ? menu.interactableSlots.getItem(slotId) : menu.priceAndSaleSlots.getItem(slotId);
            int xo = slotId == 0 ? 10 : 74;
            int yo = 140;
            this.itemRenderer.renderAndDecorateFakeItem(saleStack, x + xo, y + yo);
            this.itemRenderer.renderGuiItemDecorations(this.font, saleStack, x + xo, y + yo);
        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);

        blit(pPoseStack, guiStartX, guiStartY, this.getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
        //hide second slot
        blit(pPoseStack, guiStartX + 161, guiStartY + 36, this.getBlitOffset(), 161.0F, 15.0F, 18, 18, 512, 256);

        if (isOwner) {
            //Draw makeshift slots from player inventory in villager gui
            blit(pPoseStack, guiStartX + 4, guiStartY + 17, this.getBlitOffset(), 107.0F, 83.0F, 18 * 5, 18 * 3, 512, 256);
            blit(pPoseStack, guiStartX + 4, guiStartY + 17 + (18 * 3), this.getBlitOffset(), 107.0F, 83.0F, 18 * 5, 18 * 3, 512, 256);
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 0.8F);
            //money slot
            blit(pPoseStack, guiStartX + 4 + 18 * 4, guiStartY + 17 + 18 * 5, this.getBlitOffset(), 161.0F, 36.0F, 18, 18, 512, 256);
        } else {
            //hide open part of villager scroll gui
            fill(pPoseStack, guiStartX + 4, guiStartY + 37, guiStartX + 97 + 4, guiStartY + 122 + 37, 0xffc6c6c6);
            int scrollSpotLength = 5;
            blit(pPoseStack, guiStartX + 4, guiStartY + 37, this.getBlitOffset(), 4.0F, 158.0F - (float) scrollSpotLength, 97, 5 + scrollSpotLength, 512, 256);
        }

        if (isUnlimitedShop) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            blit(pPoseStack, guiStartX + 185, guiStartY + 36, this.getBlitOffset(), 276.0F, 0.0F, 25, 20, 512, 256);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    class SaleButton extends Button {

        public SaleButton(int x, int y, OnPress press) {
            super(x, y, 91, 20, Component.empty(), press);
        }

        public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
            if (this.isHovered) {
                if (!isSamePayout()) {
                    CrateScreen.this.renderTooltip(pPoseStack, CANNOT_SWITCH, pMouseX, pMouseY);
                } else if (pMouseX < this.x + 20) {
                    doRenderTip(pPoseStack, pMouseX, pMouseY, 0);
                } else if (pMouseX > this.x + 65) {
                    doRenderTip(pPoseStack, pMouseX, pMouseY, 1);
                }
            }
        }

        private void doRenderTip(PoseStack pPoseStack, int pMouseX, int pMouseY, int slot) {
            ItemStack stack = menu.priceAndSaleSlots.getItem(slot);
            if (!stack.isEmpty())
                CrateScreen.this.renderTooltip(pPoseStack, stack, pMouseX, pMouseY);
        }
    }
}
