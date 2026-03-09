/*    */ package net.minecraft.network.protocol.game;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class ClientboundExplodePacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final Vec3 center;
/*    */   private final float radius;
/*    */   private final int blockCount;
/*    */   
/* 18 */   public ClientboundExplodePacket(Vec3 center, float radius, int blockCount, Optional<Vec3> playerKnockback, ParticleOptions explosionParticle, Holder<SoundEvent> explosionSound, WeightedList<ExplosionParticleInfo> blockParticles) { this.center = center; this.radius = radius; this.blockCount = blockCount; this.playerKnockback = playerKnockback; this.explosionParticle = explosionParticle; this.explosionSound = explosionSound; this.blockParticles = blockParticles; } private final Optional<Vec3> playerKnockback; private final ParticleOptions explosionParticle; private final Holder<SoundEvent> explosionSound; private final WeightedList<ExplosionParticleInfo> blockParticles; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3 center() { return this.center; } public float radius() { return this.radius; } public int blockCount() { return this.blockCount; } public Optional<Vec3> playerKnockback() { return this.playerKnockback; } public ParticleOptions explosionParticle() { return this.explosionParticle; } public Holder<SoundEvent> explosionSound() { return this.explosionSound; } public WeightedList<ExplosionParticleInfo> blockParticles() { return this.blockParticles; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundExplodePacket> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, ClientboundExplodePacket::center, ByteBufCodecs.FLOAT, ClientboundExplodePacket::radius, ByteBufCodecs.INT, ClientboundExplodePacket::blockCount, Vec3.STREAM_CODEC
/*    */ 
/*    */ 
/*    */       
/* 31 */       .apply(ByteBufCodecs::optional), ClientboundExplodePacket::playerKnockback, ParticleTypes.STREAM_CODEC, ClientboundExplodePacket::explosionParticle, SoundEvent.STREAM_CODEC, ClientboundExplodePacket::explosionSound, 
/*    */ 
/*    */       
/* 34 */       WeightedList.streamCodec(ExplosionParticleInfo.STREAM_CODEC), ClientboundExplodePacket::blockParticles, ClientboundExplodePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public PacketType<ClientboundExplodePacket> type() { return GamePacketTypes.CLIENTBOUND_EXPLODE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void handle(ClientGamePacketListener listener) { listener.handleExplosion(this); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundExplodePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */