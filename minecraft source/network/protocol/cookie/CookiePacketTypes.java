/*    */ package net.minecraft.network.protocol.cookie;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CookiePacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundCookieRequestPacket> CLIENTBOUND_COOKIE_REQUEST = createClientbound("cookie_request");
/*    */   
/* 11 */   public static final PacketType<ServerboundCookieResponsePacket> SERVERBOUND_COOKIE_RESPONSE = createServerbound("cookie_response");
/*    */ 
/*    */   
/* 14 */   private static <T extends net.minecraft.network.protocol.Packet<ClientCookiePacketListener>> PacketType<T> createClientbound(String id) { return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   private static <T extends net.minecraft.network.protocol.Packet<ServerCookiePacketListener>> PacketType<T> createServerbound(String id) { return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\cookie\CookiePacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */