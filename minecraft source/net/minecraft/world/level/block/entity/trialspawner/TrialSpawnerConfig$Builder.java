/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
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
/*  75 */   private int spawnRange = 4;
/*  76 */   private float totalMobs = 6.0F;
/*  77 */   private float simultaneousMobs = 2.0F;
/*  78 */   private float totalMobsAddedPerPlayer = 2.0F;
/*  79 */   private float simultaneousMobsAddedPerPlayer = 1.0F;
/*  80 */   private int ticksBetweenSpawn = 40;
/*  81 */   private WeightedList<SpawnData> spawnPotentialsDefinition = WeightedList.of();
/*  82 */   private WeightedList<ResourceKey<LootTable>> lootTablesToEject = WeightedList.builder()
/*  83 */     .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES)
/*  84 */     .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY)
/*  85 */     .build();
/*  86 */   private ResourceKey<LootTable> itemsToDropWhenOminous = BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS;
/*     */   
/*     */   public Builder spawnRange(int spawnRange) {
/*  89 */     this.spawnRange = spawnRange;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public Builder totalMobs(float totalMobs) {
/*  94 */     this.totalMobs = totalMobs;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public Builder simultaneousMobs(float simultaneousMobs) {
/*  99 */     this.simultaneousMobs = simultaneousMobs;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder totalMobsAddedPerPlayer(float totalMobsAddedPerPlayer) {
/* 104 */     this.totalMobsAddedPerPlayer = totalMobsAddedPerPlayer;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public Builder simultaneousMobsAddedPerPlayer(float simultaneousMobsAddedPerPlayer) {
/* 109 */     this.simultaneousMobsAddedPerPlayer = simultaneousMobsAddedPerPlayer;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public Builder ticksBetweenSpawn(int ticksBetweenSpawn) {
/* 114 */     this.ticksBetweenSpawn = ticksBetweenSpawn;
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public Builder spawnPotentialsDefinition(WeightedList<SpawnData> spawnPotentialsDefinition) {
/* 119 */     this.spawnPotentialsDefinition = spawnPotentialsDefinition;
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public Builder lootTablesToEject(WeightedList<ResourceKey<LootTable>> lootTablesToEject) {
/* 124 */     this.lootTablesToEject = lootTablesToEject;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public Builder itemsToDropWhenOminous(ResourceKey<LootTable> itemsToDropWhenOminous) {
/* 129 */     this.itemsToDropWhenOminous = itemsToDropWhenOminous;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/* 134 */   public TrialSpawnerConfig build() { return new TrialSpawnerConfig(this.spawnRange, this.totalMobs, this.simultaneousMobs, this.totalMobsAddedPerPlayer, this.simultaneousMobsAddedPerPlayer, this.ticksBetweenSpawn, this.spawnPotentialsDefinition, this.lootTablesToEject, this.itemsToDropWhenOminous); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfig$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */