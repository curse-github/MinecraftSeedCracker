/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PositionMoveRotation;
/*    */ 
/*    */ public final class ClientboundEntityPositionSyncPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final PositionMoveRotation values;
/*    */   private final boolean onGround;
/*    */   
/* 11 */   public ClientboundEntityPositionSyncPacket(int id, PositionMoveRotation values, boolean onGround) { this.id = id; this.values = values; this.onGround = onGround; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundEntityPositionSyncPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public PositionMoveRotation values() { return this.values; } public boolean onGround() { return this.onGround; }
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundEntityPositionSyncPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundEntityPositionSyncPacket::id, PositionMoveRotation.STREAM_CODEC, ClientboundEntityPositionSyncPacket::values, ByteBufCodecs.BOOL, ClientboundEntityPositionSyncPacket::onGround, ClientboundEntityPositionSyncPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   public static ClientboundEntityPositionSyncPacket of(Entity entity) { return new ClientboundEntityPositionSyncPacket(entity.getId(), new PositionMoveRotation(entity.trackingPosition(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot()), entity.onGround()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public PacketType<ClientboundEntityPositionSyncPacket> type() { return GamePacketTypes.CLIENTBOUND_ENTITY_POSITION_SYNC; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 30 */   public void handle(ClientGamePacketListener listener) { listener.handleEntityPositionSync(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundEntityPositionSyncPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */