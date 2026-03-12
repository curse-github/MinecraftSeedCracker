/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueueEntry
/*     */ {
/*     */   private static final int FROM_LEVEL_BITS = 4;
/*     */   private static final int DIRECTION_BITS = 6;
/*     */   private static final long LEVEL_MASK = 15L;
/*     */   private static final long DIRECTIONS_MASK = 1008L;
/*     */   private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
/*     */   private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;
/*     */   
/*     */   public static long decreaseSkipOneDirection(int oldFromLevel, Direction skipDirection) {
/* 258 */     long decreaseData = withoutDirection(1008L, skipDirection);
/* 259 */     return withLevel(decreaseData, oldFromLevel);
/*     */   }
/*     */ 
/*     */   
/* 263 */   public static long decreaseAllDirections(int oldFromLevel) { return withLevel(1008L, oldFromLevel); }
/*     */ 
/*     */   
/*     */   public static long increaseLightFromEmission(int newFromLevel, boolean fromEmptyShape) {
/* 267 */     long increaseData = 1008L;
/* 268 */     increaseData |= 0x800L;
/* 269 */     if (fromEmptyShape) {
/* 270 */       increaseData |= 0x400L;
/*     */     }
/* 272 */     return withLevel(increaseData, newFromLevel);
/*     */   }
/*     */   
/*     */   public static long increaseSkipOneDirection(int newFromLevel, boolean fromEmptyShape, Direction skipDirection) {
/* 276 */     long increaseData = withoutDirection(1008L, skipDirection);
/* 277 */     if (fromEmptyShape) {
/* 278 */       increaseData |= 0x400L;
/*     */     }
/* 280 */     return withLevel(increaseData, newFromLevel);
/*     */   }
/*     */   
/*     */   public static long increaseOnlyOneDirection(int newFromLevel, boolean fromEmptyShape, Direction direction) {
/* 284 */     long increaseData = 0L;
/* 285 */     if (fromEmptyShape) {
/* 286 */       increaseData |= 0x400L;
/*     */     }
/* 288 */     increaseData = withDirection(increaseData, direction);
/* 289 */     return withLevel(increaseData, newFromLevel);
/*     */   }
/*     */   
/*     */   public static long increaseSkySourceInDirections(boolean down, boolean north, boolean south, boolean west, boolean east) {
/* 293 */     long increaseData = withLevel(0L, 15);
/* 294 */     if (down) {
/* 295 */       increaseData = withDirection(increaseData, Direction.DOWN);
/*     */     }
/* 297 */     if (north) {
/* 298 */       increaseData = withDirection(increaseData, Direction.NORTH);
/*     */     }
/* 300 */     if (south) {
/* 301 */       increaseData = withDirection(increaseData, Direction.SOUTH);
/*     */     }
/* 303 */     if (west) {
/* 304 */       increaseData = withDirection(increaseData, Direction.WEST);
/*     */     }
/* 306 */     if (east) {
/* 307 */       increaseData = withDirection(increaseData, Direction.EAST);
/*     */     }
/* 309 */     return increaseData;
/*     */   }
/*     */ 
/*     */   
/* 313 */   public static int getFromLevel(long entry) { return (int)(entry & 0xFL); }
/*     */ 
/*     */ 
/*     */   
/* 317 */   public static boolean isFromEmptyShape(long entry) { return ((entry & 0x400L) != 0L); }
/*     */ 
/*     */ 
/*     */   
/* 321 */   public static boolean isIncreaseFromEmission(long entry) { return ((entry & 0x800L) != 0L); }
/*     */ 
/*     */ 
/*     */   
/* 325 */   public static boolean shouldPropagateInDirection(long entry, Direction direction) { return ((entry & 1L << direction.ordinal() + 4) != 0L); }
/*     */ 
/*     */ 
/*     */   
/* 329 */   private static long withLevel(long entry, int level) { return entry & 0xFFFFFFFFFFFFFFF0L | level & 0xFL; }
/*     */ 
/*     */ 
/*     */   
/* 333 */   private static long withDirection(long entry, Direction direction) { return entry | 1L << direction.ordinal() + 4; }
/*     */ 
/*     */ 
/*     */   
/* 337 */   private static long withoutDirection(long entry, Direction direction) { return entry & (1L << direction.ordinal() + 4 ^ 0xFFFFFFFFFFFFFFFFL); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\lighting\LightEngine$QueueEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */