/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundPickItemFromBlockPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final BlockPos pos;
/*    */   private final boolean includeData;
/*    */   
/* 10 */   public ServerboundPickItemFromBlockPacket(BlockPos pos, boolean includeData) { this.pos = pos; this.includeData = includeData; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundPickItemFromBlockPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public boolean includeData() { return this.includeData; }
/* 11 */   public static final StreamCodec<ByteBuf, ServerboundPickItemFromBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundPickItemFromBlockPacket::pos, ByteBufCodecs.BOOL, ServerboundPickItemFromBlockPacket::includeData, ServerboundPickItemFromBlockPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public PacketType<ServerboundPickItemFromBlockPacket> type() { return GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void handle(ServerGamePacketListener listener) { listener.handlePickItemFromBlock(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundPickItemFromBlockPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */