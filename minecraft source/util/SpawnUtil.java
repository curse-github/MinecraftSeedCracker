/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpawnUtil
/*     */ {
/*     */   public static <T extends Mob> Optional<T> trySpawnMob(EntityType<T> entityType, EntitySpawnReason spawnReason, ServerLevel level, BlockPos start, int spawnAttempts, int spawnRangeXZ, int spawnRangeY, Strategy strategy, boolean checkCollisions) {
/*  23 */     BlockPos.MutableBlockPos searchPos = start.mutable();
/*  24 */     for (int i = 0; i < spawnAttempts; i++) {
/*  25 */       int dx = Mth.randomBetweenInclusive(level.random, -spawnRangeXZ, spawnRangeXZ);
/*  26 */       int dz = Mth.randomBetweenInclusive(level.random, -spawnRangeXZ, spawnRangeXZ);
/*     */       
/*  28 */       searchPos.setWithOffset(start, dx, spawnRangeY, dz);
/*  29 */       if (level.getWorldBorder().isWithinBounds(searchPos) && moveToPossibleSpawnPosition(level, spawnRangeY, searchPos, strategy))
/*     */       {
/*     */         
/*  32 */         if (!checkCollisions || level.noCollision(entityType.getSpawnAABB(searchPos.getX() + 0.5D, searchPos.getY(), searchPos.getZ() + 0.5D))) {
/*     */ 
/*     */ 
/*     */           
/*  36 */           T mob = (T)(Mob)entityType.create(level, null, searchPos, spawnReason, false, false);
/*  37 */           if (mob != null) {
/*  38 */             if (mob.checkSpawnRules(level, spawnReason) && mob.checkSpawnObstruction(level)) {
/*  39 */               level.addFreshEntityWithPassengers(mob);
/*  40 */               mob.playAmbientSound();
/*  41 */               return Optional.of(mob);
/*     */             } 
/*  43 */             mob.discard();
/*     */           } 
/*     */         }  } 
/*     */     } 
/*  47 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Strategy
/*     */   {
/*     */     @Deprecated
/*     */     public static final Strategy LEGACY_IRON_GOLEM = (level, pos, blockState, abovePos, aboveState) -> {
/*  60 */         if (blockState.is(Blocks.COBWEB) || blockState
/*  61 */           .is(Blocks.CACTUS) || blockState
/*  62 */           .is(Blocks.GLASS_PANE) || blockState
/*  63 */           .getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || blockState
/*  64 */           .getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || blockState
/*  65 */           .getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || blockState
/*  66 */           .is(Blocks.CONDUIT) || blockState
/*  67 */           .is(Blocks.ICE) || blockState
/*  68 */           .is(Blocks.TNT) || blockState
/*  69 */           .is(Blocks.GLOWSTONE) || blockState
/*  70 */           .is(Blocks.BEACON) || blockState
/*  71 */           .is(Blocks.SEA_LANTERN) || blockState
/*  72 */           .is(Blocks.FROSTED_ICE) || blockState
/*  73 */           .is(Blocks.TINTED_GLASS) || blockState
/*  74 */           .is(Blocks.GLASS))
/*     */         {
/*  76 */           return false;
/*     */         }
/*  78 */         return ((aboveState.isAir() || aboveState.liquid()) && (blockState.isSolid() || blockState.is(Blocks.POWDER_SNOW)));
/*     */       };
/*     */ 
/*     */     
/*  82 */     public static final Strategy ON_TOP_OF_COLLIDER = (level, pos, blockState, abovePos, aboveState) -> (aboveState.getCollisionShape(level, abovePos).isEmpty() && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP));
/*     */     boolean canSpawnOn(ServerLevel param1ServerLevel, BlockPos param1BlockPos1, BlockState param1BlockState1, BlockPos param1BlockPos2, BlockState param1BlockState2);
/*     */     
/*  85 */     public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (level, pos, blockState, abovePos, aboveState) -> (aboveState.getCollisionShape(level, abovePos).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(level, pos), Direction.UP));
/*     */   }
/*     */   
/*     */   private static boolean moveToPossibleSpawnPosition(ServerLevel level, int spawnRangeY, BlockPos.MutableBlockPos searchPos, Strategy strategy) {
/*  89 */     BlockPos.MutableBlockPos abovePos = (new BlockPos.MutableBlockPos()).set(searchPos);
/*  90 */     BlockState aboveState = level.getBlockState(abovePos);
/*     */     
/*  92 */     for (int y = spawnRangeY; y >= -spawnRangeY; y--) {
/*  93 */       searchPos.move(Direction.DOWN);
/*  94 */       abovePos.setWithOffset(searchPos, Direction.UP);
/*     */       
/*  96 */       BlockState currentState = level.getBlockState(searchPos);
/*  97 */       if (strategy.canSpawnOn(level, searchPos, currentState, abovePos, aboveState)) {
/*  98 */         searchPos.move(Direction.UP);
/*  99 */         return true;
/*     */       } 
/* 101 */       aboveState = currentState;
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\SpawnUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */