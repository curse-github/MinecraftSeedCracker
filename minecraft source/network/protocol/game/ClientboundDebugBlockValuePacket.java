/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ 
/*    */ public final class ClientboundDebugBlockValuePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final BlockPos blockPos;
/*    */   private final DebugSubscription.Update<?> update;
/*    */   
/* 10 */   public ClientboundDebugBlockValuePacket(BlockPos blockPos, DebugSubscription.Update<?> update) { this.blockPos = blockPos; this.update = update; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket; } public BlockPos blockPos() { return this.blockPos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugBlockValuePacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public DebugSubscription.Update<?> update() { return this.update; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugBlockValuePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundDebugBlockValuePacket::blockPos, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugBlockValuePacket::update, ClientboundDebugBlockValuePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundDebugBlockValuePacket> type() { return GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleDebugBlockValue(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundDebugBlockValuePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */