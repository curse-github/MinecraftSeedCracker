/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
/*    */ 
/*    */ public class OreConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<OreConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 13 */         Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter(()), 
/* 14 */         Codec.intRange(0, 64).fieldOf("size").forGetter(()), 
/* 15 */         Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter(()))
/* 16 */       .apply(i, OreConfiguration::new));
/*    */   
/*    */   public final List<TargetBlockState> targetStates;
/*    */   public final int size;
/*    */   public final float discardChanceOnAirExposure;
/*    */   
/*    */   public OreConfiguration(List<TargetBlockState> targetBlockStates, int size, float discardChanceOnAirExposure) {
/* 23 */     this.size = size;
/* 24 */     this.targetStates = targetBlockStates;
/* 25 */     this.discardChanceOnAirExposure = discardChanceOnAirExposure;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public OreConfiguration(List<TargetBlockState> targetBlockStates, int size) { this(targetBlockStates, size, 0.0F); }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public OreConfiguration(RuleTest target, BlockState state, int size, float discardChanceOnAirExposure) { this(ImmutableList.of(new TargetBlockState(target, state)), size, discardChanceOnAirExposure); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public OreConfiguration(RuleTest target, BlockState state, int size) { this(ImmutableList.of(new TargetBlockState(target, state)), size, 0.0F); }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static TargetBlockState target(RuleTest rule, BlockState state) { return new TargetBlockState(rule, state); }
/*    */   
/*    */   public static class TargetBlockState
/*    */   {
/* 45 */     public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create(i -> i.group(RuleTest.CODEC
/* 46 */           .fieldOf("target").forGetter(()), BlockState.CODEC
/* 47 */           .fieldOf("state").forGetter(()))
/* 48 */         .apply(i, TargetBlockState::new));
/*    */     
/*    */     public final RuleTest target;
/*    */     public final BlockState state;
/*    */     
/*    */     private TargetBlockState(RuleTest target, BlockState state) {
/* 54 */       this.target = target;
/* 55 */       this.state = state;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\OreConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */