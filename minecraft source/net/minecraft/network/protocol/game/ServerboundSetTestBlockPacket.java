/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.properties.TestBlockMode;
/*    */ 
/*    */ public final class ServerboundSetTestBlockPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final BlockPos position;
/*    */   private final TestBlockMode mode;
/*    */   private final String message;
/*    */   
/* 11 */   public ServerboundSetTestBlockPacket(BlockPos position, TestBlockMode mode, String message) { this.position = position; this.mode = mode; this.message = message; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket; } public BlockPos position() { return this.position; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSetTestBlockPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public TestBlockMode mode() { return this.mode; } public String message() { return this.message; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ServerboundSetTestBlockPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ServerboundSetTestBlockPacket::position, TestBlockMode.STREAM_CODEC, ServerboundSetTestBlockPacket::mode, ByteBufCodecs.STRING_UTF8, ServerboundSetTestBlockPacket::message, ServerboundSetTestBlockPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ServerboundSetTestBlockPacket> type() { return GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ServerGamePacketListener listener) { listener.handleSetTestBlock(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundSetTestBlockPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */