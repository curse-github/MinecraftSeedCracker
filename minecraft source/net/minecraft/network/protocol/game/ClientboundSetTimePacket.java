/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ 
/*    */ public final class ClientboundSetTimePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final long gameTime;
/*    */   private final long dayTime;
/*    */   private final boolean tickDayTime;
/*    */   
/*  9 */   public ClientboundSetTimePacket(long gameTime, long dayTime, boolean tickDayTime) { this.gameTime = gameTime; this.dayTime = dayTime; this.tickDayTime = tickDayTime; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket; } public long gameTime() { return this.gameTime; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public long dayTime() { return this.dayTime; } public boolean tickDayTime() { return this.tickDayTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetTimePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.LONG, ClientboundSetTimePacket::gameTime, ByteBufCodecs.LONG, ClientboundSetTimePacket::dayTime, ByteBufCodecs.BOOL, ClientboundSetTimePacket::tickDayTime, ClientboundSetTimePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public PacketType<ClientboundSetTimePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_TIME; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void handle(ClientGamePacketListener listener) { listener.handleSetTime(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetTimePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */