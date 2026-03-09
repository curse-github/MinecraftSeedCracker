/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public final class ClientboundHurtAnimationPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final float yaw;
/*    */   
/*  9 */   public ClientboundHurtAnimationPacket(int id, float yaw) { this.id = id; this.yaw = yaw; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundHurtAnimationPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public float yaw() { return this.yaw; }
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundHurtAnimationPacket> STREAM_CODEC = Packet.codec(ClientboundHurtAnimationPacket::write, ClientboundHurtAnimationPacket::new);
/*    */ 
/*    */   
/* 13 */   public ClientboundHurtAnimationPacket(LivingEntity entity) { this(entity.getId(), entity.getHurtDir()); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   private ClientboundHurtAnimationPacket(FriendlyByteBuf input) { this(input.readVarInt(), input.readFloat()); }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 21 */     output.writeVarInt(this.id);
/* 22 */     output.writeFloat(this.yaw);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public PacketType<ClientboundHurtAnimationPacket> type() { return GamePacketTypes.CLIENTBOUND_HURT_ANIMATION; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public void handle(ClientGamePacketListener listener) { listener.handleHurtAnimation(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundHurtAnimationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */