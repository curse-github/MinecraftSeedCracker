/*    */ package net.minecraft.network.protocol.handshake;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ 
/*    */ public class HandshakeProtocols {
/* 10 */   public static final SimpleUnboundProtocol<ServerHandshakePacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.HANDSHAKING, builder -> builder
/* 11 */       .addPacket(HandshakePacketTypes.CLIENT_INTENTION, ClientIntentionPacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 14 */   public static final ProtocolInfo<ServerHandshakePacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\handshake\HandshakeProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */