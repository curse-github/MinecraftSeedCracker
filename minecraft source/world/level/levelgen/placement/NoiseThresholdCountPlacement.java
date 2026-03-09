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
/*    */ public class NoiseThresholdCountPlacement
/*    */   extends RepeatingPlacement
/*    */ {
/* 16 */   public static final MapCodec<NoiseThresholdCountPlacement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.DOUBLE
/* 17 */         .fieldOf("noise_level").forGetter(()), Codec.INT
/* 18 */         .fieldOf("below_noise").forGetter(()), Codec.INT
/* 19 */         .fieldOf("above_noise").forGetter(()))
/* 20 */       .apply(i, NoiseThresholdCountPlacement::new));
/*    */   
/*    */   private final double noiseLevel;
/*    */   
/*    */   private final int belowNoise;
/*    */   private final int aboveNoise;
/*    */   
/*    */   private NoiseThresholdCountPlacement(double noiseLevel, int belowNoise, int aboveNoise) {
/* 28 */     this.noiseLevel = noiseLevel;
/* 29 */     this.belowNoise = belowNoise;
/* 30 */     this.aboveNoise = aboveNoise;
/*    */   }
/*    */ 
/*    */   
/* 34 */   public static NoiseThresholdCountPlacement of(double noiseLevel, int belowNoise, int aboveNoise) { return new NoiseThresholdCountPlacement(noiseLevel, belowNoise, aboveNoise); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int count(RandomSource random, BlockPos origin) {
/* 40 */     double flowerNoise = Biome.BIOME_INFO_NOISE.getValue(origin.getX() / 200.0D, origin.getZ() / 200.0D, false);
/* 41 */     return (flowerNoise < this.noiseLevel) ? this.belowNoise : this.aboveNoise;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 46 */   public PlacementModifierType<?> type() { return PlacementModifierType.NOISE_THRESHOLD_COUNT; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\placement\NoiseThresholdCountPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */