/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class TrailParticleOption extends Record implements ParticleOptions {
/*    */   private final Vec3 target;
/*    */   private final int color;
/*    */   private final int duration;
/*    */   
/* 11 */   public TrailParticleOption(Vec3 target, int color, int duration) { this.target = target; this.color = color; this.duration = duration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/TrailParticleOption;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption; } public Vec3 target() { return this.target; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/TrailParticleOption;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/TrailParticleOption; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/TrailParticleOption;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/TrailParticleOption;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public int color() { return this.color; } public int duration() { return this.duration; }
/* 12 */   public static final MapCodec<TrailParticleOption> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Vec3.CODEC
/* 13 */         .fieldOf("target").forGetter(TrailParticleOption::target), ExtraCodecs.RGB_COLOR_CODEC
/* 14 */         .fieldOf("color").forGetter(TrailParticleOption::color), ExtraCodecs.POSITIVE_INT
/* 15 */         .fieldOf("duration").forGetter(TrailParticleOption::duration))
/* 16 */       .apply(i, TrailParticleOption::new));
/*    */   
/* 18 */   public static final StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC, TrailParticleOption::target, ByteBufCodecs.INT, TrailParticleOption::color, ByteBufCodecs.VAR_INT, TrailParticleOption::duration, TrailParticleOption::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 27 */   public ParticleType<TrailParticleOption> getType() { return ParticleTypes.TRAIL; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\TrailParticleOption.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */