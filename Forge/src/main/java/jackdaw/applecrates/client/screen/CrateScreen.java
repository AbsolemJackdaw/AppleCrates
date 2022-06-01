package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.container.CrateMenu;
import jackdaw.applecrates.network.CrateChannel;
import jackdaw.applecrates.network.SCrateTradeSync;
import jackdaw.applecrates.network.SGetSale;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CrateScreen extends AbstractContainerScreen<CrateMenu> {
    private static final ResourceLocation VILLAGER_LOCATION = new ResourceLocation("textures/gui/container/villager2.png");
    private boolean isOwner;

    public CrateScreen(CrateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.isOwner = pMenu.isOwner;
        this.imageWidth = 276;
        this.inventoryLabelX = 107;
    }

    public static CrateScreen forOwner(CrateMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        CrateScreen crateScreen = new CrateScreen(pMenu, pPlayerInventory, pTitle);
        crateScreen.isOwner = true;
        return crateScreen;
    }

    @Override
    protected void init() {
        super.init();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        if (isOwner) {
            addRenderableWidget(new Button(x + 4, y + 138, 91, 20, TextComponent.EMPTY, (button) -> {
                if (!(menu.interactableSlots.getStackInSlot(0).isEmpty() && menu.interactableSlots.getStackInSlot(1).isEmpty()))
                    CrateChannel.NETWORK.sendToServer(new SCrateTradeSync()); //handles switching up items and giving back to player
            }));
        } else {
            addRenderableWidget(new Button(x + 4, y + 17, 91, 20, TextComponent.EMPTY, (button) -> {
                CrateChannel.NETWORK.sendToServer(new SGetSale());
            }));
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;


        int offSet = isOwner ? 0 : -140 + 19;
        renderTrade(0, x, y + offSet);
        renderTrade(1, x, y + offSet);
        RenderSystem.enableBlend();
        if (isOwner && !(menu.priceAndSaleSlots.getStackInSlot(0).isEmpty() && menu.priceAndSaleSlots.getStackInSlot(1).isEmpty()) && menu.interactableSlots.getStackInSlot(0).isEmpty() && menu.interactableSlots.getStackInSlot(1).isEmpty())
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
        blit(pPoseStack, x + 60, y + 144 + offSet, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);

    }

    //slots are invisible for aesthetic and syncing purposes. draw itemstacks by hand
    private void renderTrade(int slotId, int x, int y) {
        if ((!menu.interactableSlots.getStackInSlot(slotId).isEmpty() && isOwner) || !menu.priceAndSaleSlots.getStackInSlot(slotId).isEmpty()) {
            ItemStack saleStack = isOwner && !menu.interactableSlots.getStackInSlot(slotId).isEmpty() ? menu.interactableSlots.getStackInSlot(slotId) : menu.priceAndSaleSlots.getStackInSlot(slotId);
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
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        blit(pPoseStack, x, y, this.getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);

        if (isOwner) {
            //Draw makeshift slots from player inventory in villager gui
            blit(pPoseStack, x + 4, y + 17, this.getBlitOffset(), 107.0F, 83.0F, 18 * 5, 18 * 3, 512, 256);
            blit(pPoseStack, x + 4, y + 17 + (18 * 3), this.getBlitOffset(), 107.0F, 83.0F, 18 * 5, 18 * 3, 512, 256);

        }
    }

    class TradeOfferButton extends Button {
        final int index;

        public TradeOfferButton(int p_99205_, int p_99206_, int p_99207_, Button.OnPress p_99208_) {
            super(p_99205_, p_99206_, 89, 20, TextComponent.EMPTY, p_99208_);
            this.index = p_99207_;
            this.visible = false;
        }

        public int getIndex() {
            return this.index;
        }

        public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
            if (this.isHovered) {
                if (pMouseX < this.x + 20) {
                    ItemStack payment = new ItemStack(Items.EMERALD);
                    CrateScreen.this.renderTooltip(pPoseStack, payment, pMouseX, pMouseY);
                } else if (pMouseX > this.x + 65) {
                    ItemStack sale = new ItemStack(Items.APPLE, 4);
                    CrateScreen.this.renderTooltip(pPoseStack, sale, pMouseX, pMouseY);
                }
            }

        }
    }
}
