/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NoiseHolder
/*    */   extends Record
/*    */ {
/*    */   private final Holder<NormalNoise.NoiseParameters> noiseData;
/*    */   private final NormalNoise noise;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #46	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 46 */   public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData, NormalNoise noise) { this.noiseData = noiseData; this.noise = noise; } public Holder<NormalNoise.NoiseParameters> noiseData() { return this.noiseData; } public NormalNoise noise() { return this.noise; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 51 */   public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData) { this(noiseData, null); }
/*    */ 
/*    */   
/* 54 */   public static final Codec<NoiseHolder> CODEC = NormalNoise.NoiseParameters.CODEC.xmap(data -> new NoiseHolder(data, null), NoiseHolder::noiseData);
/*    */ 
/*    */   
/* 57 */   public double getValue(double x, double y, double z) { return (this.noise == null) ? 0.0D : this.noise.getValue(x, y, z); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public double maxValue() { return (this.noise == null) ? 2.0D : this.noise.maxValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\DensityFunction$NoiseHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */