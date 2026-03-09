/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.Difficulty;
/*    */ 
/*    */ public final class ClientboundChangeDifficultyPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Difficulty difficulty;
/*    */   private final boolean locked;
/*    */   
/* 10 */   public ClientboundChangeDifficultyPacket(Difficulty difficulty, boolean locked) { this.difficulty = difficulty; this.locked = locked; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket; } public Difficulty difficulty() { return this.difficulty; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public boolean locked() { return this.locked; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundChangeDifficultyPacket> STREAM_CODEC = StreamCodec.composite(Difficulty.STREAM_CODEC, ClientboundChangeDifficultyPacket::difficulty, ByteBufCodecs.BOOL, ClientboundChangeDifficultyPacket::locked, ClientboundChangeDifficultyPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public PacketType<ClientboundChangeDifficultyPacket> type() { return GamePacketTypes.CLIENTBOUND_CHANGE_DIFFICULTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public void handle(ClientGamePacketListener listener) { listener.handleChangeDifficulty(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundChangeDifficultyPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */