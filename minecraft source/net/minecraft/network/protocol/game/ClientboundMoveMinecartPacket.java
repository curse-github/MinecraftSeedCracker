/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundMoveMinecartPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   private final List<NewMinecartBehavior.MinecartStep> lerpSteps;
/*    */   
/* 15 */   public ClientboundMoveMinecartPacket(int entityId, List<NewMinecartBehavior.MinecartStep> lerpSteps) { this.entityId = entityId; this.lerpSteps = lerpSteps; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket; } public int entityId() { return this.entityId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundMoveMinecartPacket;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public List<NewMinecartBehavior.MinecartStep> lerpSteps() { return this.lerpSteps; }
/* 16 */   public static final StreamCodec<FriendlyByteBuf, ClientboundMoveMinecartPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundMoveMinecartPacket::entityId, NewMinecartBehavior.MinecartStep.STREAM_CODEC
/*    */       
/* 18 */       .apply(ByteBufCodecs.list()), ClientboundMoveMinecartPacket::lerpSteps, ClientboundMoveMinecartPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public PacketType<ClientboundMoveMinecartPacket> type() { return GamePacketTypes.CLIENTBOUND_MOVE_MINECART_ALONG_TRACK; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public void handle(ClientGamePacketListener listener) { listener.handleMinecartAlongTrack(this); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public Entity getEntity(Level level) { return level.getEntity(this.entityId); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundMoveMinecartPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */