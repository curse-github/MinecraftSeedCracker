/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.regex.Matcher;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractUpgrader
/*     */ {
/*     */   private final Component upgradingStatus;
/*     */   private final Component finishedStatus;
/*     */   private final String type;
/*     */   private final String folderName;
/*     */   protected CompletableFuture<Void> previousWriteFuture;
/*     */   protected final DataFixTypes dataFixType;
/*     */   
/*     */   private AbstractUpgrader(DataFixTypes dataFixType, String type, String folderName, Component upgradingStatus, Component finishedStatus) {
/* 187 */     this.dataFixType = dataFixType;
/* 188 */     this.type = type;
/* 189 */     this.folderName = folderName;
/* 190 */     this.upgradingStatus = upgradingStatus;
/* 191 */     this.finishedStatus = finishedStatus;
/*     */   }
/*     */   
/*     */   public void upgrade() {
/* 195 */     WorldUpgrader.this.totalFiles = 0;
/* 196 */     WorldUpgrader.this.totalChunks = 0;
/* 197 */     WorldUpgrader.this.converted = 0;
/* 198 */     WorldUpgrader.this.skipped = 0;
/* 199 */     List<WorldUpgrader.DimensionToUpgrade> dimensionsToUpgrade = getDimensionsToUpgrade();
/* 200 */     if (WorldUpgrader.this.totalChunks == 0) {
/*     */       return;
/*     */     }
/* 203 */     float totalSize = WorldUpgrader.this.totalFiles;
/*     */     
/* 205 */     WorldUpgrader.this.status = this.upgradingStatus;
/*     */     
/* 207 */     while (WorldUpgrader.this.running) {
/* 208 */       boolean worked = false;
/*     */       
/* 210 */       float totalProgress = 0.0F;
/* 211 */       for (WorldUpgrader.DimensionToUpgrade dimensionToUpgrade : dimensionsToUpgrade) {
/* 212 */         ResourceKey<Level> dimensionKey = dimensionToUpgrade.dimensionKey;
/* 213 */         ListIterator<WorldUpgrader.FileToUpgrade> iterator = dimensionToUpgrade.files;
/* 214 */         SimpleRegionStorage storage = dimensionToUpgrade.storage;
/* 215 */         if (iterator.hasNext()) {
/* 216 */           WorldUpgrader.FileToUpgrade fileToUpgrade = (WorldUpgrader.FileToUpgrade)iterator.next();
/* 217 */           boolean converted = true;
/* 218 */           for (ChunkPos chunkPos : fileToUpgrade.chunksToUpgrade) {
/* 219 */             converted = (converted && processOnePosition(dimensionKey, storage, chunkPos));
/* 220 */             worked = true;
/*     */           } 
/* 222 */           if (WorldUpgrader.this.recreateRegionFiles) {
/* 223 */             if (converted) {
/* 224 */               onFileFinished(fileToUpgrade.file);
/*     */             } else {
/* 226 */               WorldUpgrader.LOGGER.error("Failed to convert region file {}", fileToUpgrade.file.getPath());
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 231 */         float currentProgress = iterator.nextIndex() / totalSize;
/* 232 */         WorldUpgrader.this.progressMap.put(dimensionKey, currentProgress);
/* 233 */         totalProgress += currentProgress;
/*     */       } 
/*     */       
/* 236 */       WorldUpgrader.this.progress = totalProgress;
/*     */       
/* 238 */       if (!worked) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 243 */     WorldUpgrader.this.status = this.finishedStatus;
/*     */     
/* 245 */     for (WorldUpgrader.DimensionToUpgrade dimensionToUpgrade : dimensionsToUpgrade) {
/*     */       try {
/* 247 */         dimensionToUpgrade.storage.close();
/* 248 */       } catch (Exception e) {
/* 249 */         WorldUpgrader.LOGGER.error("Error upgrading chunk", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<WorldUpgrader.DimensionToUpgrade> getDimensionsToUpgrade() {
/* 255 */     List<WorldUpgrader.DimensionToUpgrade> dimensionsToUpgrade = Lists.newArrayList();
/* 256 */     for (ResourceKey<Level> dimensionKey : WorldUpgrader.this.levels) {
/* 257 */       RegionStorageInfo info = new RegionStorageInfo(WorldUpgrader.this.levelStorage.getLevelId(), dimensionKey, this.type);
/* 258 */       Path regionFolder = WorldUpgrader.this.levelStorage.getDimensionPath(dimensionKey).resolve(this.folderName);
/* 259 */       SimpleRegionStorage storage = createStorage(info, regionFolder);
/* 260 */       ListIterator<WorldUpgrader.FileToUpgrade> files = getFilesToProcess(info, regionFolder);
/* 261 */       dimensionsToUpgrade.add(new WorldUpgrader.DimensionToUpgrade(dimensionKey, storage, files));
/*     */     } 
/* 263 */     return dimensionsToUpgrade;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ListIterator<WorldUpgrader.FileToUpgrade> getFilesToProcess(RegionStorageInfo info, Path regionFolder) {
/* 269 */     List<WorldUpgrader.FileToUpgrade> filesToUpgrade = getAllChunkPositions(info, regionFolder);
/* 270 */     WorldUpgrader.this.totalFiles += filesToUpgrade.size();
/* 271 */     WorldUpgrader.this.totalChunks += filesToUpgrade.stream().mapToInt(fileToUpgrade -> fileToUpgrade.chunksToUpgrade.size()).sum();
/* 272 */     return filesToUpgrade.listIterator();
/*     */   }
/*     */   
/*     */   private static List<WorldUpgrader.FileToUpgrade> getAllChunkPositions(RegionStorageInfo info, Path regionFolder) {
/* 276 */     File[] files = regionFolder.toFile().listFiles((dir, name) -> name.endsWith(".mca"));
/*     */     
/* 278 */     if (files == null) {
/* 279 */       return List.of();
/*     */     }
/*     */     
/* 282 */     List<WorldUpgrader.FileToUpgrade> regionFileChunks = Lists.newArrayList();
/* 283 */     for (File regionFile : files) {
/* 284 */       Matcher regex = WorldUpgrader.REGEX.matcher(regionFile.getName());
/* 285 */       if (regex.matches()) {
/*     */ 
/*     */ 
/*     */         
/* 289 */         int xOffset = Integer.parseInt(regex.group(1)) << 5;
/* 290 */         int zOffset = Integer.parseInt(regex.group(2)) << 5;
/*     */         
/* 292 */         List<ChunkPos> chunkPositions = Lists.newArrayList(); 
/* 293 */         try { RegionFile regionSource = new RegionFile(info, regionFile.toPath(), regionFolder, true); 
/* 294 */           try { for (int x = 0; x < 32; x++) {
/* 295 */               for (int z = 0; z < 32; z++) {
/* 296 */                 ChunkPos pos = new ChunkPos(x + xOffset, z + zOffset);
/* 297 */                 if (regionSource.doesChunkExist(pos)) {
/* 298 */                   chunkPositions.add(pos);
/*     */                 }
/*     */               } 
/*     */             } 
/* 302 */             if (!chunkPositions.isEmpty()) {
/* 303 */               regionFileChunks.add(new WorldUpgrader.FileToUpgrade(regionSource, chunkPositions));
/*     */             }
/* 305 */             regionSource.close(); } catch (Throwable throwable) { try { regionSource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable t)
/* 306 */         { WorldUpgrader.LOGGER.error("Failed to read chunks from region file {}", regionFile.toPath(), t); }
/*     */       
/*     */       } 
/* 309 */     }  return regionFileChunks;
/*     */   }
/*     */   
/*     */   private boolean processOnePosition(ResourceKey<Level> dimension, SimpleRegionStorage storage, ChunkPos pos) {
/* 313 */     boolean converted = false;
/*     */     try {
/* 315 */       converted = tryProcessOnePosition(storage, pos, dimension);
/* 316 */     } catch (ReportedException|java.util.concurrent.CompletionException e) {
/* 317 */       Throwable cause = e.getCause();
/* 318 */       if (cause instanceof IOException) {
/* 319 */         WorldUpgrader.LOGGER.error("Error upgrading chunk {}", pos, cause);
/*     */       } else {
/* 321 */         throw e;
/*     */       } 
/*     */     } 
/* 324 */     if (converted) {
/* 325 */       WorldUpgrader.this.converted++;
/*     */     } else {
/* 327 */       WorldUpgrader.this.skipped++;
/*     */     } 
/* 329 */     return converted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void onFileFinished(RegionFile regionFile) {
/* 335 */     if (!WorldUpgrader.this.recreateRegionFiles) {
/*     */       return;
/*     */     }
/* 338 */     if (this.previousWriteFuture != null) {
/* 339 */       this.previousWriteFuture.join();
/*     */     }
/* 341 */     Path filePath = regionFile.getPath();
/* 342 */     Path directoryPath = filePath.getParent();
/* 343 */     Path newFilePath = WorldUpgrader.resolveRecreateDirectory(directoryPath).resolve(filePath.getFileName().toString());
/*     */     
/*     */     try {
/* 346 */       if (newFilePath.toFile().exists()) {
/* 347 */         Files.delete(filePath);
/* 348 */         Files.move(newFilePath, filePath, new java.nio.file.CopyOption[0]);
/*     */       } else {
/* 350 */         WorldUpgrader.LOGGER.error("Failed to replace an old region file. New file {} does not exist.", newFilePath);
/*     */       } 
/* 352 */     } catch (IOException e) {
/* 353 */       WorldUpgrader.LOGGER.error("Failed to replace an old region file", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract SimpleRegionStorage createStorage(RegionStorageInfo paramRegionStorageInfo, Path paramPath);
/*     */   
/*     */   protected abstract boolean tryProcessOnePosition(SimpleRegionStorage paramSimpleRegionStorage, ChunkPos paramChunkPos, ResourceKey<Level> paramResourceKey);
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\worldupdate\WorldUpgrader$AbstractUpgrader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */