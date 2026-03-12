/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ 
/*     */ public class SimpleRegionStorage
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final IOWorker worker;
/*     */   private final DataFixer fixerUpper;
/*     */   private final DataFixTypes dataFixType;
/*     */   private final Supplier<LegacyTagFixer> legacyFixer;
/*     */   
/*  31 */   public SimpleRegionStorage(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType) { this(info, folder, fixerUpper, syncWrites, dataFixType, LegacyTagFixer.EMPTY); }
/*     */ 
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType, Supplier<LegacyTagFixer> legacyFixer) {
/*  35 */     this.fixerUpper = fixerUpper;
/*  36 */     this.dataFixType = dataFixType;
/*  37 */     this.worker = new IOWorker(info, folder, syncWrites);
/*  38 */     Objects.requireNonNull(legacyFixer); this.legacyFixer = Suppliers.memoize(legacyFixer::get);
/*     */   }
/*     */ 
/*     */   
/*  42 */   public boolean isOldChunkAround(ChunkPos pos, int range) { return this.worker.isOldChunkAround(pos, range); }
/*     */ 
/*     */ 
/*     */   
/*  46 */   public CompletableFuture<Optional<CompoundTag>> read(ChunkPos pos) { return this.worker.loadAsync(pos); }
/*     */ 
/*     */ 
/*     */   
/*  50 */   public CompletableFuture<Void> write(ChunkPos pos, CompoundTag value) { return write(pos, () -> value); }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
/*  54 */     markChunkDone(pos);
/*  55 */     return this.worker.store(pos, supplier);
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion, CompoundTag dataFixContextTag) {
/*  59 */     int version = NbtUtils.getDataVersion(chunkTag, defaultVersion);
/*  60 */     if (version == SharedConstants.getCurrentVersion().dataVersion().version()) {
/*  61 */       return chunkTag;
/*     */     }
/*     */     
/*     */     try {
/*  65 */       chunkTag = ((LegacyTagFixer)this.legacyFixer.get()).applyFix(chunkTag);
/*     */ 
/*     */       
/*  68 */       injectDatafixingContext(chunkTag, dataFixContextTag);
/*  69 */       chunkTag = this.dataFixType.updateToCurrentVersion(this.fixerUpper, chunkTag, Math.max(((LegacyTagFixer)this.legacyFixer.get()).targetDataVersion(), version));
/*  70 */       removeDatafixingContext(chunkTag);
/*     */ 
/*     */       
/*  73 */       NbtUtils.addCurrentDataVersion(chunkTag);
/*     */       
/*  75 */       return chunkTag;
/*  76 */     } catch (Exception e) {
/*  77 */       CrashReport report = CrashReport.forThrowable(e, "Updated chunk");
/*  78 */       CrashReportCategory details = report.addCategory("Updated chunk details");
/*  79 */       details.setDetail("Data version", Integer.valueOf(version));
/*  80 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  85 */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion) { return upgradeChunkTag(chunkTag, defaultVersion, null); }
/*     */ 
/*     */ 
/*     */   
/*  89 */   public Dynamic<Tag> upgradeChunkTag(Dynamic<Tag> chunkTag, int defaultVersion) { return new Dynamic(chunkTag.getOps(), upgradeChunkTag((CompoundTag)chunkTag.getValue(), defaultVersion, null)); }
/*     */ 
/*     */   
/*     */   public static void injectDatafixingContext(CompoundTag chunkTag, CompoundTag contextTag) {
/*  93 */     if (contextTag != null) {
/*  94 */       chunkTag.put("__context", contextTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  99 */   private static void removeDatafixingContext(CompoundTag chunkTag) { chunkTag.remove("__context"); }
/*     */ 
/*     */ 
/*     */   
/* 103 */   protected void markChunkDone(ChunkPos pos) { ((LegacyTagFixer)this.legacyFixer.get()).markChunkDone(pos); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public CompletableFuture<Void> synchronize(boolean flush) { return this.worker.synchronize(flush); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   public void close() throws IOException { this.worker.close(); }
/*     */ 
/*     */ 
/*     */   
/* 116 */   public ChunkScanAccess chunkScanner() { return this.worker; }
/*     */ 
/*     */ 
/*     */   
/* 120 */   public RegionStorageInfo storageInfo() { return this.worker.storageInfo(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\SimpleRegionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */