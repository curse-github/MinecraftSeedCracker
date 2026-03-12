/*    */ package net.minecraft.world.level.levelgen.placement;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ 
/*    */ 
/*    */ public class NoiseBasedCountPlacement
/*    */   extends RepeatingPlacement
/*    */ {
/* 16 */   public static final MapCodec<NoiseBasedCountPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 17 */         .fieldOf("noise_to_count_ratio").forGetter(()), Codec.DOUBLE
/* 18 */         .fieldOf("noise_factor").forGetter(()), Codec.DOUBLE
/* 19 */         .fieldOf("noise_offset").orElse(Double.valueOf(0.0D)).forGetter(()))
/* 20 */       .apply(i, NoiseBasedCountPlacement::new));
/*    */   
/*    */   private final int noiseToCountRatio;
/*    */   
/*    */   private final double noiseFactor;
/*    */   
/*    */   private final double noiseOffset;
/*    */   
/*    */   private NoiseBasedCountPlacement(int noiseToCountRatio, double noiseFactor, double noiseOffset) {
/* 29 */     this.noiseToCountRatio = noiseToCountRatio;
/* 30 */     this.noiseFactor = noiseFactor;
/* 31 */     this.noiseOffset = noiseOffset;
/*    */   }
/*    */ 
/*    */   
/* 35 */   public static NoiseBasedCountPlacement of(int noiseToCountRatio, double noiseFactor, double noiseOffset) { return new NoiseBasedCountPlacement(noiseToCountRatio, noiseFactor, noiseOffset); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 40 */     double flowerNoise = Biome.BIOME_INFO_NOISE.getValue(origin.getX() / this.noiseFactor, origin.getZ() / this.noiseFactor, false);
/* 41 */     return (int)Math.ceil((flowerNoise + this.noiseOffset) * this.noiseToCountRatio);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public PlacementModifierType<?> type() { return PlacementModifierType.NOISE_BASED_COUNT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\NoiseBasedCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */