/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
/*    */ 
/*    */ public class ReplaceBlockConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<ReplaceBlockConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 13 */         Codec.list(OreConfiguration.TargetBlockState.CODEC).fieldOf("targets").forGetter(()))
/* 14 */       .apply(i, ReplaceBlockConfiguration::new));
/*    */   
/*    */   public final List<OreConfiguration.TargetBlockState> targetStates;
/*    */ 
/*    */   
/* 19 */   public ReplaceBlockConfiguration(BlockState targetState, BlockState state) { this(ImmutableList.of(OreConfiguration.target(new BlockStateMatchTest(targetState), state))); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public ReplaceBlockConfiguration(List<OreConfiguration.TargetBlockState> targetBlockStates) { this.targetStates = targetBlockStates; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceBlockConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */