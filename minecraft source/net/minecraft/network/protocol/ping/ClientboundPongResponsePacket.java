/*    */ package net.minecraft.network.protocol.ping;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundPongResponsePacket extends Record implements Packet<ClientPongPacketListener> {
/*    */   private final long time;
/*    */   
/*  8 */   public ClientboundPongResponsePacket(long time) { this.time = time; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket; } public long time() { return this.time; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/ping/ClientboundPongResponsePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPongResponsePacket> STREAM_CODEC = Packet.codec(ClientboundPongResponsePacket::write, ClientboundPongResponsePacket::new);
/*    */ 
/*    */   
/* 12 */   private ClientboundPongResponsePacket(FriendlyByteBuf input) { this(input.readLong()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeLong(this.time); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ClientboundPongResponsePacket> type() { return PingPacketTypes.CLIENTBOUND_PONG_RESPONSE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ClientPongPacketListener listener) { listener.handlePongResponse(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\ping\ClientboundPongResponsePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */