/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class HandshakePacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientIntentionPacket> CLIENT_INTENTION = createServerbound("intention");
/*    */ 
/*    */   
/* 12 */   private static <T extends net.minecraft.network.protocol.Packet<ServerHandshakePacketListener>> PacketType<T> createServerbound(String id) { return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\handshake\HandshakePacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */