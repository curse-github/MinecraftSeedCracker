/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ExplosionParticleInfo extends Record {
/*    */   private final ParticleOptions particle;
/*    */   private final float scaling;
/*    */   private final float speed;
/*    */   
/* 10 */   public ExplosionParticleInfo(ParticleOptions particle, float scaling, float speed) { this.particle = particle; this.scaling = scaling; this.speed = speed; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo; } public ParticleOptions particle() { return this.particle; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/ExplosionParticleInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/ExplosionParticleInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/ExplosionParticleInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public float scaling() { return this.scaling; } public float speed() { return this.speed; }
/* 11 */   public static final MapCodec<ExplosionParticleInfo> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(ParticleTypes.CODEC
/* 12 */         .fieldOf("particle").forGetter(ExplosionParticleInfo::particle), Codec.FLOAT
/* 13 */         .optionalFieldOf("scaling", Float.valueOf(1.0F)).forGetter(ExplosionParticleInfo::scaling), Codec.FLOAT
/* 14 */         .optionalFieldOf("speed", Float.valueOf(1.0F)).forGetter(ExplosionParticleInfo::speed))
/* 15 */       .apply(i, ExplosionParticleInfo::new));
/*    */   
/* 17 */   public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionParticleInfo> STREAM_CODEC = StreamCodec.composite(ParticleTypes.STREAM_CODEC, ExplosionParticleInfo::particle, ByteBufCodecs.FLOAT, ExplosionParticleInfo::scaling, ByteBufCodecs.FLOAT, ExplosionParticleInfo::speed, ExplosionParticleInfo::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\ExplosionParticleInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */