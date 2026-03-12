/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundPingPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundTransferPacket;
/*    */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*    */ import net.minecraft.network.protocol.common.CommonPacketTypes;
/*    */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*    */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*    */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*    */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*    */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*    */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*    */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*    */ import net.minecraft.network.protocol.cookie.CookiePacketTypes;
/*    */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*    */ 
/*    */ public class ConfigurationProtocols {
/* 33 */   public static final SimpleUnboundProtocol<ServerConfigurationPacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.CONFIGURATION, builder -> builder
/* 34 */       .addPacket(CommonPacketTypes.SERVERBOUND_CLIENT_INFORMATION, ServerboundClientInformationPacket.STREAM_CODEC)
/* 35 */       .addPacket(CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE, ServerboundCookieResponsePacket.STREAM_CODEC)
/* 36 */       .addPacket(CommonPacketTypes.SERVERBOUND_CUSTOM_PAYLOAD, ServerboundCustomPayloadPacket.STREAM_CODEC)
/* 37 */       .addPacket(ConfigurationPacketTypes.SERVERBOUND_FINISH_CONFIGURATION, ServerboundFinishConfigurationPacket.STREAM_CODEC)
/* 38 */       .addPacket(CommonPacketTypes.SERVERBOUND_KEEP_ALIVE, ServerboundKeepAlivePacket.STREAM_CODEC)
/* 39 */       .addPacket(CommonPacketTypes.SERVERBOUND_PONG, ServerboundPongPacket.STREAM_CODEC)
/* 40 */       .addPacket(CommonPacketTypes.SERVERBOUND_RESOURCE_PACK, ServerboundResourcePackPacket.STREAM_CODEC)
/* 41 */       .addPacket(ConfigurationPacketTypes.SERVERBOUND_SELECT_KNOWN_PACKS, ServerboundSelectKnownPacks.STREAM_CODEC)
/* 42 */       .addPacket(CommonPacketTypes.SERVERBOUND_CUSTOM_CLICK_ACTION, ServerboundCustomClickActionPacket.STREAM_CODEC)
/* 43 */       .addPacket(ConfigurationPacketTypes.SERVERBOUND_ACCEPT_CODE_OF_CONDUCT, ServerboundAcceptCodeOfConductPacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 46 */   public static final ProtocolInfo<ServerConfigurationPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */   
/* 48 */   public static final SimpleUnboundProtocol<ClientConfigurationPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.CONFIGURATION, builder -> builder
/* 49 */       .addPacket(CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST, ClientboundCookieRequestPacket.STREAM_CODEC)
/* 50 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CUSTOM_PAYLOAD, ClientboundCustomPayloadPacket.CONFIG_STREAM_CODEC)
/* 51 */       .addPacket(CommonPacketTypes.CLIENTBOUND_DISCONNECT, ClientboundDisconnectPacket.STREAM_CODEC)
/* 52 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_FINISH_CONFIGURATION, ClientboundFinishConfigurationPacket.STREAM_CODEC)
/* 53 */       .addPacket(CommonPacketTypes.CLIENTBOUND_KEEP_ALIVE, ClientboundKeepAlivePacket.STREAM_CODEC)
/* 54 */       .addPacket(CommonPacketTypes.CLIENTBOUND_PING, ClientboundPingPacket.STREAM_CODEC)
/* 55 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_RESET_CHAT, ClientboundResetChatPacket.STREAM_CODEC)
/* 56 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_REGISTRY_DATA, ClientboundRegistryDataPacket.STREAM_CODEC)
/* 57 */       .addPacket(CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_POP, ClientboundResourcePackPopPacket.STREAM_CODEC)
/* 58 */       .addPacket(CommonPacketTypes.CLIENTBOUND_RESOURCE_PACK_PUSH, ClientboundResourcePackPushPacket.STREAM_CODEC)
/* 59 */       .addPacket(CommonPacketTypes.CLIENTBOUND_STORE_COOKIE, ClientboundStoreCookiePacket.STREAM_CODEC)
/* 60 */       .addPacket(CommonPacketTypes.CLIENTBOUND_TRANSFER, ClientboundTransferPacket.STREAM_CODEC)
/* 61 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_UPDATE_ENABLED_FEATURES, ClientboundUpdateEnabledFeaturesPacket.STREAM_CODEC)
/* 62 */       .addPacket(CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS, ClientboundUpdateTagsPacket.STREAM_CODEC)
/* 63 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_SELECT_KNOWN_PACKS, ClientboundSelectKnownPacks.STREAM_CODEC)
/* 64 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CUSTOM_REPORT_DETAILS, ClientboundCustomReportDetailsPacket.STREAM_CODEC)
/* 65 */       .addPacket(CommonPacketTypes.CLIENTBOUND_SERVER_LINKS, ClientboundServerLinksPacket.STREAM_CODEC)
/* 66 */       .addPacket(CommonPacketTypes.CLIENTBOUND_CLEAR_DIALOG, ClientboundClearDialogPacket.STREAM_CODEC)
/* 67 */       .addPacket(CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG, ClientboundShowDialogPacket.CONTEXT_FREE_STREAM_CODEC)
/* 68 */       .addPacket(ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT, ClientboundCodeOfConductPacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 71 */   public static final ProtocolInfo<ClientConfigurationPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\configuration\ConfigurationProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */