/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ static enum null
/*     */ {
/*     */   private final ThreadLocal<List<ObjectSet<BlockPos>>> queue;
/*     */   
/* 325 */   null(boolean chunky, Block... blocks) { this.queue = ThreadLocal.withInitial(() -> Lists.newArrayListWithCapacity(7)); }
/*     */ 
/*     */   
/*     */   public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
/* 329 */     BlockState newState = state.updateShape(level, level, pos, direction, neighbourPos, level.getBlockState(neighbourPos), level.getRandom());
/* 330 */     if (state != newState) {
/* 331 */       int distance = ((Integer)newState.getValue(BlockStateProperties.DISTANCE)).intValue();
/* 332 */       List<ObjectSet<BlockPos>> queue = (List)this.queue.get();
/* 333 */       if (queue.isEmpty()) {
/* 334 */         for (int i = 0; i < 7; i++) {
/* 335 */           queue.add(new ObjectOpenHashSet());
/*     */         }
/*     */       }
/* 338 */       ((ObjectSet)queue.get(distance)).add(pos.immutable());
/*     */     } 
/* 340 */     return state;
/*     */   }
/*     */ 
/*     */   
/*     */   public void processChunk(LevelAccessor level) {
/* 345 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/*     */     
/* 347 */     List<ObjectSet<BlockPos>> queue = (List)this.queue.get();
/* 348 */     for (int neighborDistance = 2; neighborDistance < queue.size(); neighborDistance++) {
/* 349 */       int currentDistance = neighborDistance - 1;
/* 350 */       ObjectSet<BlockPos> set = (ObjectSet)queue.get(currentDistance);
/* 351 */       ObjectSet<BlockPos> newSet = (ObjectSet)queue.get(neighborDistance);
/*     */       
/* 353 */       for (ObjectIterator objectIterator = set.iterator(); objectIterator.hasNext(); ) { BlockPos pos = (BlockPos)objectIterator.next();
/* 354 */         BlockState state = level.getBlockState(pos);
/* 355 */         if (((Integer)state.getValue(BlockStateProperties.DISTANCE)).intValue() < currentDistance) {
/*     */           continue;
/*     */         }
/*     */         
/* 359 */         level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(currentDistance)), 18);
/*     */         
/* 361 */         if (neighborDistance != 7) {
/* 362 */           for (Direction direction : DIRECTIONS) {
/* 363 */             neighborPos.setWithOffset(pos, direction);
/* 364 */             BlockState neighbor = level.getBlockState(neighborPos);
/*     */             
/* 366 */             if (neighbor.hasProperty(BlockStateProperties.DISTANCE) && ((Integer)state.getValue(BlockStateProperties.DISTANCE)).intValue() > neighborDistance) {
/* 367 */               newSet.add(neighborPos.immutable());
/*     */             }
/*     */           } 
/*     */         } }
/*     */     
/*     */     } 
/*     */     
/* 374 */     queue.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixers$4.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */