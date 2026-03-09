/*     */ package net.minecraft.world.entity.npc.wanderingtrader;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.SpawnPlacementType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.animal.equine.TraderLlama;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.storage.ServerLevelData;
/*     */ 
/*     */ 
/*     */ public class WanderingTraderSpawner
/*     */   implements CustomSpawner
/*     */ {
/*     */   private static final int DEFAULT_TICK_DELAY = 1200;
/*     */   public static final int DEFAULT_SPAWN_DELAY = 24000;
/*     */   private static final int MIN_SPAWN_CHANCE = 25;
/*     */   private static final int MAX_SPAWN_CHANCE = 75;
/*     */   private static final int SPAWN_CHANCE_INCREASE = 25;
/*     */   private static final int SPAWN_ONE_IN_X_CHANCE = 10;
/*     */   
/*     */   public WanderingTraderSpawner(ServerLevelData serverLevelData) {
/*  37 */     this.random = RandomSource.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     this.serverLevelData = serverLevelData;
/*  45 */     this.tickDelay = 1200;
/*  46 */     this.spawnDelay = serverLevelData.getWanderingTraderSpawnDelay();
/*  47 */     this.spawnChance = serverLevelData.getWanderingTraderSpawnChance();
/*     */     
/*  49 */     if (this.spawnDelay == 0 && this.spawnChance == 0) {
/*  50 */       this.spawnDelay = 24000;
/*  51 */       serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  52 */       this.spawnChance = 25;
/*  53 */       serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     } 
/*     */   }
/*     */   private static final int NUMBER_OF_SPAWN_ATTEMPTS = 10; private final RandomSource random; private final ServerLevelData serverLevelData; private int tickDelay; private int spawnDelay; private int spawnChance;
/*     */   
/*     */   public void tick(ServerLevel level, boolean spawnEnemies) {
/*  59 */     if (!((Boolean)level.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     if (--this.tickDelay > 0) {
/*     */       return;
/*     */     }
/*  66 */     this.tickDelay = 1200;
/*     */     
/*  68 */     this.spawnDelay -= 1200;
/*  69 */     this.serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
/*  70 */     if (this.spawnDelay > 0) {
/*     */       return;
/*     */     }
/*  73 */     this.spawnDelay = 24000;
/*     */     
/*  75 */     int chanceToSpawn = this.spawnChance;
/*  76 */     this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
/*  77 */     this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
/*     */     
/*  79 */     if (this.random.nextInt(100) > chanceToSpawn) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     if (spawn(level)) {
/*  84 */       this.spawnChance = 25;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean spawn(ServerLevel level) {
/*  89 */     ServerPlayer serverPlayer = level.getRandomPlayer();
/*  90 */     if (serverPlayer == null) {
/*  91 */       return true;
/*     */     }
/*     */     
/*  94 */     if (this.random.nextInt(10) != 0) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     BlockPos playerPos = serverPlayer.blockPosition();
/*  99 */     int radius = 48;
/*     */     
/* 101 */     PoiManager poiManager = level.getPoiManager();
/* 102 */     Optional<BlockPos> poiPos = poiManager.find(p -> p.is(PoiTypes.MEETING), p -> true, playerPos, 48, PoiManager.Occupancy.ANY);
/*     */     
/* 104 */     BlockPos referencePos = (BlockPos)poiPos.orElse(playerPos);
/* 105 */     BlockPos spawnPosition = findSpawnPositionNear(level, referencePos, 48);
/*     */     
/* 107 */     if (spawnPosition != null && hasEnoughSpace(level, spawnPosition)) {
/* 108 */       if (level.getBiome(spawnPosition).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
/* 109 */         return false;
/*     */       }
/*     */       
/* 112 */       WanderingTrader trader = (WanderingTrader)EntityType.WANDERING_TRADER.spawn(level, spawnPosition, EntitySpawnReason.EVENT);
/*     */       
/* 114 */       if (trader != null) {
/* 115 */         for (int i = 0; i < 2; i++) {
/* 116 */           tryToSpawnLlamaFor(level, trader, 4);
/*     */         }
/* 118 */         this.serverLevelData.setWanderingTraderId(trader.getUUID());
/* 119 */         trader.setDespawnDelay(48000);
/*     */         
/* 121 */         trader.setWanderTarget(referencePos);
/* 122 */         trader.setHomeTo(referencePos, 16);
/* 123 */         return true;
/*     */       } 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   private void tryToSpawnLlamaFor(ServerLevel level, WanderingTrader trader, int radius) {
/* 130 */     BlockPos spawnPosition = findSpawnPositionNear(level, trader.blockPosition(), radius);
/* 131 */     if (spawnPosition == null) {
/*     */       return;
/*     */     }
/*     */     
/* 135 */     TraderLlama llama = (TraderLlama)EntityType.TRADER_LLAMA.spawn(level, spawnPosition, EntitySpawnReason.EVENT);
/* 136 */     if (llama == null) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     llama.setLeashedTo(trader, true);
/*     */   }
/*     */   
/*     */   private BlockPos findSpawnPositionNear(LevelReader level, BlockPos referencePosition, int radius) {
/* 144 */     BlockPos spawnPosition = null;
/*     */     
/* 146 */     SpawnPlacementType wanderingTraderSpawnType = SpawnPlacements.getPlacementType(EntityType.WANDERING_TRADER);
/* 147 */     for (int i = 0; i < 10; i++) {
/* 148 */       int xPosition = referencePosition.getX() + this.random.nextInt(radius * 2) - radius;
/* 149 */       int zPosition = referencePosition.getZ() + this.random.nextInt(radius * 2) - radius;
/* 150 */       int yPosition = level.getHeight(Heightmap.Types.WORLD_SURFACE, xPosition, zPosition);
/* 151 */       BlockPos spawnPos = new BlockPos(xPosition, yPosition, zPosition);
/*     */       
/* 153 */       if (wanderingTraderSpawnType.isSpawnPositionOk(level, spawnPos, EntityType.WANDERING_TRADER)) {
/* 154 */         spawnPosition = spawnPos;
/*     */         break;
/*     */       } 
/*     */     } 
/* 158 */     return spawnPosition;
/*     */   }
/*     */   
/*     */   private boolean hasEnoughSpace(BlockGetter level, BlockPos spawnPos) {
/* 162 */     for (BlockPos pos : BlockPos.betweenClosed(spawnPos, spawnPos.offset(1, 2, 1))) {
/* 163 */       if (!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) {
/* 164 */         return false;
/*     */       }
/*     */     } 
/* 167 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\wanderingtrader\WanderingTraderSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */