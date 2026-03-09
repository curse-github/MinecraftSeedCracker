/*    */ package net.minecraft.network.protocol.cookie;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ServerboundCookieResponsePacket extends Record implements Packet<ServerCookiePacketListener> {
/*    */   private final Identifier key;
/*    */   private final byte[] payload;
/*    */   
/* 11 */   public ServerboundCookieResponsePacket(Identifier key, byte[] payload) { this.key = key; this.payload = payload; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket; } public Identifier key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/cookie/ServerboundCookieResponsePacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public byte[] payload() { return this.payload; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCookieResponsePacket> STREAM_CODEC = Packet.codec(ServerboundCookieResponsePacket::write, ServerboundCookieResponsePacket::new);
/*    */ 
/*    */   
/* 15 */   private ServerboundCookieResponsePacket(FriendlyByteBuf input) { this(input.readIdentifier(), (byte[])input.readNullable(ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC)); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 19 */     output.writeIdentifier(this.key);
/* 20 */     output.writeNullable(this.payload, ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ServerboundCookieResponsePacket> type() { return CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ServerCookiePacketListener listener) { listener.handleCookieResponse(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\cookie\ServerboundCookieResponsePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */