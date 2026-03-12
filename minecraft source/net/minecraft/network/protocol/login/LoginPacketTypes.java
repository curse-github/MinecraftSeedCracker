/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class LoginPacketTypes
/*    */ {
/*  9 */   public static final PacketType<ClientboundCustomQueryPacket> CLIENTBOUND_CUSTOM_QUERY = createClientbound("custom_query");
/* 10 */   public static final PacketType<ClientboundLoginFinishedPacket> CLIENTBOUND_LOGIN_FINISHED = createClientbound("login_finished");
/* 11 */   public static final PacketType<ClientboundHelloPacket> CLIENTBOUND_HELLO = createClientbound("hello");
/* 12 */   public static final PacketType<ClientboundLoginCompressionPacket> CLIENTBOUND_LOGIN_COMPRESSION = createClientbound("login_compression");
/* 13 */   public static final PacketType<ClientboundLoginDisconnectPacket> CLIENTBOUND_LOGIN_DISCONNECT = createClientbound("login_disconnect");
/*    */   
/* 15 */   public static final PacketType<ServerboundCustomQueryAnswerPacket> SERVERBOUND_CUSTOM_QUERY_ANSWER = createServerbound("custom_query_answer");
/* 16 */   public static final PacketType<ServerboundHelloPacket> SERVERBOUND_HELLO = createServerbound("hello");
/* 17 */   public static final PacketType<ServerboundKeyPacket> SERVERBOUND_KEY = createServerbound("key");
/* 18 */   public static final PacketType<ServerboundLoginAcknowledgedPacket> SERVERBOUND_LOGIN_ACKNOWLEDGED = createServerbound("login_acknowledged");
/*    */ 
/*    */   
/* 21 */   private static <T extends net.minecraft.network.protocol.Packet<ClientLoginPacketListener>> PacketType<T> createClientbound(String id) { return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ 
/*    */ 
/*    */   
/* 25 */   private static <T extends net.minecraft.network.protocol.Packet<ServerLoginPacketListener>> PacketType<T> createServerbound(String id) { return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\LoginPacketTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */