/*     */ package net.minecraft.world.level.gameevent.vibrations;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.VibrationParticleOption;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ public interface Ticker
/*     */ {
/*     */   static void tick(Level level, VibrationSystem.Data data, VibrationSystem.User user) {
/*     */     ServerLevel serverLevel;
/* 333 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/* 337 */     if (data.currentVibration == null) {
/* 338 */       trySelectAndScheduleVibration(serverLevel, data, user);
/*     */     }
/*     */     
/* 341 */     if (data.currentVibration == null) {
/*     */       return;
/*     */     }
/*     */     
/* 345 */     boolean hasChanged = (data.getTravelTimeInTicks() > 0);
/* 346 */     tryReloadVibrationParticle(serverLevel, data, user);
/* 347 */     data.decrementTravelTime();
/*     */     
/* 349 */     if (data.getTravelTimeInTicks() <= 0) {
/* 350 */       hasChanged = receiveVibration(serverLevel, data, user, data.currentVibration);
/*     */     }
/*     */     
/* 353 */     if (hasChanged) {
/* 354 */       user.onDataChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void trySelectAndScheduleVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user) {
/* 359 */     data.getSelectionStrategy().chosenCandidate(serverLevel.getGameTime()).ifPresent(context -> {
/* 360 */           data.setCurrentVibration(context);
/* 361 */           Vec3 origin = context.pos();
/* 362 */           data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(context.distance()));
/* 363 */           serverLevel.sendParticles(new VibrationParticleOption(user.getPositionSource(), data.getTravelTimeInTicks()), origin.x, origin.y, origin.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/* 364 */           user.onDataChanged();
/* 365 */           data.getSelectionStrategy().startOver();
/*     */         });
/*     */   }
/*     */   
/*     */   private static void tryReloadVibrationParticle(ServerLevel level, VibrationSystem.Data data, VibrationSystem.User user) {
/* 370 */     if (!data.shouldReloadVibrationParticle()) {
/*     */       return;
/*     */     }
/*     */     
/* 374 */     if (data.currentVibration == null) {
/* 375 */       data.setReloadVibrationParticle(false);
/*     */       
/*     */       return;
/*     */     } 
/* 379 */     Vec3 origin = data.currentVibration.pos();
/* 380 */     PositionSource positionSource = user.getPositionSource();
/* 381 */     Vec3 destination = (Vec3)positionSource.getPosition(level).orElse(origin);
/* 382 */     int travelTimeInTicks = data.getTravelTimeInTicks();
/*     */     
/* 384 */     int initialTravelTime = user.calculateTravelTimeInTicks(data.currentVibration.distance());
/* 385 */     double alpha = 1.0D - travelTimeInTicks / initialTravelTime;
/*     */     
/* 387 */     double newInitialX = Mth.lerp(alpha, origin.x, destination.x);
/* 388 */     double newInitialY = Mth.lerp(alpha, origin.y, destination.y);
/* 389 */     double newInitialZ = Mth.lerp(alpha, origin.z, destination.z);
/*     */     
/* 391 */     boolean particleWasSent = (level.sendParticles(new VibrationParticleOption(positionSource, travelTimeInTicks), newInitialX, newInitialY, newInitialZ, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0);
/*     */     
/* 393 */     if (particleWasSent) {
/* 394 */       data.setReloadVibrationParticle(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean receiveVibration(ServerLevel serverLevel, VibrationSystem.Data data, VibrationSystem.User user, VibrationInfo currentVibration) {
/* 399 */     BlockPos origin = BlockPos.containing(currentVibration.pos());
/* 400 */     BlockPos destination = (BlockPos)user.getPositionSource().getPosition(serverLevel).map(BlockPos::containing).orElse(origin);
/*     */ 
/*     */ 
/*     */     
/* 404 */     if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking(serverLevel, destination)) {
/* 405 */       return false;
/*     */     }
/*     */     
/* 408 */     user.onReceiveVibration(serverLevel, origin, currentVibration
/*     */ 
/*     */         
/* 411 */         .gameEvent(), (Entity)currentVibration
/* 412 */         .getEntity(serverLevel).orElse(null), (Entity)currentVibration
/* 413 */         .getProjectileOwner(serverLevel).orElse(null), 
/* 414 */         VibrationSystem.Listener.distanceBetweenInBlocks(origin, destination));
/*     */ 
/*     */ 
/*     */     
/* 418 */     data.setCurrentVibration(null);
/* 419 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areAdjacentChunksTicking(Level level, BlockPos listenerPos) {
/* 423 */     ChunkPos listenerChunkPos = new ChunkPos(listenerPos);
/*     */     
/* 425 */     for (int x = listenerChunkPos.x - 1; x <= listenerChunkPos.x + 1; x++) {
/* 426 */       for (int z = listenerChunkPos.z - 1; z <= listenerChunkPos.z + 1; z++) {
/* 427 */         if (!level.shouldTickBlocksAt(ChunkPos.asLong(x, z)) || level.getChunkSource().getChunkNow(x, z) == null) {
/* 428 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 433 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSystem$Ticker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */