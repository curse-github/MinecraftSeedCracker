/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ChangeOverTimeBlock<T extends Enum<T>>
/*    */ {
/*    */   public static final int SCAN_DISTANCE = 4;
/*    */   
/*    */   default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 22 */     float eachBlockOncePerDayChance = 0.05688889F;
/* 23 */     if (random.nextFloat() < 0.05688889F) {
/* 24 */       getNextState(state, level, pos, random).ifPresent(weatheredState -> level.setBlockAndUpdate(pos, weatheredState));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default Optional<BlockState> getNextState(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 35 */     int ownAge = getAge().ordinal();
/* 36 */     int sameAgeCount = 0;
/* 37 */     int olderCount = 0;
/* 38 */     for (BlockPos blockPos : BlockPos.withinManhattan(pos, 4, 4, 4)) {
/* 39 */       int manhattanDistance = blockPos.distManhattan(pos);
/* 40 */       if (manhattanDistance > 4) {
/*    */         break;
/*    */       }
/* 43 */       if (blockPos.equals(pos)) {
/*    */         continue;
/*    */       }
/*    */       
/* 47 */       Block block = level.getBlockState(blockPos).getBlock(); if (block instanceof ChangeOverTimeBlock) { ChangeOverTimeBlock<?> neighborBlock = (ChangeOverTimeBlock)block;
/*    */         
/* 49 */         Enum<?> neighborAge = neighborBlock.getAge();
/* 50 */         if (getAge().getClass() != neighborAge.getClass()) {
/*    */           continue;
/*    */         }
/* 53 */         int foundAge = neighborAge.ordinal();
/* 54 */         if (foundAge < ownAge)
/* 55 */           return Optional.empty(); 
/* 56 */         if (foundAge > ownAge) {
/* 57 */           olderCount++; continue;
/*    */         } 
/* 59 */         sameAgeCount++; }
/*    */     
/*    */     } 
/*    */ 
/*    */     
/* 64 */     float chance = (olderCount + 1) / (olderCount + sameAgeCount + 1);
/* 65 */     float actualChance = chance * chance * getChanceModifier();
/*    */     
/* 67 */     if (random.nextFloat() < actualChance) {
/* 68 */       return getNext(state);
/*    */     }
/*    */     
/* 71 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   Optional<BlockState> getNext(BlockState paramBlockState);
/*    */   
/*    */   float getChanceModifier();
/*    */   
/*    */   T getAge();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\ChangeOverTimeBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */