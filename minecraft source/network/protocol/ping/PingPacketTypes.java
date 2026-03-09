/*    */ package net.minecraft.network.protocol.ping;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PingPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundPongResponsePacket> CLIENTBOUND_PONG_RESPONSE = createClientbound("pong_response");
/*    */   
/* 11 */   public static final PacketType<ServerboundPingRequestPacket> SERVERBOUND_PING_REQUEST = createServerbound("ping_request");
/*    */ 
/*    */   
/* 14 */   private static <T extends net.minecraft.network.protocol.Packet<ClientPongPacketListener>> PacketType<T> createClientbound(String id) { return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private static <T extends net.minecraft.network.protocol.Packet<ServerPingPacketListener>> PacketType<T> createServerbound(String id) { return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ping\PingPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */