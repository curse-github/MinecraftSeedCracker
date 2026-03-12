/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.DirectoryLock;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LevelStorageAccess
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final DirectoryLock lock;
/*     */   private final LevelStorageSource.LevelDirectory levelDirectory;
/*     */   private final String levelId;
/*     */   private final Map<LevelResource, Path> resources;
/*     */   
/*     */   private LevelStorageAccess(String levelId, Path path) throws IOException {
/* 364 */     this.resources = Maps.newHashMap();
/*     */ 
/*     */     
/* 367 */     this.levelId = levelId;
/* 368 */     this.levelDirectory = new LevelStorageSource.LevelDirectory(path);
/* 369 */     this.lock = DirectoryLock.create(path);
/*     */   }
/*     */   
/*     */   public long estimateDiskSpace() {
/*     */     try {
/* 374 */       return Files.getFileStore(this.levelDirectory.path).getUsableSpace();
/* 375 */     } catch (Exception ignored) {
/*     */       
/* 377 */       return Float.MAX_VALUE;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 382 */   public boolean checkForLowDiskSpace() { return (estimateDiskSpace() < 67108864L); }
/*     */ 
/*     */   
/*     */   public void safeClose() {
/*     */     try {
/* 387 */       close();
/* 388 */     } catch (IOException e) {
/* 389 */       LevelStorageSource.LOGGER.warn("Failed to unlock access to level {}", getLevelId(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 394 */   public LevelStorageSource parent() { return LevelStorageSource.this; }
/*     */ 
/*     */ 
/*     */   
/* 398 */   public LevelStorageSource.LevelDirectory getLevelDirectory() { return this.levelDirectory; }
/*     */ 
/*     */ 
/*     */   
/* 402 */   public String getLevelId() { return this.levelId; }
/*     */ 
/*     */ 
/*     */   
/* 406 */   public Path getLevelPath(LevelResource resource) { Objects.requireNonNull(this.levelDirectory); return (Path)this.resources.computeIfAbsent(resource, this.levelDirectory::resourcePath); }
/*     */ 
/*     */ 
/*     */   
/* 410 */   public Path getDimensionPath(ResourceKey<Level> name) { return DimensionType.getStorageFolder(name, this.levelDirectory.path()); }
/*     */ 
/*     */   
/*     */   private void checkLock() {
/* 414 */     if (!this.lock.isValid()) {
/* 415 */       throw new IllegalStateException("Lock is no longer valid");
/*     */     }
/*     */   }
/*     */   
/*     */   public PlayerDataStorage createPlayerStorage() {
/* 420 */     checkLock();
/* 421 */     return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
/*     */   }
/*     */   
/*     */   public LevelSummary getSummary(Dynamic<?> dataTag) {
/* 425 */     checkLock();
/* 426 */     return LevelStorageSource.this.makeLevelSummary(dataTag, this.levelDirectory, false);
/*     */   }
/*     */ 
/*     */   
/* 430 */   public Dynamic<?> getDataTag() throws IOException { return getDataTag(false); }
/*     */ 
/*     */ 
/*     */   
/* 434 */   public Dynamic<?> getDataTagFallback() throws IOException { return getDataTag(true); }
/*     */ 
/*     */   
/*     */   private Dynamic<?> getDataTag(boolean useFallback) throws IOException {
/* 438 */     checkLock();
/* 439 */     return LevelStorageSource.readLevelDataTagFixed(useFallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile(), LevelStorageSource.this.fixerUpper);
/*     */   }
/*     */ 
/*     */   
/* 443 */   public void saveDataTag(RegistryAccess registryAccess, WorldData levelData) { saveDataTag(registryAccess, levelData, null); }
/*     */ 
/*     */   
/*     */   public void saveDataTag(RegistryAccess registryAccess, WorldData levelData, CompoundTag playerData) {
/* 447 */     CompoundTag dataTag = levelData.createTag(registryAccess, playerData);
/*     */     
/* 449 */     CompoundTag root = new CompoundTag();
/* 450 */     root.put("Data", dataTag);
/*     */     
/* 452 */     saveLevelData(root);
/*     */   }
/*     */   
/*     */   private void saveLevelData(CompoundTag root) {
/* 456 */     Path worldDir = this.levelDirectory.path();
/*     */     try {
/* 458 */       Path dataFile = Files.createTempFile(worldDir, "level", ".dat", new java.nio.file.attribute.FileAttribute[0]);
/* 459 */       NbtIo.writeCompressed(root, dataFile);
/*     */       
/* 461 */       Path oldDataFile = this.levelDirectory.oldDataFile();
/* 462 */       Path currentFile = this.levelDirectory.dataFile();
/* 463 */       Util.safeReplaceFile(currentFile, dataFile, oldDataFile);
/* 464 */     } catch (Exception e) {
/* 465 */       LevelStorageSource.LOGGER.error("Failed to save level {}", worldDir, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<Path> getIconFile() {
/* 470 */     if (!this.lock.isValid()) {
/* 471 */       return Optional.empty();
/*     */     }
/* 473 */     return Optional.of(this.levelDirectory.iconFile());
/*     */   }
/*     */   
/*     */   public void deleteLevel() {
/* 477 */     checkLock();
/*     */     
/* 479 */     final Path lockPath = this.levelDirectory.lockFile();
/*     */     
/* 481 */     LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);
/* 482 */     for (int attempt = 1; attempt <= 5; attempt++) {
/* 483 */       LevelStorageSource.LOGGER.info("Attempt {}...", Integer.valueOf(attempt));
/*     */       
/*     */       try {
/* 486 */         Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>()
/*     */             {
/*     */               public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 489 */                 if (!file.equals(lockPath)) {
/* 490 */                   LevelStorageSource.LOGGER.debug("Deleting {}", file);
/* 491 */                   Files.delete(file);
/*     */                 } 
/* 493 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */ 
/*     */               
/*     */               public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 498 */                 if (exc != null) {
/* 499 */                   throw exc;
/*     */                 }
/*     */                 
/* 502 */                 if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/*     */                   
/* 504 */                   LevelStorageSource.LevelStorageAccess.this.lock.close();
/* 505 */                   Files.deleteIfExists(lockPath);
/*     */                 } 
/* 507 */                 Files.delete(dir);
/* 508 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */             });
/*     */         break;
/* 512 */       } catch (IOException e) {
/* 513 */         if (attempt < 5) {
/* 514 */           LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), e);
/*     */           try {
/* 516 */             Thread.sleep(500L);
/* 517 */           } catch (InterruptedException interruptedException) {}
/*     */         } else {
/*     */           
/* 520 */           throw e;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 527 */   public void renameLevel(String newName) throws IOException { modifyLevelDataWithoutDatafix(tag -> tag.putString("LevelName", newName.trim())); }
/*     */ 
/*     */   
/*     */   public void renameAndDropPlayer(String newName) throws IOException {
/* 531 */     modifyLevelDataWithoutDatafix(tag -> {
/* 532 */           tag.putString("LevelName", newName.trim());
/* 533 */           tag.remove("Player");
/*     */         });
/*     */   }
/*     */   
/*     */   private void modifyLevelDataWithoutDatafix(Consumer<CompoundTag> updater) throws IOException {
/* 538 */     checkLock();
/*     */     
/* 540 */     CompoundTag root = LevelStorageSource.readLevelDataTagRaw(this.levelDirectory.dataFile());
/* 541 */     updater.accept(root.getCompoundOrEmpty("Data"));
/* 542 */     saveLevelData(root);
/*     */   }
/*     */   
/*     */   public long makeWorldBackup() {
/* 546 */     checkLock();
/* 547 */     String zipFilePrefix = FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now()) + "_" + FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now());
/*     */     
/* 549 */     Path root = LevelStorageSource.this.getBackupPath();
/*     */     try {
/* 551 */       FileUtil.createDirectoriesSafe(root);
/* 552 */     } catch (IOException e) {
/* 553 */       throw new RuntimeException(e);
/*     */     } 
/* 555 */     Path zipFilePath = root.resolve(FileUtil.findAvailableName(root, zipFilePrefix, ".zip"));
/*     */     
/* 557 */     final ZipOutputStream stream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath, new java.nio.file.OpenOption[0]))); 
/* 558 */     try { final Path rootPath = Paths.get(this.levelId, new String[0]);
/*     */       
/* 560 */       Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>()
/*     */           {
/*     */             public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 563 */               if (path.endsWith("session.lock")) {
/* 564 */                 return FileVisitResult.CONTINUE;
/*     */               }
/* 566 */               String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/* 567 */               ZipEntry entry = new ZipEntry(entryPath);
/* 568 */               stream.putNextEntry(entry);
/* 569 */               Files.asByteSource(path.toFile()).copyTo(stream);
/* 570 */               stream.closeEntry();
/* 571 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/* 574 */       stream.close(); } catch (Throwable throwable) { try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 576 */      return Files.size(zipFilePath);
/*     */   }
/*     */ 
/*     */   
/* 580 */   public boolean hasWorldData() { return (Files.exists(this.levelDirectory.dataFile(), new java.nio.file.LinkOption[0]) || Files.exists(this.levelDirectory.oldDataFile(), new java.nio.file.LinkOption[0])); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 585 */   public void close() { this.lock.close(); }
/*     */ 
/*     */ 
/*     */   
/* 589 */   public boolean restoreLevelDataFromOld() { return Util.safeReplaceOrMoveFile(this.levelDirectory.dataFile(), this.levelDirectory.oldDataFile(), this.levelDirectory.corruptedDataFile(ZonedDateTime.now()), true); }
/*     */ 
/*     */ 
/*     */   
/* 593 */   public Instant getFileModificationTime(boolean fallback) { return LevelStorageSource.getFileModificationTime(fallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelStorageAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */