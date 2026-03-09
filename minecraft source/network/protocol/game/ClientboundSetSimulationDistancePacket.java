/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundSetSimulationDistancePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int simulationDistance;
/*    */   
/*  8 */   public ClientboundSetSimulationDistancePacket(int simulationDistance) { this.simulationDistance = simulationDistance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket; } public int simulationDistance() { return this.simulationDistance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetSimulationDistancePacket;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetSimulationDistancePacket> STREAM_CODEC = Packet.codec(ClientboundSetSimulationDistancePacket::write, ClientboundSetSimulationDistancePacket::new);
/*    */ 
/*    */   
/* 12 */   private ClientboundSetSimulationDistancePacket(FriendlyByteBuf input) { this(input.readVarInt()); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   private void write(FriendlyByteBuf output) { output.writeVarInt(this.simulationDistance); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   public PacketType<ClientboundSetSimulationDistancePacket> type() { return GamePacketTypes.CLIENTBOUND_SET_SIMULATION_DISTANCE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public void handle(ClientGamePacketListener listener) { listener.handleSetSimulationDistance(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundSetSimulationDistancePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */