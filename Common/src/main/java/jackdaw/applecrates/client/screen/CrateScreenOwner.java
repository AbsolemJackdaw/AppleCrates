package jackdaw.applecrates.client.screen;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.Content;
import jackdaw.applecrates.client.screen.widget.*;
import jackdaw.applecrates.container.CrateMenuOwner;
import jackdaw.applecrates.item.datacomponent.CoinCounter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CrateScreenOwner extends CrateScreen<CrateMenuOwner> {
    private static final ResourceLocation OWNER = ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/gui/owner.png");

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
    public void setFocused(GuiEventListener $$0) { // This method fires after a buttons onClick method to set focus to the button
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

        if (payout.getOrDefault(Content.coinCounter, new CoinCounter(0)).count() > 0)
            payout.remove(Content.coinCounter);
        return ItemStack.isSameItemSameComponents(payout, give);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics, pMouseX, pMouseY, pPartialTick);
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (!(menu.adapter.getSavedTradeSlotsItem(0).isEmpty() && menu.adapter.getSavedTradeSlotsItem(1).isEmpty()) && menu.adapter.getInteractableTradeItem(0).isEmpty() && menu.adapter.getInteractableTradeItem(1).isEmpty())
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_SPRITE, guiStartX + 100, guiStartY + 79, 10, 9, 0xff00ff00);
        if (!isSamePayout())
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TRADE_ARROW_SPRITE, guiStartX + 100, guiStartY + 79, 10, 9, 0xffff0000);
        renderTrade(graphics, 0, guiStartX, guiStartY);
        renderTrade(graphics, 1, guiStartX, guiStartY);


        if (!menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS).isEmpty()) { // moneyslot
            ItemStack inSlot = menu.adapter.getCrateStockItem(Constants.TOTALCRATESTOCKLOTS); // moneyslot
            if (inSlot.getOrDefault(Content.coinCounter, new CoinCounter(0)).count() > 0) {
                //set inSlot's itemcount to the nbt ammount, but only on client side
                //this is visual
                inSlot.setCount(inSlot.get(Content.coinCounter).count());
            }
        }

        graphics.nextStratum();
        this.inputField.render(graphics, pMouseX, pMouseY, pPartialTick);
        if (this.confirmAddOwnerButton.visible)
            this.confirmAddOwnerButton.render(graphics, pMouseX, pMouseY, pPartialTick);

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
        graphics.blit(RenderPipelines.GUI_TEXTURED, OWNER, guiStartX, guiStartY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        graphics.nextStratum();
        this.inputField.renderBgCustom(graphics);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        //TODO check if removing this unexisting method breaks stuff
//        if (this.inputField.isVisible())
//            this.inputField.tick();
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.input() == 256)
            this.minecraft.player.closeContainer();
        return this.inputField.keyPressed(event) || this.inputField.canConsumeInput() || super.keyPressed(event);
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

            List<Component> list;
            if (pMouseX < this.getX() + 20 && !pay.isEmpty()) {
                list = (pay.getTooltipLines(Item.TooltipContext.EMPTY, minecraft.player, TooltipFlag.NORMAL));
                graphics.setComponentTooltipForNextFrame(CrateScreenOwner.this.font, list, pMouseX, pMouseY);
            } else if (pMouseX > this.getX() + this.width - 25 && !result.isEmpty()) {
                list = (result.getTooltipLines(Item.TooltipContext.EMPTY, minecraft.player, TooltipFlag.NORMAL));
                graphics.setComponentTooltipForNextFrame(CrateScreenOwner.this.font, list, pMouseX, pMouseY);
            } else {
                if (!isSamePayout()) {
                    graphics.setComponentTooltipForNextFrame(CrateScreenOwner.this.font, List.of(CANNOT_SWITCH), pMouseX, pMouseY);
                } else {
                    graphics.setComponentTooltipForNextFrame(CrateScreenOwner.this.font, List.of(this.getHovertext()), pMouseX, pMouseY);
                }
            }
        }
    }
}
