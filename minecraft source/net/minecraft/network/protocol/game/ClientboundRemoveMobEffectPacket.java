/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class ClientboundRemoveMobEffectPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int entityId;
/*    */   private final Holder<MobEffect> effect;
/*    */   
/* 14 */   public ClientboundRemoveMobEffectPacket(int entityId, Holder<MobEffect> effect) { this.entityId = entityId; this.effect = effect; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket; } public int entityId() { return this.entityId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<MobEffect> effect() { return this.effect; }
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundRemoveMobEffectPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundRemoveMobEffectPacket::entityId, MobEffect.STREAM_CODEC, ClientboundRemoveMobEffectPacket::effect, ClientboundRemoveMobEffectPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public PacketType<ClientboundRemoveMobEffectPacket> type() { return GamePacketTypes.CLIENTBOUND_REMOVE_MOB_EFFECT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void handle(ClientGamePacketListener listener) { listener.handleRemoveMobEffect(this); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public Entity getEntity(Level level) { return level.getEntity(this.entityId); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundRemoveMobEffectPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */