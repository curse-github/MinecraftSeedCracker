/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ThreadedLevelLightEngine
/*     */   extends LevelLightEngine
/*     */   implements AutoCloseable
/*     */ {
/*     */   public static final int DEFAULT_BATCH_SIZE = 1000;
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final ConsecutiveExecutor consecutiveExecutor;
/*  32 */   private final ObjectList<Pair<TaskType, Runnable>> lightTasks = new ObjectArrayList();
/*     */   private final ChunkMap chunkMap;
/*     */   private final ChunkTaskDispatcher taskDispatcher;
/*  35 */   private final int taskPerBatch = 1000;
/*  36 */   private final AtomicBoolean scheduled = new AtomicBoolean();
/*     */   
/*     */   public ThreadedLevelLightEngine(LightChunkGetter lightChunkGetter, ChunkMap chunkMap, boolean hasSkyLight, ConsecutiveExecutor consecutiveExecutor, ChunkTaskDispatcher taskDispatcher) {
/*  39 */     super(lightChunkGetter, true, hasSkyLight);
/*  40 */     this.chunkMap = chunkMap;
/*  41 */     this.taskDispatcher = taskDispatcher;
/*  42 */     this.consecutiveExecutor = consecutiveExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */   
/*  51 */   public int runLightUpdates() { throw (UnsupportedOperationException)Util.pauseInIde(new UnsupportedOperationException("Ran automatically on a different thread!")); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos pos) {
/*  56 */     BlockPos immutable = pos.immutable();
/*  57 */     addTask(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), TaskType.PRE_UPDATE, Util.name(() -> super.checkBlock(immutable), () -> "checkBlock " + String.valueOf(immutable)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateChunkStatus(ChunkPos pos) {
/*  62 */     addTask(pos.x, pos.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> {
/*     */             
/*  64 */             super.retainData(pos, false);
/*  65 */             super.setLightEnabled(pos, false);
/*  66 */             for (int sectionY = getMinLightSection(); sectionY < getMaxLightSection(); sectionY++) {
/*  67 */               super.queueSectionData(LightLayer.BLOCK, SectionPos.of(pos, sectionY), null);
/*  68 */               super.queueSectionData(LightLayer.SKY, SectionPos.of(pos, sectionY), null);
/*     */             } 
/*     */ 
/*     */             
/*  72 */             for (int sectionY = this.levelHeightAccessor.getMinSectionY(); sectionY <= this.levelHeightAccessor.getMaxSectionY(); sectionY++) {
/*  73 */               super.updateSectionStatus(SectionPos.of(pos, sectionY), true);
/*     */             }
/*  75 */           }() -> "updateChunkStatus " + String.valueOf(pos) + " true"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) { addTask(pos.x(), pos.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.updateSectionStatus(pos, sectionEmpty), () -> "updateSectionStatus " + String.valueOf(pos) + " " + sectionEmpty)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public void propagateLightSources(ChunkPos pos) { addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> super.propagateLightSources(pos), () -> "propagateLight " + String.valueOf(pos))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   public void setLightEnabled(ChunkPos pos, boolean enable) { addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> super.setLightEnabled(pos, enable), () -> "enableLight " + String.valueOf(pos) + " " + enable)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void queueSectionData(LightLayer layer, SectionPos pos, DataLayer data) { addTask(pos.x(), pos.z(), () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.queueSectionData(layer, pos, data), () -> "queueData " + String.valueOf(pos))); }
/*     */ 
/*     */ 
/*     */   
/* 101 */   private void addTask(int chunkX, int chunkZ, TaskType type, Runnable runnable) { addTask(chunkX, chunkZ, this.chunkMap.getChunkQueueLevel(ChunkPos.asLong(chunkX, chunkZ)), type, runnable); }
/*     */ 
/*     */   
/*     */   private void addTask(int chunkX, int chunkZ, IntSupplier level, TaskType type, Runnable runnable) {
/* 105 */     this.taskDispatcher.submit(() -> {
/* 106 */           this.lightTasks.add(Pair.of(type, runnable));
/* 107 */           if (this.lightTasks.size() >= 1000) {
/* 108 */             runUpdate();
/*     */           }
/* 110 */         }ChunkPos.asLong(chunkX, chunkZ), level);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public void retainData(ChunkPos pos, boolean retain) { addTask(pos.x, pos.z, () -> 0, TaskType.PRE_UPDATE, Util.name(() -> super.retainData(pos, retain), () -> "retainData " + String.valueOf(pos))); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> initializeLight(ChunkAccess chunk, boolean lighted) {
/* 119 */     ChunkPos pos = chunk.getPos();
/* 120 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> {
/* 121 */             LevelChunkSection[] sections = chunk.getSections();
/* 122 */             for (int sectionIndex = 0; sectionIndex < chunk.getSectionsCount(); sectionIndex++) {
/* 123 */               LevelChunkSection section = sections[sectionIndex];
/* 124 */               if (!section.hasOnlyAir()) {
/* 125 */                 int sectionY = this.levelHeightAccessor.getSectionYFromSectionIndex(sectionIndex);
/* 126 */                 super.updateSectionStatus(SectionPos.of(pos, sectionY), false);
/*     */               } 
/*     */             } 
/* 129 */           }() -> "initializeLight: " + String.valueOf(pos)));
/* 130 */     return CompletableFuture.supplyAsync(() -> {
/*     */           
/* 132 */           super.setLightEnabled(pos, lighted);
/* 133 */           super.retainData(pos, false);
/* 134 */           return chunk;
/*     */         
/* 136 */         }r -> addTask(pos.x, pos.z, TaskType.POST_UPDATE, r));
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkAccess> lightChunk(ChunkAccess centerChunk, boolean lighted) {
/* 141 */     ChunkPos pos = centerChunk.getPos();
/* 142 */     centerChunk.setLightCorrect(false);
/* 143 */     addTask(pos.x, pos.z, TaskType.PRE_UPDATE, Util.name(() -> {
/* 144 */             if (!lighted) {
/* 145 */               super.propagateLightSources(pos);
/*     */             }
/* 147 */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/* 148 */               LOGGER.debug("LIT {}", pos);
/*     */             }
/* 150 */           }() -> "lightChunk " + String.valueOf(pos) + " " + lighted));
/* 151 */     return CompletableFuture.supplyAsync(() -> {
/*     */           
/* 153 */           centerChunk.setLightCorrect(true);
/* 154 */           return centerChunk;
/*     */         
/* 156 */         }r -> addTask(pos.x, pos.z, TaskType.POST_UPDATE, r));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tryScheduleUpdate() {
/* 161 */     if ((!this.lightTasks.isEmpty() || hasLightWork()) && this.scheduled.compareAndSet(false, true)) {
/* 162 */       this.consecutiveExecutor.schedule(() -> {
/* 163 */             runUpdate();
/* 164 */             this.scheduled.set(false);
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   private void runUpdate() {
/* 170 */     int totalSize = Math.min(this.lightTasks.size(), 1000);
/*     */     
/* 172 */     ObjectListIterator<Pair<TaskType, Runnable>> iterator = this.lightTasks.iterator();
/*     */     
/* 174 */     int count = 0;
/* 175 */     while (iterator.hasNext() && count < totalSize) {
/* 176 */       Pair<TaskType, Runnable> task = (Pair)iterator.next();
/* 177 */       if (task.getFirst() == TaskType.PRE_UPDATE) {
/* 178 */         ((Runnable)task.getSecond()).run();
/*     */       }
/* 180 */       count++;
/*     */     } 
/* 182 */     iterator.back(count);
/*     */     
/* 184 */     super.runLightUpdates();
/*     */     
/* 186 */     count = 0;
/* 187 */     while (iterator.hasNext() && count < totalSize) {
/* 188 */       Pair<TaskType, Runnable> task = (Pair)iterator.next();
/* 189 */       if (task.getFirst() == TaskType.POST_UPDATE) {
/* 190 */         ((Runnable)task.getSecond()).run();
/*     */       }
/* 192 */       iterator.remove();
/* 193 */       count++;
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> waitForPendingTasks(int chunkX, int chunkZ) {
/* 198 */     return CompletableFuture.runAsync(() -> {
/*     */         
/* 200 */         }r -> addTask(chunkX, chunkZ, TaskType.POST_UPDATE, r));
/*     */   }
/*     */   
/*     */   private enum TaskType
/*     */   {
/* 205 */     PRE_UPDATE, POST_UPDATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\level\ThreadedLevelLightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */