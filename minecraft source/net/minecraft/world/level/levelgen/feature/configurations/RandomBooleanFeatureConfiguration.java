/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<RandomBooleanFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(PlacedFeature.CODEC
/* 13 */         .fieldOf("feature_true").forGetter(()), PlacedFeature.CODEC
/* 14 */         .fieldOf("feature_false").forGetter(()))
/* 15 */       .apply(i, RandomBooleanFeatureConfiguration::new));
/*    */   
/*    */   public final Holder<PlacedFeature> featureTrue;
/*    */   public final Holder<PlacedFeature> featureFalse;
/*    */   
/*    */   public RandomBooleanFeatureConfiguration(Holder<PlacedFeature> featureTrue, Holder<PlacedFeature> featureFalse) {
/* 21 */     this.featureTrue = featureTrue;
/* 22 */     this.featureFalse = featureFalse;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public Stream<ConfiguredFeature<?, ?>> getFeatures() { return Stream.concat(((PlacedFeature)this.featureTrue.value()).getFeatures(), ((PlacedFeature)this.featureFalse.value()).getFeatures()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomBooleanFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */