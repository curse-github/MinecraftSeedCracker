/*    */ package net.minecraft.network.protocol.login;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*    */ import net.minecraft.network.protocol.cookie.CookiePacketTypes;
/*    */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*    */ 
/*    */ public class LoginProtocols {
/* 13 */   public static final SimpleUnboundProtocol<ServerLoginPacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.LOGIN, builder -> builder
/* 14 */       .addPacket(LoginPacketTypes.SERVERBOUND_HELLO, ServerboundHelloPacket.STREAM_CODEC)
/* 15 */       .addPacket(LoginPacketTypes.SERVERBOUND_KEY, ServerboundKeyPacket.STREAM_CODEC)
/* 16 */       .addPacket(LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER, ServerboundCustomQueryAnswerPacket.STREAM_CODEC)
/* 17 */       .addPacket(LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED, ServerboundLoginAcknowledgedPacket.STREAM_CODEC)
/* 18 */       .addPacket(CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE, ServerboundCookieResponsePacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 21 */   public static final ProtocolInfo<ServerLoginPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ 
/*    */ 
/*    */   
/* 25 */   public static final SimpleUnboundProtocol<ClientLoginPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.LOGIN, builder -> builder
/* 26 */       .addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT, ClientboundLoginDisconnectPacket.STREAM_CODEC)
/* 27 */       .addPacket(LoginPacketTypes.CLIENTBOUND_HELLO, ClientboundHelloPacket.STREAM_CODEC)
/* 28 */       .addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED, ClientboundLoginFinishedPacket.STREAM_CODEC)
/* 29 */       .addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION, ClientboundLoginCompressionPacket.STREAM_CODEC)
/* 30 */       .addPacket(LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY, ClientboundCustomQueryPacket.STREAM_CODEC)
/* 31 */       .addPacket(CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST, ClientboundCookieRequestPacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 34 */   public static final ProtocolInfo<ClientLoginPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\LoginProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */