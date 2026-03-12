/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class StatusPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundStatusResponsePacket> CLIENTBOUND_STATUS_RESPONSE = createClientbound("status_response");
/*    */   
/* 11 */   public static final PacketType<ServerboundStatusRequestPacket> SERVERBOUND_STATUS_REQUEST = createServerbound("status_request");
/*    */ 
/*    */   
/* 14 */   private static <T extends net.minecraft.network.protocol.Packet<ClientStatusPacketListener>> PacketType<T> createClientbound(String id) { return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private static <T extends net.minecraft.network.protocol.Packet<ServerStatusPacketListener>> PacketType<T> createServerbound(String id) { return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\StatusPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */