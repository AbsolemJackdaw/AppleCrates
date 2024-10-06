package jackdaw.applecrates.client.screen.widget;

import net.minecraft.network.chat.Component;

public class AddOwnerButton extends AbstractOwnerButton {

    public boolean isOn = false;

    public AddOwnerButton(int x, int y, Component message, OnPress onPress) {
        super(x, y, 20, 0, message, onPress);
    }

    @Override
    public void onPress() {
        this.isOn = !this.isOn;
        super.onPress();
    }
}

