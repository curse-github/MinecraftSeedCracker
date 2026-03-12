/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundChunkBatchFinishedPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int batchSize;
/*    */   
/*  8 */   public ClientboundChunkBatchFinishedPacket(int batchSize) { this.batchSize = batchSize; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket; } public int batchSize() { return this.batchSize; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunkBatchFinishedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundChunkBatchFinishedPacket> STREAM_CODEC = Packet.codec(ClientboundChunkBatchFinishedPacket::write, ClientboundChunkBatchFinishedPacket::new);
/*    */ 
/*    */   
/* 12 */   private ClientboundChunkBatchFinishedPacket(FriendlyByteBuf input) { this(input.readVarInt()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.batchSize); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ClientboundChunkBatchFinishedPacket> type() { return GamePacketTypes.CLIENTBOUND_CHUNK_BATCH_FINISHED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ClientGamePacketListener listener) { listener.handleChunkBatchFinished(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChunkBatchFinishedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */