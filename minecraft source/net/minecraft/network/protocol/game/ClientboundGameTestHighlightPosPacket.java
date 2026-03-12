/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundGameTestHighlightPosPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final BlockPos absolutePos;
/*    */   private final BlockPos relativePos;
/*    */   
/* 10 */   public ClientboundGameTestHighlightPosPacket(BlockPos absolutePos, BlockPos relativePos) { this.absolutePos = absolutePos; this.relativePos = relativePos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket; } public BlockPos absolutePos() { return this.absolutePos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundGameTestHighlightPosPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos relativePos() { return this.relativePos; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundGameTestHighlightPosPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::absolutePos, BlockPos.STREAM_CODEC, ClientboundGameTestHighlightPosPacket::relativePos, ClientboundGameTestHighlightPosPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundGameTestHighlightPosPacket> type() { return GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleGameTestHighlightPos(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundGameTestHighlightPosPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */