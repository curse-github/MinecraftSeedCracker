/*    */ package net.minecraft.data.worldgen.biome;
/*    */ 
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.data.worldgen.BiomeDefaultFeatures;
/*    */ import net.minecraft.data.worldgen.placement.EndPlacements;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*    */ import net.minecraft.world.level.biome.BiomeSpecialEffects;
/*    */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public class EndBiomes {
/*    */   private static Biome baseEndBiome(BiomeGenerationSettings.Builder generation) {
/* 16 */     MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
/* 17 */     BiomeDefaultFeatures.endSpawns(mobs);
/*    */     
/* 19 */     return (new Biome.BiomeBuilder())
/* 20 */       .hasPrecipitation(false)
/* 21 */       .temperature(0.5F)
/* 22 */       .downfall(0.5F)
/* 23 */       .specialEffects((new BiomeSpecialEffects.Builder())
/* 24 */         .waterColor(4159204)
/* 25 */         .build())
/*    */       
/* 27 */       .mobSpawnSettings(mobs.build())
/* 28 */       .generationSettings(generation.build())
/* 29 */       .build();
/*    */   }
/*    */   
/*    */   public static Biome endBarrens(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 33 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/* 34 */     return baseEndBiome(generation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Biome theEnd(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 40 */     BiomeGenerationSettings.Builder generation = (new BiomeGenerationSettings.Builder(placedFeatures, carvers)).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_SPIKE).addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, EndPlacements.END_PLATFORM);
/* 41 */     return baseEndBiome(generation);
/*    */   }
/*    */   
/*    */   public static Biome endMidlands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 45 */     BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
/* 46 */     return baseEndBiome(generation);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Biome endHighlands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 52 */     BiomeGenerationSettings.Builder generation = (new BiomeGenerationSettings.Builder(placedFeatures, carvers)).addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EndPlacements.CHORUS_PLANT);
/* 53 */     return baseEndBiome(generation);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Biome smallEndIslands(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
/* 58 */     BiomeGenerationSettings.Builder generation = (new BiomeGenerationSettings.Builder(placedFeatures, carvers)).addFeature(GenerationStep.Decoration.RAW_GENERATION, EndPlacements.END_ISLAND_DECORATED);
/* 59 */     return baseEndBiome(generation);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\worldgen\biome\EndBiomes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */