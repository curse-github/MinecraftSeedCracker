/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ public final class ServerboundContainerSlotStateChangedPacket extends Record implements Packet<ServerGamePacketListener> {
/*    */   private final int slotId;
/*    */   private final int containerId;
/*    */   private final boolean newState;
/*    */   
/*  8 */   public ServerboundContainerSlotStateChangedPacket(int slotId, int containerId, boolean newState) { this.slotId = slotId; this.containerId = containerId; this.newState = newState; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket; } public int slotId() { return this.slotId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundContainerSlotStateChangedPacket;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public int containerId() { return this.containerId; } public boolean newState() { return this.newState; }
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundContainerSlotStateChangedPacket> STREAM_CODEC = Packet.codec(ServerboundContainerSlotStateChangedPacket::write, ServerboundContainerSlotStateChangedPacket::new);
/*    */ 
/*    */   
/* 12 */   private ServerboundContainerSlotStateChangedPacket(FriendlyByteBuf input) { this(input.readVarInt(), input.readContainerId(), input.readBoolean()); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 16 */     output.writeVarInt(this.slotId);
/* 17 */     output.writeContainerId(this.containerId);
/* 18 */     output.writeBoolean(this.newState);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public PacketType<ServerboundContainerSlotStateChangedPacket> type() { return GamePacketTypes.SERVERBOUND_CONTAINER_SLOT_STATE_CHANGED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void handle(ServerGamePacketListener listener) { listener.handleContainerSlotStateChanged(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ServerboundContainerSlotStateChangedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */