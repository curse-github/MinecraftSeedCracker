/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class SimpleRandomFeatureConfiguration implements FeatureConfiguration {
/* 12 */   public static final Codec<SimpleRandomFeatureConfiguration> CODEC = ExtraCodecs.nonEmptyHolderSet(PlacedFeature.LIST_CODEC).fieldOf("features")
/* 13 */     .xmap(SimpleRandomFeatureConfiguration::new, c -> c.features).codec();
/*    */   
/*    */   public final HolderSet<PlacedFeature> features;
/*    */ 
/*    */   
/* 18 */   public SimpleRandomFeatureConfiguration(HolderSet<PlacedFeature> features) { this.features = features; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public Stream<ConfiguredFeature<?, ?>> getFeatures() { return this.features.stream().flatMap(f -> ((PlacedFeature)f.value()).getFeatures()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\feature\configurations\SimpleRandomFeatureConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */