/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*     */ import java.util.BitSet;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ public class ChunkHolder
/*     */   extends GenerationChunkHolder
/*     */ {
/*  33 */   public static final ChunkResult<LevelChunk> UNLOADED_LEVEL_CHUNK = ChunkResult.error("Unloaded level chunk");
/*  34 */   private static final CompletableFuture<ChunkResult<LevelChunk>> UNLOADED_LEVEL_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_LEVEL_CHUNK);
/*     */ 
/*     */   
/*     */   private final LevelHeightAccessor levelHeightAccessor;
/*     */   
/*     */   private int oldTicketLevel;
/*     */   
/*     */   private int ticketLevel;
/*     */   
/*     */   private int queueLevel;
/*     */   
/*     */   private boolean hasChangedSections;
/*     */   
/*     */   private final ShortSet[] changedBlocksPerSection;
/*     */   
/*  49 */   private final BitSet blockChangedLightSectionFilter = new BitSet();
/*  50 */   private final BitSet skyChangedLightSectionFilter = new BitSet();
/*     */   
/*     */   private final LevelLightEngine lightEngine;
/*     */   private final LevelChangeListener onLevelChange;
/*     */   private final PlayerProvider playerProvider;
/*     */   private boolean wasAccessibleSinceLastSave;
/*  56 */   private CompletableFuture<?> pendingFullStateConfirmation = CompletableFuture.completedFuture(null);
/*  57 */   private CompletableFuture<?> sendSync = CompletableFuture.completedFuture(null);
/*  58 */   private CompletableFuture<?> saveSync = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public ChunkHolder(ChunkPos pos, int ticketLevel, LevelHeightAccessor levelHeightAccessor, LevelLightEngine lightEngine, LevelChangeListener onLevelChange, PlayerProvider playerProvider) {
/*  61 */     super(pos);
/*  62 */     this.levelHeightAccessor = levelHeightAccessor;
/*  63 */     this.lightEngine = lightEngine;
/*  64 */     this.onLevelChange = onLevelChange;
/*  65 */     this.playerProvider = playerProvider;
/*  66 */     this.oldTicketLevel = ChunkLevel.MAX_LEVEL + 1;
/*  67 */     this.ticketLevel = this.oldTicketLevel;
/*  68 */     this.queueLevel = this.oldTicketLevel;
/*  69 */     setTicketLevel(ticketLevel);
/*  70 */     this.changedBlocksPerSection = new ShortSet[levelHeightAccessor.getSectionsCount()];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public CompletableFuture<ChunkResult<LevelChunk>> getTickingChunkFuture() { return this.tickingChunkFuture; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   public CompletableFuture<ChunkResult<LevelChunk>> getEntityTickingChunkFuture() { return this.entityTickingChunkFuture; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public CompletableFuture<ChunkResult<LevelChunk>> getFullChunkFuture() { return this.fullChunkFuture; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   public LevelChunk getTickingChunk() { return (LevelChunk)((ChunkResult)getTickingChunkFuture().getNow(UNLOADED_LEVEL_CHUNK)).orElse(null); }
/*     */ 
/*     */   
/*     */   public LevelChunk getChunkToSend() {
/* 102 */     if (!this.sendSync.isDone()) {
/* 103 */       return null;
/*     */     }
/* 105 */     return getTickingChunk();
/*     */   }
/*     */ 
/*     */   
/* 109 */   public CompletableFuture<?> getSendSyncFuture() { return this.sendSync; }
/*     */ 
/*     */   
/*     */   public void addSendDependency(CompletableFuture<?> sync) {
/* 113 */     if (this.sendSync.isDone()) {
/* 114 */       this.sendSync = sync;
/*     */     } else {
/* 116 */       this.sendSync = this.sendSync.thenCombine(sync, (a, b) -> null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 121 */   public CompletableFuture<?> getSaveSyncFuture() { return this.saveSync; }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public boolean isReadyForSaving() { return this.saveSync.isDone(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addSaveDependency(CompletableFuture<?> sync) {
/* 130 */     if (this.saveSync.isDone()) {
/* 131 */       this.saveSync = sync;
/*     */     } else {
/* 133 */       this.saveSync = this.saveSync.thenCombine(sync, (a, b) -> null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean blockChanged(BlockPos pos) {
/* 140 */     LevelChunk chunk = getTickingChunk();
/* 141 */     if (chunk == null) {
/* 142 */       return false;
/*     */     }
/*     */     
/* 145 */     boolean hadChangedSections = this.hasChangedSections;
/* 146 */     int sectionIndex = this.levelHeightAccessor.getSectionIndex(pos.getY());
/* 147 */     ShortOpenHashSet shortOpenHashSet = this.changedBlocksPerSection[sectionIndex];
/* 148 */     if (shortOpenHashSet == null) {
/* 149 */       this.hasChangedSections = true;
/* 150 */       shortOpenHashSet = new ShortOpenHashSet();
/* 151 */       this.changedBlocksPerSection[sectionIndex] = shortOpenHashSet;
/*     */     } 
/* 153 */     shortOpenHashSet.add(SectionPos.sectionRelativePos(pos));
/* 154 */     return !hadChangedSections;
/*     */   }
/*     */   
/*     */   public boolean sectionLightChanged(LightLayer layer, int chunkY) {
/* 158 */     ChunkAccess chunk = getChunkIfPresent(ChunkStatus.INITIALIZE_LIGHT);
/* 159 */     if (chunk == null) {
/* 160 */       return false;
/*     */     }
/*     */     
/* 163 */     chunk.markUnsaved();
/*     */     
/* 165 */     LevelChunk tickingChunk = getTickingChunk();
/* 166 */     if (tickingChunk == null) {
/* 167 */       return false;
/*     */     }
/*     */     
/* 170 */     int minLightSection = this.lightEngine.getMinLightSection();
/* 171 */     int maxLightSection = this.lightEngine.getMaxLightSection();
/* 172 */     if (chunkY < minLightSection || chunkY > maxLightSection) {
/* 173 */       return false;
/*     */     }
/*     */     
/* 176 */     BitSet filter = (layer == LightLayer.SKY) ? this.skyChangedLightSectionFilter : this.blockChangedLightSectionFilter;
/* 177 */     int index = chunkY - minLightSection;
/* 178 */     if (!filter.get(index)) {
/* 179 */       filter.set(index);
/* 180 */       return true;
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 186 */   public boolean hasChangesToBroadcast() { return (this.hasChangedSections || !this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty()); }
/*     */ 
/*     */   
/*     */   public void broadcastChanges(LevelChunk chunk) {
/* 190 */     if (!hasChangesToBroadcast()) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     Level level = chunk.getLevel();
/*     */     
/* 196 */     if (!this.skyChangedLightSectionFilter.isEmpty() || !this.blockChangedLightSectionFilter.isEmpty()) {
/* 197 */       List<ServerPlayer> borderPlayers = this.playerProvider.getPlayers(this.pos, true);
/* 198 */       if (!borderPlayers.isEmpty()) {
/* 199 */         ClientboundLightUpdatePacket lightPacket = new ClientboundLightUpdatePacket(chunk.getPos(), this.lightEngine, this.skyChangedLightSectionFilter, this.blockChangedLightSectionFilter);
/* 200 */         broadcast(borderPlayers, lightPacket);
/*     */       } 
/* 202 */       this.skyChangedLightSectionFilter.clear();
/* 203 */       this.blockChangedLightSectionFilter.clear();
/*     */     } 
/*     */     
/* 206 */     if (!this.hasChangedSections) {
/*     */       return;
/*     */     }
/*     */     
/* 210 */     List<ServerPlayer> players = this.playerProvider.getPlayers(this.pos, false);
/* 211 */     for (int sectionIndex = 0; sectionIndex < this.changedBlocksPerSection.length; sectionIndex++) {
/* 212 */       ShortSet changedBlocks = this.changedBlocksPerSection[sectionIndex];
/* 213 */       if (changedBlocks != null) {
/*     */ 
/*     */         
/* 216 */         this.changedBlocksPerSection[sectionIndex] = null;
/*     */         
/* 218 */         if (!players.isEmpty()) {
/*     */ 
/*     */           
/* 221 */           int sectionY = this.levelHeightAccessor.getSectionYFromSectionIndex(sectionIndex);
/* 222 */           SectionPos sectionPos = SectionPos.of(chunk.getPos(), sectionY);
/*     */           
/* 224 */           if (changedBlocks.size() == 1)
/* 225 */           { BlockPos pos = sectionPos.relativeToBlockPos(changedBlocks.iterator().nextShort());
/* 226 */             BlockState state = level.getBlockState(pos);
/*     */             
/* 228 */             broadcast(players, new ClientboundBlockUpdatePacket(pos, state));
/* 229 */             broadcastBlockEntityIfNeeded(players, level, pos, state); }
/*     */           else
/* 231 */           { LevelChunkSection section = chunk.getSection(sectionIndex);
/* 232 */             ClientboundSectionBlocksUpdatePacket packet = new ClientboundSectionBlocksUpdatePacket(sectionPos, changedBlocks, section);
/*     */             
/* 234 */             broadcast(players, packet);
/* 235 */             packet.runUpdates((pos, state) -> broadcastBlockEntityIfNeeded(players, level, pos, state)); } 
/*     */         } 
/*     */       } 
/* 238 */     }  this.hasChangedSections = false;
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntityIfNeeded(List<ServerPlayer> players, Level level, BlockPos pos, BlockState state) {
/* 242 */     if (state.hasBlockEntity()) {
/* 243 */       broadcastBlockEntity(players, level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntity(List<ServerPlayer> players, Level level, BlockPos blockPos) {
/* 248 */     BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 249 */     if (blockEntity != null) {
/* 250 */       Packet<?> packet = blockEntity.getUpdatePacket();
/* 251 */       if (packet != null) {
/* 252 */         broadcast(players, packet);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 258 */   private void broadcast(List<ServerPlayer> players, Packet<?> packet) { players.forEach(player -> player.connection.send(packet)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 263 */   public int getTicketLevel() { return this.ticketLevel; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   public int getQueueLevel() { return this.queueLevel; }
/*     */ 
/*     */ 
/*     */   
/* 272 */   private void setQueueLevel(int queueLevel) { this.queueLevel = queueLevel; }
/*     */ 
/*     */ 
/*     */   
/* 276 */   public void setTicketLevel(int ticketLevel) { this.ticketLevel = ticketLevel; }
/*     */ 
/*     */ 
/*     */   
/* 280 */   private void scheduleFullChunkPromotion(ChunkMap scheduler, CompletableFuture<ChunkResult<LevelChunk>> task, Executor mainThreadExecutor, FullChunkStatus status) { this.pendingFullStateConfirmation.cancel(false);
/* 281 */     CompletableFuture<Void> confirmation = new CompletableFuture<Void>();
/* 282 */     confirmation.thenRunAsync(() -> scheduler.onFullChunkStatusChange(this.pos, status), mainThreadExecutor);
/* 283 */     this.pendingFullStateConfirmation = confirmation;
/* 284 */     task.thenAccept(r -> r.ifSuccess(())); }
/*     */ 
/*     */   
/*     */   private void demoteFullChunk(ChunkMap scheduler, FullChunkStatus status) {
/* 288 */     this.pendingFullStateConfirmation.cancel(false);
/* 289 */     scheduler.onFullChunkStatusChange(this.pos, status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateFutures(ChunkMap scheduler, Executor mainThreadExecutor) {
/* 296 */     FullChunkStatus oldFullStatus = ChunkLevel.fullStatus(this.oldTicketLevel);
/* 297 */     FullChunkStatus newFullStatus = ChunkLevel.fullStatus(this.ticketLevel);
/*     */     
/* 299 */     boolean wasAccessible = oldFullStatus.isOrAfter(FullChunkStatus.FULL);
/* 300 */     boolean isAccessible = newFullStatus.isOrAfter(FullChunkStatus.FULL);
/* 301 */     this.wasAccessibleSinceLastSave |= isAccessible;
/*     */     
/* 303 */     if (!wasAccessible && isAccessible) {
/* 304 */       this.fullChunkFuture = scheduler.prepareAccessibleChunk(this);
/* 305 */       scheduleFullChunkPromotion(scheduler, this.fullChunkFuture, mainThreadExecutor, FullChunkStatus.FULL);
/* 306 */       addSaveDependency(this.fullChunkFuture);
/*     */     } 
/* 308 */     if (wasAccessible && !isAccessible) {
/* 309 */       this.fullChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 310 */       this.fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 313 */     boolean wasTicking = oldFullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/* 314 */     boolean isTicking = newFullStatus.isOrAfter(FullChunkStatus.BLOCK_TICKING);
/*     */     
/* 316 */     if (!wasTicking && isTicking) {
/* 317 */       this.tickingChunkFuture = scheduler.prepareTickingChunk(this);
/* 318 */       scheduleFullChunkPromotion(scheduler, this.tickingChunkFuture, mainThreadExecutor, FullChunkStatus.BLOCK_TICKING);
/* 319 */       addSaveDependency(this.tickingChunkFuture);
/*     */     } 
/* 321 */     if (wasTicking && !isTicking) {
/* 322 */       this.tickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 323 */       this.tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 326 */     boolean wasEntityTicking = oldFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/* 327 */     boolean isEntityTicking = newFullStatus.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/*     */     
/* 329 */     if (!wasEntityTicking && isEntityTicking) {
/* 330 */       if (this.entityTickingChunkFuture != UNLOADED_LEVEL_CHUNK_FUTURE) {
/* 331 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException());
/*     */       }
/* 333 */       this.entityTickingChunkFuture = scheduler.prepareEntityTickingChunk(this);
/* 334 */       scheduleFullChunkPromotion(scheduler, this.entityTickingChunkFuture, mainThreadExecutor, FullChunkStatus.ENTITY_TICKING);
/* 335 */       addSaveDependency(this.entityTickingChunkFuture);
/*     */     } 
/* 337 */     if (wasEntityTicking && !isEntityTicking) {
/* 338 */       this.entityTickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 339 */       this.entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 342 */     if (!newFullStatus.isOrAfter(oldFullStatus)) {
/* 343 */       demoteFullChunk(scheduler, newFullStatus);
/*     */     }
/*     */     
/* 346 */     this.onLevelChange.onLevelChange(this.pos, this::getQueueLevel, this.ticketLevel, this::setQueueLevel);
/* 347 */     this.oldTicketLevel = this.ticketLevel;
/*     */   }
/*     */ 
/*     */   
/* 351 */   public boolean wasAccessibleSinceLastSave() { return this.wasAccessibleSinceLastSave; }
/*     */ 
/*     */ 
/*     */   
/* 355 */   public void refreshAccessibility() { this.wasAccessibleSinceLastSave = ChunkLevel.fullStatus(this.ticketLevel).isOrAfter(FullChunkStatus.FULL); }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LevelChangeListener {
/*     */     void onLevelChange(ChunkPos param1ChunkPos, IntSupplier param1IntSupplier, int param1Int, IntConsumer param1IntConsumer);
/*     */   }
/*     */   
/*     */   public static interface PlayerProvider {
/*     */     List<ServerPlayer> getPlayers(ChunkPos param1ChunkPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */