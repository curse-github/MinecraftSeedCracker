/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlainBuilder
/*     */ {
/*  72 */   private final List<Holder<ConfiguredWorldCarver<?>>> carvers = new ArrayList();
/*  73 */   private final List<List<Holder<PlacedFeature>>> features = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   public PlainBuilder addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> feature) { return addFeature(step.ordinal(), feature); }
/*     */ 
/*     */   
/*     */   public PlainBuilder addFeature(int index, Holder<PlacedFeature> feature) {
/*  83 */     addFeatureStepsUpTo(index);
/*  84 */     ((List)this.features.get(index)).add(feature);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public PlainBuilder addCarver(Holder<ConfiguredWorldCarver<?>> carver) {
/*  89 */     this.carvers.add(carver);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   private void addFeatureStepsUpTo(int index) {
/*  94 */     while (this.features.size() <= index) {
/*  95 */       this.features.add(Lists.newArrayList());
/*     */     }
/*     */   }
/*     */   
/*     */   public BiomeGenerationSettings build() {
/* 100 */     return new BiomeGenerationSettings(
/* 101 */         HolderSet.direct(this.carvers), (List)this.features
/* 102 */         .stream().map(HolderSet::direct).collect(ImmutableList.toImmutableList()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\BiomeGenerationSettings$PlainBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */