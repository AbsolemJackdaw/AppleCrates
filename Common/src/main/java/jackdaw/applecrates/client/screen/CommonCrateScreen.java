package jackdaw.applecrates.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CommonCrateScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected static final ResourceLocation VILLAGER_UI = new ResourceLocation("textures/gui/container/villager2.png");

    protected int guiStartX;
    protected int guiStartY;

    public CommonCrateScreen(T menu, Inventory inventory, Component comp) {
        super(menu, inventory, comp);
    }

    @Override
    protected void init() {
        super.init();
        this.guiStartX = (this.width - this.imageWidth) / 2;
        this.guiStartY = (this.height - this.imageHeight) / 2;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {

    }

    public boolean isUnlimitedShop() {
        return false;
    }
}
