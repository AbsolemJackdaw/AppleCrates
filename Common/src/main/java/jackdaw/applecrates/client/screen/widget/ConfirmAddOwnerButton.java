package jackdaw.applecrates.client.screen.widget;

import net.minecraft.network.chat.Component;

public class ConfirmAddOwnerButton extends AbstractOwnerButton {

    public ConfirmAddOwnerButton(int x, int y, Component message, OnPress onPress) {
        super(x, y, 12, 20, message, onPress);
        this.visible = false;
    }
}
