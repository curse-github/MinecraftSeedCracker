/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class FixedPlayerDistanceChunkTracker
/*     */   extends ChunkTracker
/*     */ {
/* 204 */   protected final Long2ByteMap chunks = new Long2ByteOpenHashMap();
/*     */   protected final int maxDistance;
/*     */   
/*     */   protected FixedPlayerDistanceChunkTracker(int maxDistance) {
/* 208 */     super(maxDistance + 2, 16, 256);
/* 209 */     this.maxDistance = maxDistance;
/* 210 */     this.chunks.defaultReturnValue((byte)(maxDistance + 2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 215 */   protected int getLevel(long node) { return this.chunks.get(node); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setLevel(long node, int level) {
/*     */     byte oldLevel;
/* 221 */     if (level > this.maxDistance) {
/* 222 */       oldLevel = this.chunks.remove(node);
/*     */     } else {
/* 224 */       oldLevel = this.chunks.put(node, (byte)level);
/*     */     } 
/* 226 */     onLevelChange(node, oldLevel, level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onLevelChange(long node, int oldLevel, int level) {}
/*     */ 
/*     */   
/* 234 */   protected int getLevelFromSource(long to) { return havePlayer(to) ? 0 : Integer.MAX_VALUE; }
/*     */ 
/*     */   
/*     */   private boolean havePlayer(long chunkPos) {
/* 238 */     ObjectSet<ServerPlayer> players = (ObjectSet)DistanceManager.this.playersPerChunk.get(chunkPos);
/* 239 */     return (players != null && !players.isEmpty());
/*     */   }
/*     */ 
/*     */   
/* 243 */   public void runAllUpdates() { runUpdates(2147483647); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\DistanceManager$FixedPlayerDistanceChunkTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */