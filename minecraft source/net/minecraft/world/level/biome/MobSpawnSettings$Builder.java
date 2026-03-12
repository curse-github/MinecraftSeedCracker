/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobCategory;
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
/*     */ public class Builder
/*     */ {
/*  96 */   private final Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawners = Util.makeEnumMap(MobCategory.class, c -> WeightedList.builder());
/*  97 */   private final Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> mobSpawnCosts = Maps.newLinkedHashMap();
/*  98 */   private float creatureGenerationProbability = 0.1F;
/*     */   
/*     */   public Builder addSpawn(MobCategory category, int weight, MobSpawnSettings.SpawnerData spawnerData) {
/* 101 */     ((WeightedList.Builder)this.spawners.get(category)).add(spawnerData, weight);
/* 102 */     return this;
/*     */   }
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
/*     */   public Builder addMobCharge(EntityType<?> type, double charge, double energyBudget) {
/* 128 */     this.mobSpawnCosts.put(type, new MobSpawnSettings.MobSpawnCost(energyBudget, charge));
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public Builder creatureGenerationProbability(float creatureGenerationProbability) {
/* 133 */     this.creatureGenerationProbability = creatureGenerationProbability;
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public MobSpawnSettings build() {
/* 138 */     return new MobSpawnSettings(this.creatureGenerationProbability, (Map)this.spawners
/*     */         
/* 140 */         .entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, e -> ((WeightedList.Builder)e.getValue()).build())), 
/* 141 */         ImmutableMap.copyOf(this.mobSpawnCosts));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MobSpawnSettings$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */