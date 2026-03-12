/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function7;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.InclusiveRange;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*    */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DualNoiseProvider
/*    */   extends NoiseProvider
/*    */ {
/* 28 */   public static final MapCodec<DualNoiseProvider> CODEC = RecordCodecBuilder.mapCodec(i -> 
/* 29 */       i.group(
/* 30 */         InclusiveRange.codec(Codec.INT, Integer.valueOf(1), Integer.valueOf(64)).fieldOf("variety").forGetter(()), NormalNoise.NoiseParameters.DIRECT_CODEC
/* 31 */         .fieldOf("slow_noise").forGetter(()), ExtraCodecs.POSITIVE_FLOAT
/* 32 */         .fieldOf("slow_scale").forGetter(()))
/* 33 */       .and(
/* 34 */         noiseProviderCodec(i))
/* 35 */       .apply(i, DualNoiseProvider::new));
/*    */   
/*    */   private final InclusiveRange<Integer> variety;
/*    */   private final NormalNoise.NoiseParameters slowNoiseParameters;
/*    */   private final float slowScale;
/*    */   private final NormalNoise slowNoise;
/*    */   
/*    */   public DualNoiseProvider(InclusiveRange<Integer> variety, NormalNoise.NoiseParameters slowNoiseParameters, float slowScale, long seed, NormalNoise.NoiseParameters parameters, float scale, List<BlockState> states) {
/* 43 */     super(seed, parameters, scale, states);
/* 44 */     this.variety = variety;
/* 45 */     this.slowNoiseParameters = slowNoiseParameters;
/* 46 */     this.slowScale = slowScale;
/* 47 */     this.slowNoise = NormalNoise.create(new WorldgenRandom(new LegacyRandomSource(seed)), slowNoiseParameters);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 52 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.DUAL_NOISE_PROVIDER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, BlockPos pos) {
/* 58 */     double varietyNoise = getSlowNoiseValue(pos);
/* 59 */     int localVariety = (int)Mth.clampedMap(varietyNoise, -1.0D, 1.0D, ((Integer)this.variety.minInclusive()).intValue(), (((Integer)this.variety.maxInclusive()).intValue() + 1));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     List<BlockState> possibleStates = Lists.newArrayListWithCapacity(localVariety);
/* 65 */     for (int i = 0; i < localVariety; i++)
/*    */     {
/* 67 */       possibleStates.add(getRandomState(this.states, getSlowNoiseValue(pos.offset(i * 54545, 0, i * 34234))));
/*    */     }
/*    */     
/* 70 */     return getRandomState(possibleStates, pos, this.scale);
/*    */   }
/*    */ 
/*    */   
/* 74 */   protected double getSlowNoiseValue(BlockPos pos) { return this.slowNoise.getValue((pos.getX() * this.slowScale), (pos.getY() * this.slowScale), (pos.getZ() * this.slowScale)); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\DualNoiseProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */