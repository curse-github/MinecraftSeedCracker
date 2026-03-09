/*    */ package net.minecraft.network.protocol.cookie;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundCookieRequestPacket extends Record implements Packet<ClientCookiePacketListener> {
/*  9 */   public ClientboundCookieRequestPacket(Identifier key) { this.key = key; } private final Identifier key; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket; } public Identifier key() { return this.key; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/cookie/ClientboundCookieRequestPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundCookieRequestPacket> STREAM_CODEC = Packet.codec(ClientboundCookieRequestPacket::write, ClientboundCookieRequestPacket::new);
/*    */ 
/*    */   
/* 15 */   private ClientboundCookieRequestPacket(FriendlyByteBuf input) { this(input.readIdentifier()); }
/*    */ 
/*    */ 
/*    */   
/* 19 */   private void write(FriendlyByteBuf output) { output.writeIdentifier(this.key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ClientboundCookieRequestPacket> type() { return CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ClientCookiePacketListener listener) { listener.handleRequestCookie(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\cookie\ClientboundCookieRequestPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */