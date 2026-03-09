/*    */ package net.minecraft.world.entity.npc;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.tags.StructureTags;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.SpawnPlacements;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CatSpawner
/*    */   implements CustomSpawner
/*    */ {
/*    */   private static final int TICK_DELAY = 1200;
/*    */   private int nextTick;
/*    */   
/*    */   public void tick(ServerLevel level, boolean spawnEnemies) {
/* 28 */     this.nextTick--;
/* 29 */     if (this.nextTick > 0) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     this.nextTick = 1200;
/*    */     
/* 35 */     ServerPlayer serverPlayer = level.getRandomPlayer();
/* 36 */     if (serverPlayer == null) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     RandomSource random = level.random;
/* 41 */     int x = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
/* 42 */     int z = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
/* 43 */     BlockPos spawnPos = serverPlayer.blockPosition().offset(x, 0, z);
/*    */ 
/*    */     
/* 46 */     int delta = 10;
/* 47 */     if (!level.hasChunksAt(spawnPos.getX() - 10, spawnPos.getZ() - 10, spawnPos.getX() + 10, spawnPos.getZ() + 10)) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     if (SpawnPlacements.isSpawnPositionOk(EntityType.CAT, level, spawnPos)) {
/* 52 */       if (level.isCloseToVillage(spawnPos, 2)) {
/* 53 */         spawnInVillage(level, spawnPos);
/* 54 */       } else if (level.structureManager().getStructureWithPieceAt(spawnPos, StructureTags.CATS_SPAWN_IN).isValid()) {
/* 55 */         spawnInHut(level, spawnPos);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnInVillage(ServerLevel serverLevel, BlockPos spawnPos) {
/* 61 */     int radius = 48;
/* 62 */     if (serverLevel.getPoiManager().getCountInRange(p -> p.is(PoiTypes.HOME), spawnPos, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
/* 63 */       List<Cat> cats = serverLevel.getEntitiesOfClass(Cat.class, (new AABB(spawnPos)).inflate(48.0D, 8.0D, 48.0D));
/* 64 */       if (cats.size() < 5) {
/* 65 */         spawnCat(spawnPos, serverLevel, false);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private void spawnInHut(ServerLevel level, BlockPos spawnPos) {
/* 71 */     int radius = 16;
/* 72 */     List<Cat> cats = level.getEntitiesOfClass(Cat.class, (new AABB(spawnPos)).inflate(16.0D, 8.0D, 16.0D));
/* 73 */     if (cats.isEmpty()) {
/* 74 */       spawnCat(spawnPos, level, true);
/*    */     }
/*    */   }
/*    */   
/*    */   private void spawnCat(BlockPos spawnPos, ServerLevel level, boolean makePersistent) {
/* 79 */     Cat cat = (Cat)EntityType.CAT.create(level, EntitySpawnReason.NATURAL);
/* 80 */     if (cat == null) {
/*    */       return;
/*    */     }
/*    */     
/* 84 */     cat.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), EntitySpawnReason.NATURAL, null);
/* 85 */     if (makePersistent) {
/* 86 */       cat.setPersistenceRequired();
/*    */     }
/* 88 */     cat.snapTo(spawnPos, 0.0F, 0.0F);
/* 89 */     level.addFreshEntityWithPassengers(cat);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\npc\CatSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */