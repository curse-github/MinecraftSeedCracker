/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ 
/*    */ public final class ClientboundTickingStepPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int tickSteps;
/*    */   
/*  9 */   public ClientboundTickingStepPacket(int tickSteps) { this.tickSteps = tickSteps; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket; } public int tickSteps() { return this.tickSteps; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundTickingStepPacket> STREAM_CODEC = Packet.codec(ClientboundTickingStepPacket::write, ClientboundTickingStepPacket::new);
/*    */ 
/*    */   
/* 13 */   private ClientboundTickingStepPacket(FriendlyByteBuf input) { this(input.readVarInt()); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static ClientboundTickingStepPacket from(TickRateManager manager) { return new ClientboundTickingStepPacket(manager.frozenTicksToRun()); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.tickSteps); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public PacketType<ClientboundTickingStepPacket> type() { return GamePacketTypes.CLIENTBOUND_TICKING_STEP; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public void handle(ClientGamePacketListener listener) { listener.handleTickingStep(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundTickingStepPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */