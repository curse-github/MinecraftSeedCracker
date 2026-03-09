/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
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
/*     */ public class SpawnState
/*     */ {
/*     */   private final int spawnableChunkCount;
/*     */   private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */   private final PotentialCalculator spawnPotential;
/*     */   private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */   private final LocalMobCapCalculator localMobCapCalculator;
/*     */   private BlockPos lastCheckedPos;
/*     */   private EntityType<?> lastCheckedType;
/*     */   private double lastCharge;
/*     */   
/*     */   private SpawnState(int spawnableChunkCount, Object2IntOpenHashMap<MobCategory> mobCategoryCounts, PotentialCalculator spawnPotential, LocalMobCapCalculator localMobCapCalculator) {
/*  80 */     this.spawnableChunkCount = spawnableChunkCount;
/*  81 */     this.mobCategoryCounts = mobCategoryCounts;
/*  82 */     this.spawnPotential = spawnPotential;
/*  83 */     this.localMobCapCalculator = localMobCapCalculator;
/*  84 */     this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable(mobCategoryCounts);
/*     */   }
/*     */   
/*     */   private boolean canSpawn(EntityType<?> type, BlockPos testPos, ChunkAccess chunk) {
/*  88 */     this.lastCheckedPos = testPos;
/*  89 */     this.lastCheckedType = type;
/*     */     
/*  91 */     MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(testPos, chunk).getMobSettings().getMobSpawnCost(type);
/*  92 */     if (mobSpawnCost == null) {
/*  93 */       this.lastCharge = 0.0D;
/*  94 */       return true;
/*     */     } 
/*  96 */     double charge = mobSpawnCost.charge();
/*  97 */     this.lastCharge = charge;
/*  98 */     double energyChange = this.spawnPotential.getPotentialEnergyChange(testPos, charge);
/*  99 */     return (energyChange <= mobSpawnCost.energyBudget());
/*     */   }
/*     */   private void afterSpawn(Mob mob, ChunkAccess chunk) {
/*     */     double charge;
/* 103 */     EntityType<?> type = mob.getType();
/*     */     
/* 105 */     BlockPos pos = mob.blockPosition();
/* 106 */     if (pos.equals(this.lastCheckedPos) && type == this.lastCheckedType) {
/* 107 */       charge = this.lastCharge;
/*     */     } else {
/*     */       
/* 110 */       MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(pos, chunk).getMobSettings().getMobSpawnCost(type);
/* 111 */       if (mobSpawnCost != null) {
/* 112 */         charge = mobSpawnCost.charge();
/*     */       } else {
/* 114 */         charge = 0.0D;
/*     */       } 
/*     */     } 
/* 117 */     this.spawnPotential.addCharge(pos, charge);
/* 118 */     MobCategory category = type.getCategory();
/* 119 */     this.mobCategoryCounts.addTo(category, 1);
/* 120 */     this.localMobCapCalculator.addMob(new ChunkPos(pos), category);
/*     */   }
/*     */ 
/*     */   
/* 124 */   public int getSpawnableChunkCount() { return this.spawnableChunkCount; }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public Object2IntMap<MobCategory> getMobCategoryCounts() { return this.unmodifiableMobCategoryCounts; }
/*     */ 
/*     */   
/*     */   private boolean canSpawnForCategoryGlobal(MobCategory mobCategory) {
/* 132 */     int maxMobCount = mobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 133 */     return (this.mobCategoryCounts.getInt(mobCategory) < maxMobCount);
/*     */   }
/*     */ 
/*     */   
/* 137 */   private boolean canSpawnForCategoryLocal(MobCategory mobCategory, ChunkPos chunkPos) { return (this.localMobCapCalculator.canSpawn(mobCategory, chunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\NaturalSpawner$SpawnState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */