/*    */ package net.minecraft.world.level.levelgen.feature.stateproviders;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function8;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoiseThresholdProvider
/*    */   extends NoiseBasedStateProvider
/*    */ {
/* 26 */   public static final MapCodec<NoiseThresholdProvider> CODEC = RecordCodecBuilder.mapCodec(i -> 
/* 27 */       noiseCodec(i).and(i.group(
/* 28 */           Codec.floatRange(-1.0F, 1.0F).fieldOf("threshold").forGetter(()), 
/* 29 */           Codec.floatRange(0.0F, 1.0F).fieldOf("high_chance").forGetter(()), BlockState.CODEC
/* 30 */           .fieldOf("default_state").forGetter(()), 
/* 31 */           ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("low_states").forGetter(()), 
/* 32 */           ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("high_states").forGetter(())))
/*    */       
/* 34 */       .apply(i, NoiseThresholdProvider::new));
/*    */   
/*    */   private final float threshold;
/*    */   private final float highChance;
/*    */   private final BlockState defaultState;
/*    */   private final List<BlockState> lowStates;
/*    */   private final List<BlockState> highStates;
/*    */   
/*    */   public NoiseThresholdProvider(long seed, NormalNoise.NoiseParameters parameters, float scale, float threshold, float highChance, BlockState defaultState, List<BlockState> lowStates, List<BlockState> highStates) {
/* 43 */     super(seed, parameters, scale);
/* 44 */     this.threshold = threshold;
/* 45 */     this.highChance = highChance;
/* 46 */     this.defaultState = defaultState;
/* 47 */     this.lowStates = lowStates;
/* 48 */     this.highStates = highStates;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 53 */   protected BlockStateProviderType<?> type() { return BlockStateProviderType.NOISE_THRESHOLD_PROVIDER; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getState(RandomSource random, BlockPos pos) {
/* 59 */     double localValue = getNoiseValue(pos, this.scale);
/* 60 */     if (localValue < this.threshold) {
/* 61 */       return (BlockState)Util.getRandom(this.lowStates, random);
/*    */     }
/*    */     
/* 64 */     if (random.nextFloat() < this.highChance) {
/* 65 */       return (BlockState)Util.getRandom(this.highStates, random);
/*    */     }
/*    */     
/* 68 */     return this.defaultState;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\stateproviders\NoiseThresholdProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */