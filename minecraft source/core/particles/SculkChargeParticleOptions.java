/*    */ package net.minecraft.core.particles;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class SculkChargeParticleOptions extends Record implements ParticleOptions {
/*    */   private final float roll;
/*    */   
/* 10 */   public SculkChargeParticleOptions(float roll) { this.roll = roll; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions; } public float roll() { return this.roll; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/particles/SculkChargeParticleOptions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/particles/SculkChargeParticleOptions;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 11 */   public static final MapCodec<SculkChargeParticleOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.FLOAT
/* 12 */         .fieldOf("roll").forGetter(()))
/* 13 */       .apply(i, SculkChargeParticleOptions::new));
/*    */   
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, SculkChargeParticleOptions> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, o -> 
/* 16 */       Float.valueOf(o.roll), SculkChargeParticleOptions::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public ParticleType<SculkChargeParticleOptions> getType() { return ParticleTypes.SCULK_CHARGE; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\particles\SculkChargeParticleOptions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */