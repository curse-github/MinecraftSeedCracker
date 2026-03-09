/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.levelgen.GenerationStep;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Builder
/*    */ {
/*    */   private final HolderSet<Biome> biomes;
/*    */   private Map<MobCategory, StructureSpawnOverride> spawnOverrides;
/*    */   private GenerationStep.Decoration step;
/*    */   private TerrainAdjustment terrainAdaption;
/*    */   
/*    */   public Builder(HolderSet<Biome> biomes) {
/* 72 */     this.spawnOverrides = Structure.StructureSettings.DEFAULT.spawnOverrides;
/* 73 */     this.step = Structure.StructureSettings.DEFAULT.step;
/* 74 */     this.terrainAdaption = Structure.StructureSettings.DEFAULT.terrainAdaptation;
/*    */ 
/*    */     
/* 77 */     this.biomes = biomes;
/*    */   }
/*    */   
/*    */   public Builder spawnOverrides(Map<MobCategory, StructureSpawnOverride> spawnOverrides) {
/* 81 */     this.spawnOverrides = spawnOverrides;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public Builder generationStep(GenerationStep.Decoration step) {
/* 86 */     this.step = step;
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public Builder terrainAdapation(TerrainAdjustment terrainAdaption) {
/* 91 */     this.terrainAdaption = terrainAdaption;
/* 92 */     return this;
/*    */   }
/*    */ 
/*    */   
/* 96 */   public Structure.StructureSettings build() { return new Structure.StructureSettings(this.biomes, this.spawnOverrides, this.step, this.terrainAdaption); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\Structure$StructureSettings$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */