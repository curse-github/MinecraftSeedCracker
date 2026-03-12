/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongListIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SectionStorage<R, P>
/*     */   extends Object implements AutoCloseable {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final String SECTIONS_TAG = "Sections"; private final SimpleRegionStorage simpleRegionStorage; private final Long2ObjectMap<Optional<R>> storage; private final LongLinkedOpenHashSet dirtyChunks;
/*     */   private final Codec<P> codec;
/*     */   private final Function<R, P> packer;
/*     */   private final BiFunction<P, Runnable, R> unpacker;
/*     */   
/*     */   public SectionStorage(SimpleRegionStorage simpleRegionStorage, Codec<P> codec, Function<R, P> packer, BiFunction<P, Runnable, R> unpacker, Function<Runnable, R> factory, RegistryAccess registryAccess, ChunkIOErrorReporter errorReporter, LevelHeightAccessor levelHeightAccessor) {
/*  50 */     this.storage = new Long2ObjectOpenHashMap();
/*  51 */     this.dirtyChunks = new LongLinkedOpenHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.loadedChunks = new LongOpenHashSet();
/*  62 */     this.pendingLoads = new Long2ObjectOpenHashMap();
/*  63 */     this.loadLock = new Object();
/*     */ 
/*     */     
/*  66 */     this.simpleRegionStorage = simpleRegionStorage;
/*  67 */     this.codec = codec;
/*  68 */     this.packer = packer;
/*  69 */     this.unpacker = unpacker;
/*  70 */     this.factory = factory;
/*  71 */     this.registryAccess = registryAccess;
/*  72 */     this.errorReporter = errorReporter;
/*  73 */     this.levelHeightAccessor = levelHeightAccessor;
/*     */   }
/*     */   private final Function<Runnable, R> factory; private final RegistryAccess registryAccess; private final ChunkIOErrorReporter errorReporter; protected final LevelHeightAccessor levelHeightAccessor; private final LongSet loadedChunks; private final Long2ObjectMap<CompletableFuture<Optional<PackedChunk<P>>>> pendingLoads; private final Object loadLock;
/*     */   protected void tick(BooleanSupplier haveTime) {
/*  77 */     LongListIterator longListIterator = this.dirtyChunks.iterator();
/*  78 */     while (longListIterator.hasNext() && haveTime.getAsBoolean()) {
/*  79 */       ChunkPos chunkPos = new ChunkPos(longListIterator.nextLong());
/*  80 */       longListIterator.remove();
/*  81 */       writeChunk(chunkPos);
/*     */     } 
/*     */     
/*  84 */     unpackPendingLoads();
/*     */   }
/*     */   
/*     */   private void unpackPendingLoads() {
/*  88 */     synchronized (this.loadLock) {
/*  89 */       ObjectIterator objectIterator = Long2ObjectMaps.fastIterator(this.pendingLoads);
/*  90 */       while (objectIterator.hasNext()) {
/*  91 */         Long2ObjectMap.Entry<CompletableFuture<Optional<PackedChunk<P>>>> entry = (Long2ObjectMap.Entry)objectIterator.next();
/*  92 */         Optional<PackedChunk<P>> chunk = (Optional)((CompletableFuture)entry.getValue()).getNow(null);
/*  93 */         if (chunk == null) {
/*     */           continue;
/*     */         }
/*  96 */         long chunkKey = entry.getLongKey();
/*  97 */         unpackChunk(new ChunkPos(chunkKey), (PackedChunk)chunk.orElse(null));
/*  98 */         objectIterator.remove();
/*  99 */         this.loadedChunks.add(chunkKey);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flushAll() {
/* 105 */     if (!this.dirtyChunks.isEmpty()) {
/* 106 */       this.dirtyChunks.forEach(pos -> writeChunk(new ChunkPos(pos)));
/* 107 */       this.dirtyChunks.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 112 */   public boolean hasWork() { return !this.dirtyChunks.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   protected Optional<R> get(long sectionPos) { return (Optional)this.storage.get(sectionPos); }
/*     */ 
/*     */   
/*     */   protected Optional<R> getOrLoad(long sectionPos) {
/* 120 */     if (outsideStoredRange(sectionPos)) {
/* 121 */       return Optional.empty();
/*     */     }
/* 123 */     Optional<R> r = get(sectionPos);
/* 124 */     if (r != null) {
/* 125 */       return r;
/*     */     }
/* 127 */     unpackChunk(SectionPos.of(sectionPos).chunk());
/*     */     
/* 129 */     r = get(sectionPos);
/* 130 */     if (r == null) {
/* 131 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException());
/*     */     }
/* 133 */     return r;
/*     */   }
/*     */   
/*     */   protected boolean outsideStoredRange(long sectionPos) {
/* 137 */     int y = SectionPos.sectionToBlockCoord(SectionPos.y(sectionPos));
/* 138 */     return this.levelHeightAccessor.isOutsideBuildHeight(y);
/*     */   }
/*     */   
/*     */   protected R getOrCreate(long sectionPos) {
/* 142 */     if (outsideStoredRange(sectionPos)) {
/* 143 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("sectionPos out of bounds"));
/*     */     }
/* 145 */     Optional<R> r = getOrLoad(sectionPos);
/* 146 */     if (r.isPresent()) {
/* 147 */       return (R)r.get();
/*     */     }
/* 149 */     R newR = (R)this.factory.apply(() -> setDirty(sectionPos));
/* 150 */     this.storage.put(sectionPos, Optional.of(newR));
/* 151 */     return newR;
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> prefetch(ChunkPos chunkPos) {
/* 155 */     synchronized (this.loadLock) {
/* 156 */       long chunkKey = chunkPos.toLong();
/* 157 */       if (this.loadedChunks.contains(chunkKey)) {
/* 158 */         return CompletableFuture.completedFuture(null);
/*     */       }
/* 160 */       return (CompletableFuture)this.pendingLoads.computeIfAbsent(chunkKey, k -> tryRead(chunkPos));
/*     */     } 
/*     */   }
/*     */   private void unpackChunk(ChunkPos chunkPos) {
/*     */     CompletableFuture<Optional<PackedChunk<P>>> future;
/* 165 */     long chunkKey = chunkPos.toLong();
/*     */     
/* 167 */     synchronized (this.loadLock) {
/* 168 */       if (!this.loadedChunks.add(chunkKey)) {
/*     */         return;
/*     */       }
/* 171 */       future = (CompletableFuture)this.pendingLoads.computeIfAbsent(chunkKey, k -> tryRead(chunkPos));
/*     */     } 
/* 173 */     unpackChunk(chunkPos, (PackedChunk)((Optional)future.join()).orElse(null));
/*     */     
/* 175 */     synchronized (this.loadLock) {
/* 176 */       this.pendingLoads.remove(chunkKey);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Optional<PackedChunk<P>>> tryRead(ChunkPos chunkPos) {
/* 181 */     RegistryOps<Tag> registryOps = this.registryAccess.createSerializationContext(NbtOps.INSTANCE);
/* 182 */     return this.simpleRegionStorage.read(chunkPos)
/* 183 */       .thenApplyAsync(result -> result.map(()), 
/*     */         
/* 185 */         Util.backgroundExecutor().forName("parseSection"))
/* 186 */       .exceptionally(throwable -> {
/* 187 */           if (throwable instanceof CompletionException) {
/* 188 */             throwable = throwable.getCause();
/*     */           }
/* 190 */           if (throwable instanceof IOException) { IOException e = (IOException)throwable;
/* 191 */             LOGGER.error("Error reading chunk {} data from disk", chunkPos, e);
/* 192 */             this.errorReporter.reportChunkLoadFailure(e, this.simpleRegionStorage.storageInfo(), chunkPos);
/* 193 */             return Optional.empty(); }
/*     */           
/* 195 */           throw new CompletionException(throwable);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void unpackChunk(ChunkPos pos, PackedChunk<P> packedChunk) {
/* 201 */     if (packedChunk == null) {
/* 202 */       for (int sectionY = this.levelHeightAccessor.getMinSectionY(); sectionY <= this.levelHeightAccessor.getMaxSectionY(); sectionY++) {
/* 203 */         this.storage.put(getKey(pos, sectionY), Optional.empty());
/*     */       }
/*     */     } else {
/* 206 */       boolean versionChanged = packedChunk.versionChanged();
/* 207 */       for (int sectionY = this.levelHeightAccessor.getMinSectionY(); sectionY <= this.levelHeightAccessor.getMaxSectionY(); sectionY++) {
/* 208 */         long key = getKey(pos, sectionY);
/*     */         
/* 210 */         Optional<R> section = Optional.ofNullable(packedChunk.sectionsByY.get(sectionY)).map(packed -> this.unpacker.apply(packed, ()));
/* 211 */         this.storage.put(key, section);
/* 212 */         section.ifPresent(s -> {
/* 213 */               onSectionLoad(key);
/* 214 */               if (versionChanged) {
/* 215 */                 setDirty(key);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeChunk(ChunkPos chunkPos) {
/* 223 */     RegistryOps<Tag> registryOps = this.registryAccess.createSerializationContext(NbtOps.INSTANCE);
/* 224 */     Dynamic<Tag> tag = writeChunk(chunkPos, registryOps);
/* 225 */     Tag value = (Tag)tag.getValue();
/* 226 */     if (value instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)value;
/* 227 */       this.simpleRegionStorage.write(chunkPos, compoundTag).exceptionally(throwable -> {
/* 228 */             this.errorReporter.reportChunkSaveFailure(throwable, this.simpleRegionStorage.storageInfo(), chunkPos);
/* 229 */             return null;
/*     */           }); }
/*     */     else
/* 232 */     { LOGGER.error("Expected compound tag, got {}", value); }
/*     */   
/*     */   }
/*     */   
/*     */   private <T> Dynamic<T> writeChunk(ChunkPos chunkPos, DynamicOps<T> ops) {
/* 237 */     Map<T, T> sections = Maps.newHashMap();
/* 238 */     for (int sectionY = this.levelHeightAccessor.getMinSectionY(); sectionY <= this.levelHeightAccessor.getMaxSectionY(); sectionY++) {
/* 239 */       long key = getKey(chunkPos, sectionY);
/* 240 */       Optional<R> r = (Optional)this.storage.get(key);
/* 241 */       if (r != null && !r.isEmpty()) {
/*     */ 
/*     */         
/* 244 */         DataResult<T> serializedSection = this.codec.encodeStart(ops, this.packer.apply(r.get()));
/* 245 */         String yName = Integer.toString(sectionY);
/* 246 */         Objects.requireNonNull(LOGGER); serializedSection.resultOrPartial(LOGGER::error).ifPresent(s -> sections.put(ops.createString(yName), s));
/*     */       } 
/*     */     } 
/* 249 */     return new Dynamic(ops, ops.createMap(ImmutableMap.of(ops
/* 250 */             .createString("Sections"), ops.createMap(sections), ops
/* 251 */             .createString("DataVersion"), ops.createInt(SharedConstants.getCurrentVersion().dataVersion().version()))));
/*     */   }
/*     */ 
/*     */   
/* 255 */   private static long getKey(ChunkPos chunkPos, int sectionY) { return SectionPos.asLong(chunkPos.x, sectionY, chunkPos.z); }
/*     */ 
/*     */   
/*     */   protected void onSectionLoad(long sectionPos) {}
/*     */ 
/*     */   
/*     */   protected void setDirty(long sectionPos) {
/* 262 */     Optional<R> r = (Optional)this.storage.get(sectionPos);
/* 263 */     if (r == null || r.isEmpty()) {
/* 264 */       LOGGER.warn("No data for position: {}", SectionPos.of(sectionPos));
/*     */       return;
/*     */     } 
/* 267 */     this.dirtyChunks.add(ChunkPos.asLong(SectionPos.x(sectionPos), SectionPos.z(sectionPos)));
/*     */   }
/*     */   
/*     */   public void flush(ChunkPos chunkPos) {
/* 271 */     if (this.dirtyChunks.remove(chunkPos.toLong())) {
/* 272 */       writeChunk(chunkPos);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 278 */   public void close() { this.simpleRegionStorage.close(); }
/*     */   private static final class PackedChunk<T> extends Record { private final Int2ObjectMap<T> sectionsByY; private final boolean versionChanged;
/*     */     
/* 281 */     private PackedChunk(Int2ObjectMap<T> sectionsByY, boolean versionChanged) { this.sectionsByY = sectionsByY; this.versionChanged = versionChanged; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #281	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #281	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #281	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 281 */       //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/SectionStorage$PackedChunk<TT;>; } public Int2ObjectMap<T> sectionsByY() { return this.sectionsByY; } public boolean versionChanged() { return this.versionChanged; }
/*     */     public static <T> PackedChunk<T> parse(Codec<T> codec, DynamicOps<Tag> ops, Tag tag, SimpleRegionStorage simpleRegionStorage, LevelHeightAccessor levelHeightAccessor) {
/* 283 */       Dynamic<Tag> originalTag = new Dynamic<Tag>(ops, tag);
/*     */       
/* 285 */       Dynamic<Tag> fixedTag = simpleRegionStorage.upgradeChunkTag(originalTag, 1945);
/* 286 */       boolean versionChanged = (originalTag != fixedTag);
/* 287 */       OptionalDynamic<Tag> sections = fixedTag.get("Sections");
/* 288 */       Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 289 */       for (int sectionY = levelHeightAccessor.getMinSectionY(); sectionY <= levelHeightAccessor.getMaxSectionY(); sectionY++) {
/* 290 */         Optional<T> section = sections.get(Integer.toString(sectionY)).result().flatMap(sectionData -> {
/* 291 */               Objects.requireNonNull(SectionStorage.LOGGER); return codec.parse(sectionData).resultOrPartial(SectionStorage.LOGGER::error);
/*     */             });
/* 293 */         if (section.isPresent()) {
/* 294 */           int2ObjectOpenHashMap.put(sectionY, section.get());
/*     */         }
/*     */       } 
/* 297 */       return new PackedChunk(int2ObjectOpenHashMap, versionChanged);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\SectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */