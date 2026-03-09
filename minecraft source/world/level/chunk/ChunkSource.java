/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ChunkSource
/*    */   implements AutoCloseable, LightChunkGetter
/*    */ {
/* 14 */   public LevelChunk getChunk(int x, int z, boolean loadOrGenerate) { return (LevelChunk)getChunk(x, z, ChunkStatus.FULL, loadOrGenerate); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public LevelChunk getChunkNow(int x, int z) { return getChunk(x, z, false); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public LightChunk getChunkForLighting(int x, int z) { return getChunk(x, z, ChunkStatus.EMPTY, false); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public boolean hasChunk(int x, int z) { return (getChunk(x, z, ChunkStatus.FULL, false) != null); }
/*    */ 
/*    */   
/*    */   public abstract ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   public abstract void tick(BooleanSupplier paramBooleanSupplier, boolean paramBoolean);
/*    */ 
/*    */   
/*    */   public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {}
/*    */ 
/*    */   
/*    */   public abstract String gatherStats();
/*    */ 
/*    */   
/*    */   public abstract int getLoadedChunksCount();
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */   
/*    */   public abstract LevelLightEngine getLightEngine();
/*    */ 
/*    */   
/*    */   public void setSpawnSettings(boolean spawnEnemies) {}
/*    */ 
/*    */   
/* 54 */   public boolean updateChunkForced(ChunkPos pos, boolean forced) { return false; }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public LongSet getForceLoadedChunks() { return LongSet.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\ChunkSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */