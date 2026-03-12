/*     */ package net.minecraft.world.entity.ai.village;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.CustomSpawner;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VillageSiege
/*     */   implements CustomSpawner
/*     */ {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private boolean hasSetupSiege;
/*  24 */   private State siegeState = State.SIEGE_DONE;
/*     */   private int zombiesToSpawn;
/*     */   private int nextSpawnTime;
/*     */   private int spawnX;
/*     */   private int spawnY;
/*     */   private int spawnZ;
/*     */   
/*     */   private enum State {
/*  32 */     SIEGE_CAN_ACTIVATE,
/*  33 */     SIEGE_TONIGHT,
/*  34 */     SIEGE_DONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick(ServerLevel level, boolean spawnEnemies) {
/*  40 */     if (level.isBrightOutside() || !spawnEnemies) {
/*  41 */       this.siegeState = State.SIEGE_DONE;
/*  42 */       this.hasSetupSiege = false;
/*     */       
/*     */       return;
/*     */     } 
/*  46 */     long dayTime = level.getDayTime() % 24000L;
/*  47 */     if (dayTime == 18000L) {
/*  48 */       this.siegeState = (level.random.nextInt(10) == 0) ? State.SIEGE_TONIGHT : State.SIEGE_DONE;
/*     */     }
/*     */     
/*  51 */     if (this.siegeState == State.SIEGE_DONE) {
/*     */       return;
/*     */     }
/*     */     
/*  55 */     if (!this.hasSetupSiege) {
/*  56 */       if (tryToSetupSiege(level)) {
/*  57 */         this.hasSetupSiege = true;
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (this.nextSpawnTime > 0) {
/*  65 */       this.nextSpawnTime--;
/*     */       
/*     */       return;
/*     */     } 
/*  69 */     this.nextSpawnTime = 2;
/*  70 */     if (this.zombiesToSpawn > 0) {
/*  71 */       trySpawn(level);
/*  72 */       this.zombiesToSpawn--;
/*     */     } else {
/*  74 */       this.siegeState = State.SIEGE_DONE;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryToSetupSiege(ServerLevel level) {
/*  79 */     for (Player player : level.players()) {
/*  80 */       if (!player.isSpectator()) {
/*  81 */         BlockPos center = player.blockPosition();
/*  82 */         if (!level.isVillage(center) || level.getBiome(center).is(BiomeTags.WITHOUT_ZOMBIE_SIEGES)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  87 */         for (int i = 0; i < 10; i++) {
/*  88 */           float angle = level.random.nextFloat() * 6.2831855F;
/*  89 */           this.spawnX = center.getX() + Mth.floor(Mth.cos(angle) * 32.0F);
/*  90 */           this.spawnY = center.getY();
/*  91 */           this.spawnZ = center.getZ() + Mth.floor(Mth.sin(angle) * 32.0F);
/*     */           
/*  93 */           if (findRandomSpawnPos(level, new BlockPos(this.spawnX, this.spawnY, this.spawnZ)) != null) {
/*  94 */             this.nextSpawnTime = 0;
/*  95 */             this.zombiesToSpawn = 20;
/*     */             break;
/*     */           } 
/*     */         } 
/*  99 */         return true;
/*     */       } 
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */   private void trySpawn(ServerLevel level) {
/*     */     Zombie zombie;
/* 106 */     Vec3 spawnPos = findRandomSpawnPos(level, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
/* 107 */     if (spawnPos == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 112 */       zombie = new Zombie(level);
/* 113 */       zombie.finalizeSpawn(level, level.getCurrentDifficultyAt(zombie.blockPosition()), EntitySpawnReason.EVENT, null);
/* 114 */     } catch (Exception e) {
/* 115 */       LOGGER.warn("Failed to create zombie for village siege at {}", spawnPos, e);
/*     */       return;
/*     */     } 
/* 118 */     zombie.snapTo(spawnPos.x, spawnPos.y, spawnPos.z, level.random.nextFloat() * 360.0F, 0.0F);
/* 119 */     level.addFreshEntityWithPassengers(zombie);
/*     */   }
/*     */ 
/*     */   
/*     */   private Vec3 findRandomSpawnPos(ServerLevel level, BlockPos pos) {
/* 124 */     for (int i = 0; i < 10; i++) {
/* 125 */       int x = pos.getX() + level.random.nextInt(16) - 8;
/* 126 */       int z = pos.getZ() + level.random.nextInt(16) - 8;
/* 127 */       int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
/* 128 */       BlockPos offset = new BlockPos(x, y, z);
/*     */       
/* 130 */       if (level.isVillage(offset))
/*     */       {
/*     */         
/* 133 */         if (Monster.checkMonsterSpawnRules(EntityType.ZOMBIE, level, EntitySpawnReason.EVENT, offset, level.random))
/* 134 */           return Vec3.atBottomCenterOf(offset); 
/*     */       }
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\village\VillageSiege.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */