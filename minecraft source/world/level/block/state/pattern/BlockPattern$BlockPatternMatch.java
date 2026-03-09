/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import net.minecraft.core.BlockPos;
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
/*     */ public class BlockPatternMatch
/*     */ {
/*     */   private final BlockPos frontTopLeft;
/*     */   private final Direction forwards;
/*     */   private final Direction up;
/*     */   private final LoadingCache<BlockPos, BlockInWorld> cache;
/*     */   private final int width;
/*     */   private final int height;
/*     */   private final int depth;
/*     */   
/*     */   public BlockPatternMatch(BlockPos frontTopLeft, Direction forwards, Direction up, LoadingCache<BlockPos, BlockInWorld> cache, int width, int height, int depth) {
/* 146 */     this.frontTopLeft = frontTopLeft;
/* 147 */     this.forwards = forwards;
/* 148 */     this.up = up;
/* 149 */     this.cache = cache;
/* 150 */     this.width = width;
/* 151 */     this.height = height;
/* 152 */     this.depth = depth;
/*     */   }
/*     */ 
/*     */   
/* 156 */   public BlockPos getFrontTopLeft() { return this.frontTopLeft; }
/*     */ 
/*     */ 
/*     */   
/* 160 */   public Direction getForwards() { return this.forwards; }
/*     */ 
/*     */ 
/*     */   
/* 164 */   public Direction getUp() { return this.up; }
/*     */ 
/*     */ 
/*     */   
/* 168 */   public int getWidth() { return this.width; }
/*     */ 
/*     */ 
/*     */   
/* 172 */   public int getHeight() { return this.height; }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public int getDepth() { return this.depth; }
/*     */ 
/*     */ 
/*     */   
/* 180 */   public BlockInWorld getBlock(int right, int down, int forwards) { return (BlockInWorld)this.cache.getUnchecked(BlockPattern.translateAndRotate(this.frontTopLeft, getForwards(), getUp(), right, down, forwards)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 185 */   public String toString() { return MoreObjects.toStringHelper(this)
/* 186 */       .add("up", this.up)
/* 187 */       .add("forwards", this.forwards)
/* 188 */       .add("frontTopLeft", this.frontTopLeft)
/* 189 */       .toString(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern$BlockPatternMatch.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */