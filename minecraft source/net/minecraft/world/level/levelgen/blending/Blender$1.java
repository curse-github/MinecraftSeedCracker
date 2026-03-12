/*    */ package net.minecraft.world.level.levelgen.blending;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import net.minecraft.world.level.biome.BiomeResolver;
/*    */ import net.minecraft.world.level.levelgen.DensityFunction;
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
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends Blender
/*    */ {
/* 39 */   null(Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData, Long2ObjectOpenHashMap<BlendingData> densityBlendingData) { super(heightAndBiomeBlendingData, densityBlendingData); }
/*    */ 
/*    */   
/* 42 */   public Blender.BlendingOutput blendOffsetAndFactor(int blockX, int blockZ) { return new Blender.BlendingOutput(1.0D, 0.0D); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public double blendDensity(DensityFunction.FunctionContext context, double noiseValue) { return noiseValue; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public BiomeResolver getBiomeResolver(BiomeResolver biomeResolver) { return biomeResolver; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\blending\Blender$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */