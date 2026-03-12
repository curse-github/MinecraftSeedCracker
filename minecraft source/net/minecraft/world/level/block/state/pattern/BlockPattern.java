/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ 
/*     */ public class BlockPattern
/*     */ {
/*     */   private final Predicate<BlockInWorld>[][][] pattern;
/*     */   private final int depth;
/*     */   private final int height;
/*     */   private final int width;
/*     */   
/*     */   public BlockPattern(Predicate[][][] pattern) {
/*  23 */     this.pattern = pattern;
/*     */     
/*  25 */     this.depth = pattern.length;
/*     */     
/*  27 */     if (this.depth > 0) {
/*  28 */       this.height = pattern[0].length;
/*     */       
/*  30 */       if (this.height > 0) {
/*  31 */         this.width = pattern[0][0].length;
/*     */       } else {
/*  33 */         this.width = 0;
/*     */       } 
/*     */     } else {
/*  36 */       this.height = 0;
/*  37 */       this.width = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  42 */   public int getDepth() { return this.depth; }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public int getHeight() { return this.height; }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public int getWidth() { return this.width; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*  55 */   public Predicate<BlockInWorld>[][][] getPattern() { return this.pattern; }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   public BlockPatternMatch matches(LevelReader level, BlockPos origin, Direction forwards, Direction up) {
/*  60 */     LoadingCache<BlockPos, BlockInWorld> cache = createLevelCache(level, false);
/*  61 */     return matches(origin, forwards, up, cache);
/*     */   }
/*     */   
/*     */   private BlockPatternMatch matches(BlockPos origin, Direction forwards, Direction up, LoadingCache<BlockPos, BlockInWorld> cache) {
/*  65 */     for (int x = 0; x < this.width; x++) {
/*  66 */       for (int y = 0; y < this.height; y++) {
/*  67 */         for (int z = 0; z < this.depth; z++) {
/*  68 */           if (!this.pattern[z][y][x].test((BlockInWorld)cache.getUnchecked(translateAndRotate(origin, forwards, up, x, y, z)))) {
/*  69 */             return null;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     return new BlockPatternMatch(origin, forwards, up, cache, this.width, this.height, this.depth);
/*     */   }
/*     */   
/*     */   public BlockPatternMatch find(LevelReader level, BlockPos origin) {
/*  79 */     LoadingCache<BlockPos, BlockInWorld> cache = createLevelCache(level, false);
/*     */     
/*  81 */     int dist = Math.max(Math.max(this.width, this.height), this.depth);
/*     */     
/*  83 */     for (BlockPos testPos : BlockPos.betweenClosed(origin, origin.offset(dist - 1, dist - 1, dist - 1))) {
/*  84 */       for (Direction forwards : Direction.values()) {
/*  85 */         for (Direction up : Direction.values()) {
/*  86 */           if (up != forwards && up != forwards.getOpposite()) {
/*     */ 
/*     */ 
/*     */             
/*  90 */             BlockPatternMatch match = matches(testPos, forwards, up, cache);
/*  91 */             if (match != null) {
/*  92 */               return match;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 102 */   public static LoadingCache<BlockPos, BlockInWorld> createLevelCache(LevelReader level, boolean loadChunks) { return CacheBuilder.newBuilder().build(new BlockCacheLoader(level, loadChunks)); }
/*     */ 
/*     */   
/*     */   protected static BlockPos translateAndRotate(BlockPos origin, Direction forwardsDirection, Direction upDirection, int right, int down, int forwards) {
/* 106 */     if (forwardsDirection == upDirection || forwardsDirection == upDirection.getOpposite()) {
/* 107 */       throw new IllegalArgumentException("Invalid forwards & up combination");
/*     */     }
/*     */     
/* 110 */     Vec3i forwardsVector = new Vec3i(forwardsDirection.getStepX(), forwardsDirection.getStepY(), forwardsDirection.getStepZ());
/* 111 */     Vec3i upVector = new Vec3i(upDirection.getStepX(), upDirection.getStepY(), upDirection.getStepZ());
/* 112 */     Vec3i rightVector = forwardsVector.cross(upVector);
/*     */     
/* 114 */     return origin.offset(upVector
/* 115 */         .getX() * -down + rightVector.getX() * right + forwardsVector.getX() * forwards, upVector
/* 116 */         .getY() * -down + rightVector.getY() * right + forwardsVector.getY() * forwards, upVector
/* 117 */         .getZ() * -down + rightVector.getZ() * right + forwardsVector.getZ() * forwards);
/*     */   }
/*     */   
/*     */   private static class BlockCacheLoader
/*     */     extends CacheLoader<BlockPos, BlockInWorld> {
/*     */     private final LevelReader level;
/*     */     private final boolean loadChunks;
/*     */     
/*     */     public BlockCacheLoader(LevelReader level, boolean loadChunks) {
/* 126 */       this.level = level;
/* 127 */       this.loadChunks = loadChunks;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     public BlockInWorld load(BlockPos key) { return new BlockInWorld(this.level, key, this.loadChunks); }
/*     */   }
/*     */   
/*     */   public static class BlockPatternMatch
/*     */   {
/*     */     private final BlockPos frontTopLeft;
/*     */     private final Direction forwards;
/*     */     private final Direction up;
/*     */     private final LoadingCache<BlockPos, BlockInWorld> cache;
/*     */     private final int width;
/*     */     private final int height;
/*     */     private final int depth;
/*     */     
/*     */     public BlockPatternMatch(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, BlockInWorld> cache, int width, int height, int depth) {
/* 146 */       this.frontTopLeft = frontTopLeft;
/* 147 */       this.forwards = forwards;
/* 148 */       this.up = up;
/* 149 */       this.cache = cache;
/* 150 */       this.width = width;
/* 151 */       this.height = height;
/* 152 */       this.depth = depth;
/*     */     }
/*     */ 
/*     */     
/* 156 */     public BlockPos getFrontTopLeft() { return this.frontTopLeft; }
/*     */ 
/*     */ 
/*     */     
/* 160 */     public Direction getForwards() { return this.forwards; }
/*     */ 
/*     */ 
/*     */     
/* 164 */     public Direction getUp() { return this.up; }
/*     */ 
/*     */ 
/*     */     
/* 168 */     public int getWidth() { return this.width; }
/*     */ 
/*     */ 
/*     */     
/* 172 */     public int getHeight() { return this.height; }
/*     */ 
/*     */ 
/*     */     
/* 176 */     public int getDepth() { return this.depth; }
/*     */ 
/*     */ 
/*     */     
/* 180 */     public BlockInWorld getBlock(int right, int down, int forwards) { return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, getForwards(), getUp(), right, down, forwards)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 185 */     public String toString() { return MoreObjects.toStringHelper(this)
/* 186 */         .add("up", this.up)
/* 187 */         .add("forwards", this.forwards)
/* 188 */         .add("frontTopLeft", this.frontTopLeft)
/* 189 */         .toString(); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */