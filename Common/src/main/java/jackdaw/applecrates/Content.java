package jackdaw.applecrates;

import jackdaw.applecrates.client.IClientConfig;
import jackdaw.applecrates.container.IMenuSlots;
import jackdaw.applecrates.network.IPacketOnButtonPress;

import java.util.function.Consumer;

public class Content {

    public static IMenuSlots menuSlots;
    public static IClientConfig clientConfig;
    public static IPacketOnButtonPress ownerGuiButton;
    public static IPacketOnButtonPress buyerGuiButton;
    public static Consumer<String> addOwnerButton;

}
