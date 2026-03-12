/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ServerboundChunkBatchReceivedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final float desiredChunksPerTick;
/*    */   
/*  8 */   public ServerboundChunkBatchReceivedPacket(float desiredChunksPerTick) { this.desiredChunksPerTick = desiredChunksPerTick; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket; } public float desiredChunksPerTick() { return this.desiredChunksPerTick; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChunkBatchReceivedPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundChunkBatchReceivedPacket> STREAM_CODEC = Packet.codec(ServerboundChunkBatchReceivedPacket::write, ServerboundChunkBatchReceivedPacket::new);
/*    */ 
/*    */   
/* 12 */   private ServerboundChunkBatchReceivedPacket(FriendlyByteBuf input) { this(input.readFloat()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeFloat(this.desiredChunksPerTick); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ServerboundChunkBatchReceivedPacket> type() { return GamePacketTypes.SERVERBOUND_CHUNK_BATCH_RECEIVED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ServerGamePacketListener listener) { listener.handleChunkBatchReceived(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundChunkBatchReceivedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */