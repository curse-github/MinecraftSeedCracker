/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceSphereConfiguration implements FeatureConfiguration {
/*  9 */   public static final Codec<ReplaceSphereConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockState.CODEC
/* 10 */         .fieldOf("target").forGetter(()), BlockState.CODEC
/* 11 */         .fieldOf("state").forGetter(()), 
/* 12 */         IntProvider.codec(0, 12).fieldOf("radius").forGetter(()))
/* 13 */       .apply(i, ReplaceSphereConfiguration::new));
/*    */   
/*    */   public final BlockState targetState;
/*    */   
/*    */   public final BlockState replaceState;
/*    */   private final IntProvider radius;
/*    */   
/*    */   public ReplaceSphereConfiguration(BlockState targetState, BlockState replaceState, IntProvider radius) {
/* 21 */     this.targetState = targetState;
/* 22 */     this.replaceState = replaceState;
/* 23 */     this.radius = radius;
/*    */   }
/*    */ 
/*    */   
/* 27 */   public IntProvider radius() { return this.radius; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ReplaceSphereConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */