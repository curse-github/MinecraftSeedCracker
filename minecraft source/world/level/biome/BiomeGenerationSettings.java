/*     */ package net.minecraft.world.level.biome;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.feature.Feature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class BiomeGenerationSettings {
/*  28 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   public static final BiomeGenerationSettings EMPTY = new BiomeGenerationSettings(
/*  31 */       HolderSet.direct(new Holder[0]), 
/*  32 */       List.of());
/*     */ 
/*     */   
/*  35 */   public static final MapCodec<BiomeGenerationSettings> CODEC = RecordCodecBuilder.mapCodec(i -> {
/*  36 */         Objects.requireNonNull(LOGGER);
/*  37 */         Objects.requireNonNull(LOGGER); return i.group(ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", LOGGER::error)).fieldOf("carvers").forGetter(()), PlacedFeature.LIST_OF_LISTS_CODEC.promotePartial(Util.prefix("Features: ", LOGGER::error)).fieldOf("features").forGetter(()))
/*  38 */           .apply(i, BiomeGenerationSettings::new);
/*     */       });
/*     */   
/*     */   private final HolderSet<ConfiguredWorldCarver<?>> carvers;
/*     */   private final List<HolderSet<PlacedFeature>> features;
/*     */   private final Supplier<List<ConfiguredFeature<?, ?>>> flowerFeatures;
/*     */   private final Supplier<Set<PlacedFeature>> featureSet;
/*     */   
/*     */   private BiomeGenerationSettings(HolderSet<ConfiguredWorldCarver<?>> carvers, List<HolderSet<PlacedFeature>> features) {
/*  47 */     this.carvers = carvers;
/*  48 */     this.features = features;
/*     */ 
/*     */     
/*  51 */     this.flowerFeatures = Suppliers.memoize(() -> (List)features.stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter(()).collect(ImmutableList.toImmutableList()));
/*  52 */     this.featureSet = Suppliers.memoize(() -> (Set)features.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet()));
/*     */   }
/*     */ 
/*     */   
/*  56 */   public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers() { return this.carvers; }
/*     */ 
/*     */ 
/*     */   
/*  60 */   public List<ConfiguredFeature<?, ?>> getFlowerFeatures() { return (List)this.flowerFeatures.get(); }
/*     */ 
/*     */ 
/*     */   
/*  64 */   public List<HolderSet<PlacedFeature>> features() { return this.features; }
/*     */ 
/*     */ 
/*     */   
/*  68 */   public boolean hasFeature(PlacedFeature feature) { return ((Set)this.featureSet.get()).contains(feature); }
/*     */   
/*     */   public static class PlainBuilder
/*     */   {
/*  72 */     private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList();
/*  73 */     private final List<List<Holder<PlacedFeature>>> features = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     public PlainBuilder addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature) { return addFeature(step.ordinal(), feature); }
/*     */ 
/*     */     
/*     */     public PlainBuilder addFeature(int index, Holder<PlacedFeature> feature) {
/*  83 */       addFeatureStepsUpTo(index);
/*  84 */       ((List)this.features.get(index)).add(feature);
/*  85 */       return this;
/*     */     }
/*     */     
/*     */     public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> carver) {
/*  89 */       this.carvers.add(carver);
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     private void addFeatureStepsUpTo(int index) {
/*  94 */       while (this.features.size() <= index) {
/*  95 */         this.features.add(Lists.newArrayList());
/*     */       }
/*     */     }
/*     */     
/*     */     public BiomeGenerationSettings build() {
/* 100 */       return new BiomeGenerationSettings(
/* 101 */           HolderSet.direct(this.carvers), (List)this.features
/* 102 */           .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */     extends PlainBuilder {
/*     */     private final HolderGetter<PlacedFeature> placedFeatures;
/*     */     private final HolderGetter<ConfiguredWorldCarver<?>> worldCarvers;
/*     */     
/*     */     public Builder(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
/* 112 */       this.placedFeatures = placedFeatures;
/* 113 */       this.worldCarvers = worldCarvers;
/*     */     }
/*     */     
/*     */     public Builder addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature) {
/* 117 */       addFeature(step.ordinal(), this.placedFeatures.getOrThrow(feature));
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addCarver(ResourceKey<ConfiguredWorldCarver<?>> carver) {
/* 122 */       addCarver(this.worldCarvers.getOrThrow(carver));
/* 123 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */