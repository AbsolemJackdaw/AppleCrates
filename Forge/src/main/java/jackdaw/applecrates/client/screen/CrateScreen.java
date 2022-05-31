package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import jackdaw.applecrates.container.CrateMenu;
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
            addRenderableWidget(new Button(x + 5, y + 138, 89, 20, TextComponent.EMPTY, (button) -> {

            }));

//            addRenderableWidget(new Button(x+5, 119, 89, 20, new TextComponent("For"), (button) -> {
//
//            }));
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;


        if (isOwner) {
            if (!menu.crateSales.getStackInSlot(0).isEmpty()) {
                ItemStack confirmedSale = menu.crateSales.getStackInSlot(0);
                this.itemRenderer.renderAndDecorateFakeItem(confirmedSale, x + 74, y + 140);
                this.itemRenderer.renderGuiItemDecorations(this.font, confirmedSale, x + 74, y + 140);
            }
            if (!menu.crateSales.getStackInSlot(1).isEmpty()) {
                ItemStack confirmedSale = menu.crateSales.getStackInSlot(1);
                this.itemRenderer.renderAndDecorateFakeItem(confirmedSale, x + 16, y + 140);
                this.itemRenderer.renderGuiItemDecorations(this.font, confirmedSale, x + 16, y + 140);
            }
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
            blit(pPoseStack, x + 5 + 35 + 20, y + 144, this.getBlitOffset(), 15.0F, 171.0F, 10, 9, 512, 256);

        }
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VILLAGER_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(pPoseStack, i, j, this.getBlitOffset(), 0.0F, 0.0F, this.imageWidth, this.imageHeight, 512, 256);
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
