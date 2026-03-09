/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.entity.ChunkEntities;
/*     */ import net.minecraft.world.level.entity.EntityPersistentStorage;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityStorage
/*     */   extends Object implements EntityPersistentStorage<Entity> {
/*  32 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String ENTITIES_TAG = "Entities"; private static final String POSITION_TAG = "Position";
/*     */   private final ServerLevel level;
/*     */   private final SimpleRegionStorage simpleRegionStorage;
/*     */   private final LongSet emptyChunks;
/*     */   private final ConsecutiveExecutor entityDeserializerQueue;
/*     */   
/*     */   public EntityStorage(SimpleRegionStorage simpleRegionStorage, ServerLevel level, Executor mainThreadExecutor) {
/*  39 */     this.emptyChunks = new LongOpenHashSet();
/*     */ 
/*     */ 
/*     */     
/*  43 */     this.simpleRegionStorage = simpleRegionStorage;
/*  44 */     this.level = level;
/*  45 */     this.entityDeserializerQueue = new ConsecutiveExecutor(mainThreadExecutor, "entity-deserializer");
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<ChunkEntities<Entity>> loadEntities(ChunkPos pos) {
/*  50 */     if (this.emptyChunks.contains(pos.toLong())) {
/*  51 */       return CompletableFuture.completedFuture(emptyChunk(pos));
/*     */     }
/*  53 */     CompletableFuture<Optional<CompoundTag>> loadFuture = this.simpleRegionStorage.read(pos);
/*  54 */     reportLoadFailureIfPresent(loadFuture, pos);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Objects.requireNonNull(this.entityDeserializerQueue); return loadFuture.thenApplyAsync(tag -> { if (tag.isEmpty()) { this.emptyChunks.add(pos.toLong()); return emptyChunk(pos); }  try { ChunkPos storedPos = (ChunkPos)((CompoundTag)tag.get()).read("Position", ChunkPos.CODEC).orElseThrow(); if (!Objects.equals(pos, storedPos)) { LOGGER.error("Chunk file at {} is in the wrong location. (Expected {}, got {})", new Object[] { pos, pos, storedPos }); this.level.getServer().reportMisplacedChunk(storedPos, pos, this.simpleRegionStorage.storageInfo()); }  } catch (Exception e) { LOGGER.warn("Failed to parse chunk {} position info", pos, e); this.level.getServer().reportChunkLoadFailure(e, this.simpleRegionStorage.storageInfo(), pos); }  CompoundTag upgradedChunkTag = this.simpleRegionStorage.upgradeChunkTag((CompoundTag)tag.get(), -1); ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(pos), LOGGER); try { ValueInput chunkRoot = TagValueInput.create(reporter, this.level.registryAccess(), upgradedChunkTag); ValueInput.ValueInputList entities = chunkRoot.childrenListOrEmpty("Entities"); List<Entity> chunkEntities = EntityType.loadEntitiesRecursive(entities, this.level, EntitySpawnReason.LOAD).toList(); ChunkEntities chunkEntities1 = new ChunkEntities(pos, chunkEntities); reporter.close(); return chunkEntities1; } catch (Throwable throwable) { try { reporter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  }this.entityDeserializerQueue::schedule);
/*     */   }
/*     */ 
/*     */   
/*  90 */   private static ChunkEntities<Entity> emptyChunk(ChunkPos pos) { return new ChunkEntities(pos, List.of()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void storeEntities(ChunkEntities<Entity> chunk) {
/*  95 */     ChunkPos pos = chunk.getPos();
/*  96 */     if (chunk.isEmpty()) {
/*  97 */       if (this.emptyChunks.add(pos.toLong())) {
/*  98 */         reportSaveFailureIfPresent(this.simpleRegionStorage.write(pos, IOWorker.STORE_EMPTY), pos);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 103 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(ChunkAccess.problemPath(pos), LOGGER); 
/* 104 */     try { ListTag entities = new ListTag();
/*     */       
/* 106 */       chunk.getEntities().forEach(e -> {
/* 107 */             TagValueOutput output = TagValueOutput.createWithContext(reporter.forChild(e.problemPath()), e.registryAccess());
/* 108 */             if (e.save(output)) {
/* 109 */               CompoundTag result = output.buildResult();
/* 110 */               entities.add(result);
/*     */             } 
/*     */           });
/*     */       
/* 114 */       CompoundTag chunkTag = NbtUtils.addCurrentDataVersion(new CompoundTag());
/* 115 */       chunkTag.put("Entities", entities);
/* 116 */       chunkTag.store("Position", ChunkPos.CODEC, pos);
/* 117 */       reportSaveFailureIfPresent(this.simpleRegionStorage.write(pos, chunkTag), pos);
/* 118 */       this.emptyChunks.remove(pos.toLong());
/* 119 */       reporter.close(); }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 123 */      } private void reportSaveFailureIfPresent(CompletableFuture<?> operation, ChunkPos pos) { operation.exceptionally(t -> {
/* 124 */           LOGGER.error("Failed to store entity chunk {}", pos, t);
/* 125 */           this.level.getServer().reportChunkSaveFailure(t, this.simpleRegionStorage.storageInfo(), pos);
/* 126 */           return null;
/*     */         }); }
/*     */ 
/*     */   
/*     */   private void reportLoadFailureIfPresent(CompletableFuture<?> operation, ChunkPos pos) {
/* 131 */     operation.exceptionally(t -> {
/* 132 */           LOGGER.error("Failed to load entity chunk {}", pos, t);
/* 133 */           this.level.getServer().reportChunkLoadFailure(t, this.simpleRegionStorage.storageInfo(), pos);
/* 134 */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(boolean flushStorage) {
/* 140 */     this.simpleRegionStorage.synchronize(flushStorage).join();
/* 141 */     this.entityDeserializerQueue.runAll();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public void close() throws IOException { this.simpleRegionStorage.close(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\EntityStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */