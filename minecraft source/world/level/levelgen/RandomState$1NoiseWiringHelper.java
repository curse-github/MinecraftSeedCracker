/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.synth.BlendedNoise;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NoiseWiringHelper
/*    */   implements DensityFunction.Visitor
/*    */ {
/* 48 */   private final Map<DensityFunction, DensityFunction> wrapped = new HashMap();
/*    */ 
/*    */   
/* 51 */   private RandomSource newLegacyInstance(long seedOffset) { return new LegacyRandomSource(seed + seedOffset); }
/*    */ 
/*    */ 
/*    */   
/*    */   public DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/* 56 */     Holder<NormalNoise.NoiseParameters> noiseData = noise.noiseData();
/* 57 */     if (useLegacyInit) {
/* 58 */       if (noiseData.is(Noises.TEMPERATURE)) {
/* 59 */         NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(0L), new NormalNoise.NoiseParameters(-7, 1.0D, new double[] { 1.0D }));
/* 60 */         return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*    */       } 
/* 62 */       if (noiseData.is(Noises.VEGETATION)) {
/* 63 */         NormalNoise newNoise = NormalNoise.createLegacyNetherBiome(newLegacyInstance(1L), new NormalNoise.NoiseParameters(-7, 1.0D, new double[] { 1.0D }));
/* 64 */         return new DensityFunction.NoiseHolder(noiseData, newNoise);
/*    */       } 
/* 66 */       if (noiseData.is(Noises.SHIFT)) {
/* 67 */         NormalNoise newOffsetNoise = NormalNoise.create(RandomState.this.random.fromHashOf(Noises.SHIFT.identifier()), new NormalNoise.NoiseParameters(0, 0.0D, new double[0]));
/* 68 */         return new DensityFunction.NoiseHolder(noiseData, newOffsetNoise);
/*    */       } 
/*    */     } 
/* 71 */     NormalNoise instantiate = RandomState.this.getOrCreateNoise((ResourceKey)noiseData.unwrapKey().orElseThrow());
/* 72 */     return new DensityFunction.NoiseHolder(noiseData, instantiate);
/*    */   }
/*    */   
/*    */   private DensityFunction wrapNew(DensityFunction function) {
/* 76 */     if (function instanceof BlendedNoise) { BlendedNoise noise = (BlendedNoise)function;
/* 77 */       RandomSource terrainRandom = useLegacyInit ? newLegacyInstance(0L) : RandomState.this.random.fromHashOf(Identifier.withDefaultNamespace("terrain"));
/* 78 */       return noise.withNewRandom(terrainRandom); }
/*    */     
/* 80 */     if (function instanceof DensityFunctions.EndIslandDensityFunction) {
/* 81 */       return new DensityFunctions.EndIslandDensityFunction(seed);
/*    */     }
/* 83 */     return function;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 88 */   public DensityFunction apply(DensityFunction function) { return (DensityFunction)this.wrapped.computeIfAbsent(function, this::wrapNew); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\RandomState$1NoiseWiringHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */