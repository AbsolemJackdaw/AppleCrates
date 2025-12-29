package jackdaw.applecrates.network;

import jackdaw.applecrates.Constants;
import jackdaw.applecrates.network.packetprocessing.ServerAddOwner;
import jackdaw.applecrates.network.packetprocessing.ServerCrateSync;
import jackdaw.applecrates.network.packetprocessing.ServerGetSale;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class PacketId {

    private static final Identifier SPACKET_TRADE = Identifier.fromNamespaceAndPath(Constants.MODID, "s_tradesync");
    public static final CustomPacketPayload.Type<ServerCrateSync> SPACKET_TRADE_TYPE = new CustomPacketPayload.Type<>(SPACKET_TRADE);
    public static StreamCodec<FriendlyByteBuf, ServerCrateSync> SPACKET_TRADE_CODEC = StreamCodec.unit(new ServerCrateSync());

    private static final Identifier SPACKET_SALE = Identifier.fromNamespaceAndPath(Constants.MODID, "s_getsale");
    public static final CustomPacketPayload.Type<ServerGetSale> SPACKET_SALE_TYPE = new CustomPacketPayload.Type<>(SPACKET_SALE);
    public static StreamCodec<FriendlyByteBuf, ServerGetSale> SPACKET_SALE_CODEC = StreamCodec.unit(new ServerGetSale());

    private static final Identifier SPACKET_ADDOWNER = Identifier.fromNamespaceAndPath(Constants.MODID, "s_addowner");
    public static final CustomPacketPayload.Type<ServerAddOwner> SPACKET_ADDOWNER_TYPE = new CustomPacketPayload.Type<>(SPACKET_ADDOWNER);
    public static StreamCodec<FriendlyByteBuf, ServerAddOwner> SPACKET_ADDOWNER_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ServerAddOwner::newOwnerUsername, ServerAddOwner::new);
}
