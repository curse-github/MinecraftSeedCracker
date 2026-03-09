/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ProbabilityFeatureConfiguration implements FeatureConfiguration {
/*  7 */   public static final Codec<ProbabilityFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(
/*  8 */         Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(()))
/*  9 */       .apply(i, ProbabilityFeatureConfiguration::new));
/*    */   
/*    */   public final float probability;
/*    */ 
/*    */   
/* 14 */   public ProbabilityFeatureConfiguration(float probability) { this.probability = probability; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\ProbabilityFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */