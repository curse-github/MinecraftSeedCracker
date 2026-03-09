/*     */ package net.minecraft.world.level.block.state.pattern;
/*     */ 
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BlockCacheLoader
/*     */   extends CacheLoader<BlockPos, BlockInWorld>
/*     */ {
/*     */   private final LevelReader level;
/*     */   private final boolean loadChunks;
/*     */   
/*     */   public BlockCacheLoader(LevelReader level, boolean loadChunks) {
/* 126 */     this.level = level;
/* 127 */     this.loadChunks = loadChunks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 132 */   public BlockInWorld load(BlockPos key) { return new BlockInWorld(this.level, key, this.loadChunks); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\pattern\BlockPattern$BlockCacheLoader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */