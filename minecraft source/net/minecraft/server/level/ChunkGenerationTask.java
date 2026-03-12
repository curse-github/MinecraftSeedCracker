/*     */ package net.minecraft.server.level;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkDependencies;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class ChunkGenerationTask {
/*     */   private final GeneratingChunkMap chunkMap;
/*     */   private final ChunkPos pos;
/*     */   private ChunkStatus scheduledStatus;
/*     */   public final ChunkStatus targetStatus;
/*     */   private final List<CompletableFuture<ChunkResult<ChunkAccess>>> scheduledLayer;
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   private boolean needsGeneration;
/*     */   
/*     */   private ChunkGenerationTask(GeneratingChunkMap chunkMap, ChunkStatus targetStatus, ChunkPos pos, StaticCache2D<GenerationChunkHolder> cache) {
/*  21 */     this.scheduledStatus = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  26 */     this.scheduledLayer = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  32 */     this.chunkMap = chunkMap;
/*  33 */     this.targetStatus = targetStatus;
/*  34 */     this.pos = pos;
/*  35 */     this.cache = cache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChunkGenerationTask create(GeneratingChunkMap chunkMap, ChunkStatus targetStatus, ChunkPos pos) {
/*  42 */     int worstCaseRadius = ChunkPyramid.GENERATION_PYRAMID.getStepTo(targetStatus).getAccumulatedRadiusOf(ChunkStatus.EMPTY);
/*  43 */     StaticCache2D<GenerationChunkHolder> cache = StaticCache2D.create(pos.x, pos.z, worstCaseRadius, (x, z) -> chunkMap.acquireGeneration(ChunkPos.asLong(x, z)));
/*     */     
/*  45 */     return new ChunkGenerationTask(chunkMap, targetStatus, pos, cache);
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> runUntilWait() {
/*     */     while (true) {
/*  50 */       CompletableFuture<?> waitingFor = waitForScheduledLayer();
/*  51 */       if (waitingFor != null) {
/*  52 */         return waitingFor;
/*     */       }
/*  54 */       if (this.markedForCancellation || this.scheduledStatus == this.targetStatus) {
/*     */         
/*  56 */         releaseClaim();
/*  57 */         return null;
/*     */       } 
/*  59 */       scheduleNextLayer();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleNextLayer() {
/*     */     ChunkStatus statusToSchedule;
/*  65 */     if (this.scheduledStatus == null) {
/*  66 */       statusToSchedule = ChunkStatus.EMPTY;
/*  67 */     } else if (!this.needsGeneration && this.scheduledStatus == ChunkStatus.EMPTY && !canLoadWithoutGeneration()) {
/*  68 */       this.needsGeneration = true;
/*     */       
/*  70 */       statusToSchedule = ChunkStatus.EMPTY;
/*     */     } else {
/*  72 */       statusToSchedule = (ChunkStatus)ChunkStatus.getStatusList().get(this.scheduledStatus.getIndex() + 1);
/*     */     } 
/*  74 */     scheduleLayer(statusToSchedule, this.needsGeneration);
/*  75 */     this.scheduledStatus = statusToSchedule;
/*     */   }
/*     */ 
/*     */   
/*  79 */   public void markForCancellation() { this.markedForCancellation = true; }
/*     */ 
/*     */   
/*     */   private void releaseClaim() {
/*  83 */     GenerationChunkHolder chunkHolder = (GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z);
/*     */     
/*  85 */     chunkHolder.removeTask(this);
/*  86 */     Objects.requireNonNull(this.chunkMap); this.cache.forEach(this.chunkMap::releaseGeneration);
/*     */   }
/*     */   
/*     */   private boolean canLoadWithoutGeneration() {
/*  90 */     if (this.targetStatus == ChunkStatus.EMPTY) {
/*  91 */       return true;
/*     */     }
/*  93 */     ChunkStatus highestGeneratedStatus = ((GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z)).getPersistedStatus();
/*  94 */     if (highestGeneratedStatus == null || highestGeneratedStatus.isBefore(this.targetStatus)) {
/*  95 */       return false;
/*     */     }
/*  97 */     ChunkDependencies dependencies = ChunkPyramid.LOADING_PYRAMID.getStepTo(this.targetStatus).accumulatedDependencies();
/*  98 */     int range = dependencies.getRadius();
/*  99 */     for (int x = this.pos.x - range; x <= this.pos.x + range; x++) {
/* 100 */       for (int z = this.pos.z - range; z <= this.pos.z + range; z++) {
/* 101 */         int distance = this.pos.getChessboardDistance(x, z);
/* 102 */         ChunkStatus requiredStatus = dependencies.get(distance);
/* 103 */         ChunkStatus persistedStatus = ((GenerationChunkHolder)this.cache.get(x, z)).getPersistedStatus();
/* 104 */         if (persistedStatus == null || persistedStatus.isBefore(requiredStatus)) {
/* 105 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 113 */   public GenerationChunkHolder getCenter() { return (GenerationChunkHolder)this.cache.get(this.pos.x, this.pos.z); }
/*     */ 
/*     */   
/*     */   private void scheduleLayer(ChunkStatus status, boolean needsGeneration) {
/* 117 */     Zone zone = Profiler.get().zone("scheduleLayer"); 
/* 118 */     try { Objects.requireNonNull(status); zone.addText(status::getName);
/* 119 */       int radius = getRadiusForLayer(status, needsGeneration);
/* 120 */       for (int x = this.pos.x - radius; x <= this.pos.x + radius; x++)
/* 121 */       { for (int z = this.pos.z - radius; z <= this.pos.z + radius; z++)
/* 122 */         { GenerationChunkHolder chunkHolder = (GenerationChunkHolder)this.cache.get(x, z);
/* 123 */           if (this.markedForCancellation || !scheduleChunkInLayer(status, needsGeneration, chunkHolder))
/*     */           
/*     */           { 
/*     */ 
/*     */             
/* 128 */             if (zone != null) zone.close();  return; }  }  }  if (zone != null) zone.close();  }
/*     */     catch (Throwable throwable) { if (zone != null)
/*     */         try { zone.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 133 */      } private int getRadiusForLayer(ChunkStatus status, boolean needsGeneration) { ChunkPyramid pyramid = needsGeneration ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 134 */     return pyramid.getStepTo(this.targetStatus).getAccumulatedRadiusOf(status); }
/*     */ 
/*     */   
/*     */   private boolean scheduleChunkInLayer(ChunkStatus status, boolean needsGeneration, GenerationChunkHolder chunkHolder) {
/* 138 */     ChunkStatus persistedStatus = chunkHolder.getPersistedStatus();
/* 139 */     boolean generate = (persistedStatus != null && status.isAfter(persistedStatus));
/* 140 */     ChunkPyramid pyramid = generate ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 141 */     if (generate && !needsGeneration) {
/* 142 */       throw new IllegalStateException("Can't load chunk, but didn't expect to need to generate");
/*     */     }
/*     */ 
/*     */     
/* 146 */     CompletableFuture<ChunkResult<ChunkAccess>> future = chunkHolder.applyStep(pyramid.getStepTo(status), this.chunkMap, this.cache);
/* 147 */     ChunkResult<ChunkAccess> now = (ChunkResult)future.getNow(null);
/* 148 */     if (now == null) {
/* 149 */       this.scheduledLayer.add(future);
/* 150 */       return true;
/*     */     } 
/*     */     
/* 153 */     if (now.isSuccess()) {
/* 154 */       return true;
/*     */     }
/*     */     
/* 157 */     markForCancellation();
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<?> waitForScheduledLayer() {
/* 163 */     while (!this.scheduledLayer.isEmpty()) {
/* 164 */       CompletableFuture<ChunkResult<ChunkAccess>> lastFuture = (CompletableFuture)this.scheduledLayer.getLast();
/* 165 */       ChunkResult<ChunkAccess> resultNow = (ChunkResult)lastFuture.getNow(null);
/* 166 */       if (resultNow == null) {
/* 167 */         return lastFuture;
/*     */       }
/* 169 */       this.scheduledLayer.removeLast();
/* 170 */       if (!resultNow.isSuccess()) {
/* 171 */         markForCancellation();
/*     */       }
/*     */     } 
/*     */     
/* 175 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ChunkGenerationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */