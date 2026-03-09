/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.util.BitSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.SequencedMap;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.CollectFields;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.thread.PriorityConsecutiveExecutor;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOWorker
/*     */   implements AutoCloseable, ChunkScanAccess
/*     */ {
/*  47 */   public static final Supplier<CompoundTag> STORE_EMPTY = () -> null;
/*     */   
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger(); private final AtomicBoolean shutdownRequested; private final PriorityConsecutiveExecutor consecutiveExecutor; private final RegionFileStorage storage; private final SequencedMap<ChunkPos, PendingStore> pendingWrites; private final Long2ObjectLinkedOpenHashMap<CompletableFuture<BitSet>> regionCacheForBlender;
/*     */   private static final int REGION_CACHE_SIZE = 1024;
/*     */   
/*  52 */   private enum Priority { FOREGROUND, BACKGROUND, SHUTDOWN; }
/*     */    @FunctionalInterface
/*     */   private static interface ThrowingSupplier<T> { T get() throws Exception; }
/*     */   private static class PendingStore { private CompoundTag data;
/*     */     public PendingStore(CompoundTag data) {
/*  57 */       this.result = new CompletableFuture();
/*     */ 
/*     */       
/*  60 */       this.data = data;
/*     */     }
/*     */     private final CompletableFuture<Void> result;
/*     */     private CompoundTag copyData() {
/*  64 */       CompoundTag data = this.data;
/*  65 */       return (data == null) ? null : data.copy();
/*     */     } }
/*     */ 
/*     */   
/*     */   protected IOWorker(RegionStorageInfo info, Path dir, boolean sync) {
/*  70 */     this.shutdownRequested = new AtomicBoolean();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     this.pendingWrites = new LinkedHashMap();
/*     */     
/*  77 */     this.regionCacheForBlender = new Long2ObjectLinkedOpenHashMap();
/*     */ 
/*     */ 
/*     */     
/*  81 */     this.storage = new RegionFileStorage(info, dir, sync);
/*  82 */     this.consecutiveExecutor = new PriorityConsecutiveExecutor(Priority.values().length, Util.ioPool(), "IOWorker-" + info.type());
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/*  86 */     ChunkPos from = new ChunkPos(pos.x - range, pos.z - range);
/*  87 */     ChunkPos to = new ChunkPos(pos.x + range, pos.z + range);
/*     */     
/*  89 */     for (int regionX = from.getRegionX(); regionX <= to.getRegionX(); regionX++) {
/*  90 */       for (int regionZ = from.getRegionZ(); regionZ <= to.getRegionZ(); regionZ++) {
/*     */         
/*  92 */         BitSet data = (BitSet)getOrCreateOldDataForRegion(regionX, regionZ).join();
/*  93 */         if (!data.isEmpty()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  98 */           ChunkPos minChunkPos = ChunkPos.minFromRegion(regionX, regionZ);
/*  99 */           int startChunkX = Math.max(from.x - minChunkPos.x, 0);
/* 100 */           int startChunkZ = Math.max(from.z - minChunkPos.z, 0);
/* 101 */           int endChunkX = Math.min(to.x - minChunkPos.x, 31);
/* 102 */           int endChunkZ = Math.min(to.z - minChunkPos.z, 31);
/*     */           
/* 104 */           for (int x = startChunkX; x <= endChunkX; x++) {
/* 105 */             for (int z = startChunkZ; z <= endChunkZ; z++) {
/* 106 */               int chunkIndex = z * 32 + x;
/* 107 */               if (data.get(chunkIndex)) {
/* 108 */                 return true;
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<BitSet> getOrCreateOldDataForRegion(int regionX, int regionZ) {
/* 119 */     long regionPos = ChunkPos.asLong(regionX, regionZ);
/* 120 */     synchronized (this.regionCacheForBlender) {
/* 121 */       CompletableFuture<BitSet> result = (CompletableFuture)this.regionCacheForBlender.getAndMoveToFirst(regionPos);
/* 122 */       if (result == null) {
/* 123 */         result = createOldDataForRegion(regionX, regionZ);
/*     */         
/* 125 */         this.regionCacheForBlender.putAndMoveToFirst(regionPos, result);
/* 126 */         if (this.regionCacheForBlender.size() > 1024) {
/* 127 */           this.regionCacheForBlender.removeLast();
/*     */         }
/*     */       } 
/* 130 */       return result;
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<BitSet> createOldDataForRegion(int regionX, int regionZ) {
/* 135 */     return CompletableFuture.supplyAsync(() -> {
/* 136 */           ChunkPos from = ChunkPos.minFromRegion(regionX, regionZ);
/* 137 */           ChunkPos to = ChunkPos.maxFromRegion(regionX, regionZ);
/*     */           
/* 139 */           BitSet resultSet = new BitSet();
/*     */           
/* 141 */           ChunkPos.rangeClosed(from, to).forEach(());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 162 */           return resultSet;
/* 163 */         }Util.backgroundExecutor());
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isOldChunk(CompoundTag tag) {
/* 168 */     if (tag.getIntOr("DataVersion", 0) < 4295) {
/* 169 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 173 */     return tag.getCompound("blending_data").isPresent();
/*     */   }
/*     */ 
/*     */   
/* 177 */   public CompletableFuture<Void> store(ChunkPos pos, CompoundTag value) { return store(pos, () -> value); }
/*     */ 
/*     */ 
/*     */   
/* 181 */   public CompletableFuture<Void> store(ChunkPos pos, Supplier<CompoundTag> supplier) { return submitTask(() -> {
/* 182 */           CompoundTag data = (CompoundTag)supplier.get();
/* 183 */           PendingStore pendingStore = (PendingStore)this.pendingWrites.computeIfAbsent(pos, ());
/* 184 */           pendingStore.data = data;
/* 185 */           return pendingStore.result;
/* 186 */         }).thenCompose(Function.identity()); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Optional<CompoundTag>> loadAsync(ChunkPos pos) {
/* 190 */     return submitThrowingTask(() -> {
/* 191 */           PendingStore pendingStore = (PendingStore)this.pendingWrites.get(pos);
/* 192 */           if (pendingStore != null) {
/* 193 */             return Optional.ofNullable(pendingStore.copyData());
/*     */           }
/*     */           try {
/* 196 */             CompoundTag data = this.storage.read(pos);
/* 197 */             return Optional.ofNullable(data);
/* 198 */           } catch (Exception e) {
/* 199 */             LOGGER.warn("Failed to read chunk {}", pos, e);
/* 200 */             throw e;
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> synchronize(boolean flush) {
/* 207 */     CompletableFuture<Void> currentWrites = submitTask(() -> CompletableFuture.allOf((CompletableFuture[])this.pendingWrites.values().stream().map(()).toArray(()))).thenCompose(Function.identity());
/* 208 */     if (flush) {
/* 209 */       return currentWrites.thenCompose(ignore -> submitThrowingTask(()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 219 */     return currentWrites.thenCompose(ignore -> submitTask(()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> scanChunk(ChunkPos pos, StreamTagVisitor visitor) {
/* 225 */     return submitThrowingTask(() -> {
/*     */           try {
/* 227 */             PendingStore pendingStore = (PendingStore)this.pendingWrites.get(pos);
/* 228 */             if (pendingStore != null) {
/* 229 */               if (pendingStore.data != null) {
/* 230 */                 pendingStore.data.acceptAsRoot(visitor);
/*     */               }
/*     */             } else {
/* 233 */               this.storage.scanChunk(pos, visitor);
/*     */             } 
/* 235 */             return null;
/* 236 */           } catch (Exception e) {
/* 237 */             LOGGER.warn("Failed to bulk scan chunk {}", pos, e);
/* 238 */             throw e;
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private <T> CompletableFuture<T> submitThrowingTask(ThrowingSupplier<T> task) {
/* 244 */     return this.consecutiveExecutor.scheduleWithResult(Priority.FOREGROUND.ordinal(), future -> {
/* 245 */           if (!this.shutdownRequested.get()) {
/*     */             try {
/* 247 */               future.complete(task.get());
/* 248 */             } catch (Exception e) {
/* 249 */               future.completeExceptionally(e);
/*     */             } 
/*     */           }
/* 252 */           tellStorePending();
/*     */         });
/*     */   }
/*     */   
/*     */   private <T> CompletableFuture<T> submitTask(Supplier<T> task) {
/* 257 */     return this.consecutiveExecutor.scheduleWithResult(Priority.FOREGROUND.ordinal(), future -> {
/* 258 */           if (!this.shutdownRequested.get()) {
/* 259 */             future.complete(task.get());
/*     */           }
/* 261 */           tellStorePending();
/*     */         });
/*     */   }
/*     */   
/*     */   private void storePendingChunk() {
/* 266 */     Map.Entry<ChunkPos, PendingStore> entry = this.pendingWrites.pollFirstEntry();
/* 267 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 270 */     runStore((ChunkPos)entry.getKey(), (PendingStore)entry.getValue());
/* 271 */     tellStorePending();
/*     */   }
/*     */ 
/*     */   
/* 275 */   private void tellStorePending() { this.consecutiveExecutor.schedule(new StrictQueue.RunnableWithPriority(Priority.BACKGROUND.ordinal(), this::storePendingChunk)); }
/*     */ 
/*     */   
/*     */   private void runStore(ChunkPos pos, PendingStore write) {
/*     */     try {
/* 280 */       this.storage.write(pos, write.data);
/* 281 */       write.result.complete(null);
/* 282 */     } catch (Exception e) {
/* 283 */       LOGGER.error("Failed to store chunk {}", pos, e);
/* 284 */       write.result.completeExceptionally(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 290 */     if (!this.shutdownRequested.compareAndSet(false, true)) {
/*     */       return;
/*     */     }
/*     */     
/* 294 */     waitForShutdown();
/* 295 */     this.consecutiveExecutor.close();
/*     */     
/*     */     try {
/* 298 */       this.storage.close();
/* 299 */     } catch (Exception e) {
/* 300 */       LOGGER.error("Failed to close storage", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 306 */   private void waitForShutdown() { this.consecutiveExecutor.scheduleWithResult(Priority.SHUTDOWN.ordinal(), future -> future.complete(Unit.INSTANCE)).join(); }
/*     */ 
/*     */ 
/*     */   
/* 310 */   public RegionStorageInfo storageInfo() { return this.storage.info(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\IOWorker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */