/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.LevelChunk;
/*    */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*    */ 
/*    */ public final class ChunkBiomeData
/*    */   extends Record
/*    */ {
/*    */   private final ChunkPos pos;
/*    */   private final byte[] buffer;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundChunksBiomesPacket$ChunkBiomeData;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 22 */   public ChunkBiomeData(ChunkPos pos, byte[] buffer) { this.pos = pos; this.buffer = buffer; } public ChunkPos pos() { return this.pos; } public byte[] buffer() { return this.buffer; }
/*    */ 
/*    */ 
/*    */   
/*    */   public ChunkBiomeData(LevelChunk chunk) {
/* 27 */     this(chunk.getPos(), new byte[calculateChunkSize(chunk)]);
/* 28 */     extractChunkData(new FriendlyByteBuf(getWriteBuffer()), chunk);
/*    */   }
/*    */   
/*    */   public ChunkBiomeData(FriendlyByteBuf input) {
/* 32 */     this(input
/* 33 */         .readChunkPos(), input
/* 34 */         .readByteArray(2097152));
/*    */   }
/*    */ 
/*    */   
/*    */   private static int calculateChunkSize(LevelChunk chunk) {
/* 39 */     int total = 0;
/*    */     
/* 41 */     for (LevelChunkSection section : chunk.getSections()) {
/* 42 */       total += section.getBiomes().getSerializedSize();
/*    */     }
/*    */     
/* 45 */     return total;
/*    */   }
/*    */ 
/*    */   
/* 49 */   public FriendlyByteBuf getReadBuffer() { return new FriendlyByteBuf(Unpooled.wrappedBuffer(this.buffer)); }
/*    */ 
/*    */   
/*    */   private ByteBuf getWriteBuffer() {
/* 53 */     ByteBuf buffer = Unpooled.wrappedBuffer(this.buffer);
/* 54 */     buffer.writerIndex(0);
/* 55 */     return buffer;
/*    */   }
/*    */   
/*    */   public static void extractChunkData(FriendlyByteBuf buffer, LevelChunk chunk) {
/* 59 */     for (LevelChunkSection section : chunk.getSections()) {
/* 60 */       section.getBiomes().write(buffer);
/*    */     }
/* 62 */     if (buffer.writerIndex() != buffer.capacity()) {
/* 63 */       throw new IllegalStateException("Didn't fill biome buffer: expected " + buffer.capacity() + " bytes, got " + buffer.writerIndex());
/*    */     }
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 68 */     output.writeChunkPos(this.pos);
/* 69 */     output.writeByteArray(this.buffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChunksBiomesPacket$ChunkBiomeData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */