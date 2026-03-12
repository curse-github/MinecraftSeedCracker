/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class PatrolSpawner
/*     */   implements CustomSpawner
/*     */ {
/*     */   private int nextTick;
/*     */   
/*     */   public void tick(ServerLevel level, boolean spawnEnemies) {
/*  22 */     if (!spawnEnemies) {
/*     */       return;
/*     */     }
/*     */     
/*  26 */     if (!((Boolean)level.getGameRules().get(GameRules.SPAWN_PATROLS)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  30 */     RandomSource random = level.random;
/*     */     
/*  32 */     this.nextTick--;
/*  33 */     if (this.nextTick > 0) {
/*     */       return;
/*     */     }
/*     */     
/*  37 */     this.nextTick += 12000 + random.nextInt(1200);
/*     */     
/*  39 */     if (!level.isBrightOutside()) {
/*     */       return;
/*     */     }
/*     */     
/*  43 */     if (random.nextInt(5) != 0) {
/*     */       return;
/*     */     }
/*     */     
/*  47 */     int playerCount = level.players().size();
/*  48 */     if (playerCount < 1) {
/*     */       return;
/*     */     }
/*     */     
/*  52 */     Player player = (Player)level.players().get(random.nextInt(playerCount));
/*  53 */     if (player.isSpectator()) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     if (level.isCloseToVillage(player.blockPosition(), 2)) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     int x = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
/*  62 */     int z = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
/*  63 */     BlockPos.MutableBlockPos spawnPos = player.blockPosition().mutable().move(x, 0, z);
/*     */ 
/*     */     
/*  66 */     int delta = 10;
/*  67 */     if (!level.hasChunksAt(spawnPos.getX() - 10, spawnPos.getZ() - 10, spawnPos.getX() + 10, spawnPos.getZ() + 10)) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     if (!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, spawnPos)).booleanValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     int groupSize = (int)Math.ceil(level.getCurrentDifficultyAt(spawnPos).getEffectiveDifficulty()) + 1;
/*  76 */     for (int i = 0; i < groupSize; i++) {
/*  77 */       spawnPos.setY(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos).getY());
/*     */       
/*  79 */       if (i == 0) {
/*  80 */         if (!spawnPatrolMember(level, spawnPos, random, true)) {
/*     */           break;
/*     */         }
/*     */       } else {
/*  84 */         spawnPatrolMember(level, spawnPos, random, false);
/*     */       } 
/*     */       
/*  87 */       spawnPos.setX(spawnPos.getX() + random.nextInt(5) - random.nextInt(5));
/*  88 */       spawnPos.setZ(spawnPos.getZ() + random.nextInt(5) - random.nextInt(5));
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean spawnPatrolMember(ServerLevel level, BlockPos pos, RandomSource random, boolean isLeader) {
/*  93 */     BlockState state = level.getBlockState(pos);
/*  94 */     if (!NaturalSpawner.isValidEmptySpawnBlock(level, pos, state, state.getFluidState(), EntityType.PILLAGER)) {
/*  95 */       return false;
/*     */     }
/*     */     
/*  98 */     if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, level, EntitySpawnReason.PATROL, pos, random)) {
/*  99 */       return false;
/*     */     }
/*     */     
/* 102 */     PatrollingMonster mob = (PatrollingMonster)EntityType.PILLAGER.create(level, EntitySpawnReason.PATROL);
/* 103 */     if (mob != null) {
/* 104 */       if (isLeader) {
/* 105 */         mob.setPatrolLeader(true);
/* 106 */         mob.findPatrolTarget();
/*     */       } 
/*     */       
/* 109 */       mob.setPos(pos.getX(), pos.getY(), pos.getZ());
/* 110 */       mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.PATROL, null);
/*     */       
/* 112 */       level.addFreshEntityWithPassengers(mob);
/* 113 */       return true;
/*     */     } 
/*     */     
/* 116 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\PatrolSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */