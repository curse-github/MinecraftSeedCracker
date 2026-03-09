/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class RandomFeatureConfiguration implements FeatureConfiguration {
/* 15 */   public static final Codec<RandomFeatureConfiguration> CODEC = RecordCodecBuilder.create(i -> i.apply2(RandomFeatureConfiguration::new, WeightedPlacedFeature.CODEC
/*    */         
/* 17 */         .listOf().fieldOf("features").forGetter(()), PlacedFeature.CODEC
/* 18 */         .fieldOf("default").forGetter(())));
/*    */   
/*    */   public final List<WeightedPlacedFeature> features;
/*    */   
/*    */   public final Holder<PlacedFeature> defaultFeature;
/*    */   
/*    */   public RandomFeatureConfiguration(List<WeightedPlacedFeature> features, Holder<PlacedFeature> defaultFeature) {
/* 25 */     this.features = features;
/* 26 */     this.defaultFeature = defaultFeature;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public Stream<ConfiguredFeature<?, ?>> getFeatures() { return Stream.concat(this.features.stream().flatMap(weighted -> ((PlacedFeature)weighted.feature.value()).getFeatures()), ((PlacedFeature)this.defaultFeature.value()).getFeatures()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */