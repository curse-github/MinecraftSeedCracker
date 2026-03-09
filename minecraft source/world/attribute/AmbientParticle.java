/*    */ package net.minecraft.world.attribute;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ 
/*    */ public final class AmbientParticle extends Record {
/*    */   private final ParticleOptions particle;
/*    */   private final float probability;
/*    */   
/* 11 */   public AmbientParticle(ParticleOptions particle, float probability) { this.particle = particle; this.probability = probability; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/AmbientParticle;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle; } public ParticleOptions particle() { return this.particle; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AmbientParticle;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/AmbientParticle; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AmbientParticle;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/AmbientParticle;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public float probability() { return this.probability; }
/* 12 */   public static final Codec<AmbientParticle> CODEC = RecordCodecBuilder.create(i -> i.group(ParticleTypes.CODEC
/* 13 */         .fieldOf("particle").forGetter(()), 
/* 14 */         Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()))
/* 15 */       .apply(i, AmbientParticle::new));
/*    */ 
/*    */   
/* 18 */   public boolean canSpawn(RandomSource random) { return (random.nextFloat() <= this.probability); }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static List<AmbientParticle> of(ParticleOptions particle, float probability) { return List.of(new AmbientParticle(particle, probability)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\AmbientParticle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */