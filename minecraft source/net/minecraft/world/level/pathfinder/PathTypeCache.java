/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.HashCommon;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PathTypeCache
/*    */ {
/*    */   private static final int SIZE = 4096;
/*    */   private static final int MASK = 4095;
/* 13 */   private final long[] positions = new long[4096];
/* 14 */   private final PathType[] pathTypes = new PathType[4096];
/*    */   
/*    */   public PathType getOrCompute(BlockGetter level, BlockPos pos) {
/* 17 */     long key = pos.asLong();
/* 18 */     int index = index(key);
/* 19 */     PathType cachedPathType = get(index, key);
/* 20 */     if (cachedPathType != null) {
/* 21 */       return cachedPathType;
/*    */     }
/* 23 */     return compute(level, pos, index, key);
/*    */   }
/*    */   
/*    */   private PathType get(int index, long key) {
/* 27 */     if (this.positions[index] == key) {
/* 28 */       return this.pathTypes[index];
/*    */     }
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   private PathType compute(BlockGetter level, BlockPos pos, int index, long key) {
/* 34 */     PathType pathType = WalkNodeEvaluator.getPathTypeFromState(level, pos);
/* 35 */     this.positions[index] = key;
/* 36 */     this.pathTypes[index] = pathType;
/* 37 */     return pathType;
/*    */   }
/*    */   
/*    */   public void invalidate(BlockPos pos) {
/* 41 */     long key = pos.asLong();
/* 42 */     int index = index(key);
/* 43 */     if (this.positions[index] == key) {
/* 44 */       this.pathTypes[index] = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 49 */   private static int index(long pos) { return (int)HashCommon.mix(pos) & 0xFFF; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\PathTypeCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */