/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ 
/*    */ public final class ClientboundTickingStatePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final float tickRate;
/*    */   private final boolean isFrozen;
/*    */   
/*  9 */   public ClientboundTickingStatePacket(float tickRate, boolean isFrozen) { this.tickRate = tickRate; this.isFrozen = isFrozen; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket; } public float tickRate() { return this.tickRate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean isFrozen() { return this.isFrozen; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTickingStatePacket> STREAM_CODEC = Packet.codec(ClientboundTickingStatePacket::write, ClientboundTickingStatePacket::new);
/*    */   
/*    */   private ClientboundTickingStatePacket(FriendlyByteBuf input) {
/* 13 */     this(input
/* 14 */         .readFloat(), input
/* 15 */         .readBoolean());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static ClientboundTickingStatePacket from(TickRateManager manager) { return new ClientboundTickingStatePacket(manager.tickrate(), manager.isFrozen()); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeFloat(this.tickRate);
/* 25 */     output.writeBoolean(this.isFrozen);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public PacketType<ClientboundTickingStatePacket> type() { return GamePacketTypes.CLIENTBOUND_TICKING_STATE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   public void handle(ClientGamePacketListener listener) { listener.handleTickingState(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTickingStatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */