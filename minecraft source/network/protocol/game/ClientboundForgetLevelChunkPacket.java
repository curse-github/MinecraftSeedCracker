/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class ClientboundForgetLevelChunkPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final ChunkPos pos;
/*    */   
/*  9 */   public ClientboundForgetLevelChunkPacket(ChunkPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket; } public ChunkPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundForgetLevelChunkPacket> STREAM_CODEC = Packet.codec(ClientboundForgetLevelChunkPacket::write, ClientboundForgetLevelChunkPacket::new);
/*    */ 
/*    */   
/* 13 */   private ClientboundForgetLevelChunkPacket(FriendlyByteBuf input) { this(input.readChunkPos()); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private void write(FriendlyByteBuf output) { output.writeChunkPos(this.pos); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundForgetLevelChunkPacket> type() { return GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleForgetLevelChunk(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundForgetLevelChunkPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */