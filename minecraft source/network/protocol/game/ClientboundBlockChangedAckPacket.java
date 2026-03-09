/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundBlockChangedAckPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int sequence;
/*    */   
/*  8 */   public ClientboundBlockChangedAckPacket(int sequence) { this.sequence = sequence; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket; } public int sequence() { return this.sequence; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundBlockChangedAckPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundBlockChangedAckPacket> STREAM_CODEC = Packet.codec(ClientboundBlockChangedAckPacket::write, ClientboundBlockChangedAckPacket::new);
/*    */ 
/*    */   
/* 12 */   private ClientboundBlockChangedAckPacket(FriendlyByteBuf input) { this(input.readVarInt()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.sequence); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ClientboundBlockChangedAckPacket> type() { return GamePacketTypes.CLIENTBOUND_BLOCK_CHANGED_ACK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ClientGamePacketListener listener) { listener.handleBlockChangedAck(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundBlockChangedAckPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */