/*    */ package net.minecraft.network.protocol.status;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.PingPacketTypes;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ 
/*    */ public class StatusProtocols {
/* 14 */   public static final SimpleUnboundProtocol<ServerStatusPacketListener, ByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.STATUS, builder -> builder
/* 15 */       .addPacket(StatusPacketTypes.SERVERBOUND_STATUS_REQUEST, ServerboundStatusRequestPacket.STREAM_CODEC)
/* 16 */       .addPacket(PingPacketTypes.SERVERBOUND_PING_REQUEST, ServerboundPingRequestPacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 19 */   public static final ProtocolInfo<ServerStatusPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(e -> e);
/*    */   
/* 21 */   public static final SimpleUnboundProtocol<ClientStatusPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.STATUS, builder -> builder
/* 22 */       .addPacket(StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE, ClientboundStatusResponsePacket.STREAM_CODEC)
/* 23 */       .addPacket(PingPacketTypes.CLIENTBOUND_PONG_RESPONSE, ClientboundPongResponsePacket.STREAM_CODEC));
/*    */ 
/*    */   
/* 26 */   public static final ProtocolInfo<ClientStatusPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\StatusProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */