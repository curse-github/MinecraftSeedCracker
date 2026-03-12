/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class ClientboundDebugChunkValuePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final ChunkPos chunkPos;
/*    */   private final DebugSubscription.Update<?> update;
/*    */   
/* 10 */   public ClientboundDebugChunkValuePacket(ChunkPos chunkPos, DebugSubscription.Update<?> update) { this.chunkPos = chunkPos; this.update = update; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket; } public ChunkPos chunkPos() { return this.chunkPos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugChunkValuePacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public DebugSubscription.Update<?> update() { return this.update; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugChunkValuePacket> STREAM_CODEC = StreamCodec.composite(ChunkPos.STREAM_CODEC, ClientboundDebugChunkValuePacket::chunkPos, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugChunkValuePacket::update, ClientboundDebugChunkValuePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundDebugChunkValuePacket> type() { return GamePacketTypes.CLIENTBOUND_DEBUG_CHUNK_VALUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleDebugChunkValue(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDebugChunkValuePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */