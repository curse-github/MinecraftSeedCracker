/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PlayerTicketTracker
/*     */   extends DistanceManager.FixedPlayerDistanceChunkTracker
/*     */ {
/*     */   private int viewDistance;
/* 250 */   private final Long2IntMap queueLevels = Long2IntMaps.synchronize(new Long2IntOpenHashMap());
/* 251 */   private final LongSet toUpdate = new LongOpenHashSet();
/*     */   
/*     */   protected PlayerTicketTracker(int maxDistance) {
/* 254 */     super(paramDistanceManager, maxDistance);
/* 255 */     this.viewDistance = 0;
/* 256 */     this.queueLevels.defaultReturnValue(maxDistance + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 261 */   protected void onLevelChange(long node, int oldLevel, int level) { this.toUpdate.add(node); }
/*     */ 
/*     */   
/*     */   public void updateViewDistance(int viewDistance) {
/* 265 */     for (ObjectIterator objectIterator = this.chunks.long2ByteEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ByteMap.Entry entry = (Long2ByteMap.Entry)objectIterator.next();
/* 266 */       byte level = entry.getByteValue();
/* 267 */       long key = entry.getLongKey();
/* 268 */       onLevelChange(key, level, haveTicketFor(level), (level <= viewDistance)); }
/*     */     
/* 270 */     this.viewDistance = viewDistance;
/*     */   }
/*     */   
/*     */   private void onLevelChange(long key, int level, boolean saw, boolean sees) {
/* 274 */     if (saw != sees) {
/* 275 */       Ticket ticket = new Ticket(TicketType.PLAYER_LOADING, DistanceManager.PLAYER_TICKET_LEVEL);
/* 276 */       if (sees) {
/* 277 */         DistanceManager.this.ticketDispatcher.submit(() -> DistanceManager.this.mainThreadExecutor.execute(()), key, () -> 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 284 */             level);
/*     */       } else {
/* 286 */         DistanceManager.this.ticketDispatcher.release(key, () -> DistanceManager.this.mainThreadExecutor.execute(()), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void runAllUpdates() {
/* 293 */     super.runAllUpdates();
/* 294 */     if (!this.toUpdate.isEmpty()) {
/* 295 */       LongIterator iterator = this.toUpdate.iterator();
/* 296 */       while (iterator.hasNext()) {
/* 297 */         long node = iterator.nextLong();
/* 298 */         int oldLevel = this.queueLevels.get(node);
/* 299 */         int level = getLevel(node);
/* 300 */         if (oldLevel != level) {
/* 301 */           DistanceManager.this.ticketDispatcher.onLevelChange(new ChunkPos(node), () -> this.queueLevels.get(node), level, l -> {
/* 302 */                 if (l >= this.queueLevels.defaultReturnValue()) {
/* 303 */                   this.queueLevels.remove(node);
/*     */                 } else {
/* 305 */                   this.queueLevels.put(node, l);
/*     */                 } 
/*     */               });
/* 308 */           onLevelChange(node, level, haveTicketFor(oldLevel), haveTicketFor(level));
/*     */         } 
/*     */       } 
/* 311 */       this.toUpdate.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 316 */   private boolean haveTicketFor(int level) { return (level <= this.viewDistance); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\DistanceManager$PlayerTicketTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */