/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.util.ExceptionCollector;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public final class RegionFileStorage implements AutoCloseable {
/*     */   public static final String ANVIL_EXTENSION = ".mca";
/*     */   private static final int MAX_CACHE_SIZE = 256;
/*     */   private final Long2ObjectLinkedOpenHashMap<RegionFile> regionCache;
/*     */   private final RegionStorageInfo info;
/*     */   private final Path folder;
/*     */   private final boolean sync;
/*     */   
/*     */   RegionFileStorage(RegionStorageInfo info, Path folder, boolean sync) {
/*  22 */     this.regionCache = new Long2ObjectLinkedOpenHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  28 */     this.folder = folder;
/*  29 */     this.sync = sync;
/*  30 */     this.info = info;
/*     */   }
/*     */   
/*     */   private RegionFile getRegionFile(ChunkPos pos) throws IOException {
/*  34 */     long key = ChunkPos.asLong(pos.getRegionX(), pos.getRegionZ());
/*  35 */     RegionFile region = (RegionFile)this.regionCache.getAndMoveToFirst(key);
/*  36 */     if (region != null) {
/*  37 */       return region;
/*     */     }
/*     */     
/*  40 */     if (this.regionCache.size() >= 256) {
/*  41 */       ((RegionFile)this.regionCache.removeLast()).close();
/*     */     }
/*     */     
/*  44 */     FileUtil.createDirectoriesSafe(this.folder);
/*     */     
/*  46 */     Path file = this.folder.resolve("r." + pos.getRegionX() + "." + pos.getRegionZ() + ".mca");
/*  47 */     RegionFile newRegion = new RegionFile(this.info, file, this.folder, this.sync);
/*  48 */     this.regionCache.putAndMoveToFirst(key, newRegion);
/*  49 */     return newRegion;
/*     */   }
/*     */   
/*     */   public CompoundTag read(ChunkPos pos) throws IOException {
/*  53 */     RegionFile region = getRegionFile(pos);
/*  54 */     DataInputStream regionChunkInputStream = region.getChunkDataInputStream(pos); 
/*  55 */     try { if (regionChunkInputStream == null)
/*  56 */       { CompoundTag compoundTag1 = null;
/*     */ 
/*     */ 
/*     */         
/*  60 */         if (regionChunkInputStream != null) regionChunkInputStream.close();  return compoundTag1; }  CompoundTag compoundTag = NbtIo.read(regionChunkInputStream); if (regionChunkInputStream != null) regionChunkInputStream.close();  return compoundTag; } catch (Throwable throwable) { if (regionChunkInputStream != null)
/*     */         try { regionChunkInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  64 */      } public void scanChunk(ChunkPos pos, StreamTagVisitor scanner) throws IOException { RegionFile region = getRegionFile(pos);
/*  65 */     DataInputStream regionChunkInputStream = region.getChunkDataInputStream(pos); 
/*  66 */     try { if (regionChunkInputStream != null) {
/*  67 */         NbtIo.parse(regionChunkInputStream, scanner, NbtAccounter.unlimitedHeap());
/*     */       }
/*  69 */       if (regionChunkInputStream != null) regionChunkInputStream.close();  } catch (Throwable throwable) { if (regionChunkInputStream != null)
/*     */         try { regionChunkInputStream.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  73 */      } protected void write(ChunkPos pos, CompoundTag value) throws IOException { if (SharedConstants.DEBUG_DONT_SAVE_WORLD) {
/*     */       return;
/*     */     }
/*  76 */     RegionFile region = getRegionFile(pos);
/*  77 */     if (value == null) {
/*  78 */       region.clear(pos);
/*     */     } else {
/*  80 */       DataOutputStream output = region.getChunkDataOutputStream(pos); 
/*  81 */       try { NbtIo.write(value, output);
/*  82 */         if (output != null) output.close();  }
/*     */       catch (Throwable throwable) { if (output != null)
/*     */           try { output.close(); }
/*     */           catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */             throw throwable; }
/*     */     
/*  88 */     }  } public void close() throws IOException { ExceptionCollector<IOException> exception = new ExceptionCollector<IOException>();
/*  89 */     for (ObjectIterator objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile regionFile = (RegionFile)objectIterator.next();
/*     */       try {
/*  91 */         regionFile.close();
/*  92 */       } catch (IOException e) {
/*  93 */         exception.add(e);
/*     */       }  }
/*     */     
/*  96 */     exception.throwIfPresent(); }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 100 */     for (ObjectIterator objectIterator = this.regionCache.values().iterator(); objectIterator.hasNext(); ) { RegionFile regionFile = (RegionFile)objectIterator.next();
/* 101 */       regionFile.flush(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 106 */   public RegionStorageInfo info() { return this.info; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionFileStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */