/*    */ package net.minecraft.network.protocol.common;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class ClientboundStoreCookiePacket extends Record implements Packet<ClientCommonPacketListener> {
/*    */   private final Identifier key;
/*    */   private final byte[] payload;
/*    */   
/* 11 */   public ClientboundStoreCookiePacket(Identifier key, byte[] payload) { this.key = key; this.payload = payload; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket; } public Identifier key() { return this.key; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundStoreCookiePacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public byte[] payload() { return this.payload; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final StreamCodec<FriendlyByteBuf, ClientboundStoreCookiePacket> STREAM_CODEC = Packet.codec(ClientboundStoreCookiePacket::write, ClientboundStoreCookiePacket::new);
/*    */   
/*    */   private static final int MAX_PAYLOAD_SIZE = 5120;
/* 18 */   public static final StreamCodec<ByteBuf, byte[]> PAYLOAD_STREAM_CODEC = ByteBufCodecs.byteArray(5120);
/*    */ 
/*    */   
/* 21 */   private ClientboundStoreCookiePacket(FriendlyByteBuf input) { this(input.readIdentifier(), (byte[])PAYLOAD_STREAM_CODEC.decode(input)); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 25 */     output.writeIdentifier(this.key);
/* 26 */     PAYLOAD_STREAM_CODEC.encode(output, this.payload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public PacketType<ClientboundStoreCookiePacket> type() { return CommonPacketTypes.CLIENTBOUND_STORE_COOKIE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public void handle(ClientCommonPacketListener listener) { listener.handleStoreCookie(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\common\ClientboundStoreCookiePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */