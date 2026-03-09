/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.storage.LegacyTagFixer;
/*     */ import net.minecraft.world.level.chunk.storage.RecreatingSimpleRegionStorage;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.structure.LegacyStructureDataHandler;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldUpgrader
/*     */   implements AutoCloseable
/*     */ {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  59 */   private static final ThreadFactory THREAD_FACTORY = (new ThreadFactoryBuilder()).setDaemon(true).build();
/*     */   private static final String NEW_DIRECTORY_PREFIX = "new_";
/*  61 */   private static final Component STATUS_UPGRADING_POI = Component.translatable("optimizeWorld.stage.upgrading.poi");
/*  62 */   private static final Component STATUS_FINISHED_POI = Component.translatable("optimizeWorld.stage.finished.poi");
/*  63 */   private static final Component STATUS_UPGRADING_ENTITIES = Component.translatable("optimizeWorld.stage.upgrading.entities");
/*  64 */   private static final Component STATUS_FINISHED_ENTITIES = Component.translatable("optimizeWorld.stage.finished.entities");
/*  65 */   private static final Component STATUS_UPGRADING_CHUNKS = Component.translatable("optimizeWorld.stage.upgrading.chunks");
/*  66 */   private static final Component STATUS_FINISHED_CHUNKS = Component.translatable("optimizeWorld.stage.finished.chunks");
/*     */ 
/*     */   
/*     */   private final Registry<LevelStem> dimensions;
/*     */ 
/*     */   
/*     */   private final Set<ResourceKey<Level>> levels;
/*     */ 
/*     */   
/*     */   private final boolean eraseCache;
/*     */   
/*     */   private final boolean recreateRegionFiles;
/*     */   
/*     */   private final LevelStorageSource.LevelStorageAccess levelStorage;
/*     */   
/*     */   private final Thread thread;
/*     */   
/*     */   private final DataFixer dataFixer;
/*     */   
/*     */   private final Reference2FloatMap<ResourceKey<Level>> progressMap;
/*     */   
/*  87 */   private static final Pattern REGEX = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$"); private final DimensionDataStorage overworldDataStorage; public WorldUpgrader(LevelStorageSource.LevelStorageAccess levelSource, DataFixer dataFixer, WorldData worldData, RegistryAccess registryAccess, boolean eraseCache, boolean recreateRegionFiles) {
/*     */     this.running = true;
/*     */     this.progressMap = Reference2FloatMaps.synchronize(new Reference2FloatOpenHashMap());
/*     */     this.status = Component.translatable("optimizeWorld.stage.counting");
/*  91 */     this.dimensions = registryAccess.lookupOrThrow(Registries.LEVEL_STEM);
/*  92 */     this.levels = (Set)this.dimensions.registryKeySet().stream().map(Registries::levelStemToLevel).collect(Collectors.toUnmodifiableSet());
/*  93 */     this.eraseCache = eraseCache;
/*  94 */     this.dataFixer = dataFixer;
/*  95 */     this.levelStorage = levelSource;
/*  96 */     this.overworldDataStorage = new DimensionDataStorage(this.levelStorage.getDimensionPath(Level.OVERWORLD).resolve("data"), dataFixer, registryAccess);
/*  97 */     this.recreateRegionFiles = recreateRegionFiles;
/*     */     
/*  99 */     this.thread = THREAD_FACTORY.newThread(this::work);
/* 100 */     this.thread.setUncaughtExceptionHandler((t, e) -> {
/* 101 */           LOGGER.error("Error upgrading world", e);
/* 102 */           this.status = Component.translatable("optimizeWorld.stage.failed");
/* 103 */           this.finished = true;
/*     */         });
/* 105 */     this.thread.start();
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 109 */     this.running = false;
/*     */     try {
/* 111 */       this.thread.join();
/* 112 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   private void work() {
/* 117 */     long conversionTime = Util.getMillis();
/* 118 */     LOGGER.info("Upgrading entities");
/* 119 */     (new EntityUpgrader(this)).upgrade();
/* 120 */     LOGGER.info("Upgrading POIs");
/* 121 */     (new PoiUpgrader(this)).upgrade();
/* 122 */     LOGGER.info("Upgrading blocks");
/* 123 */     (new ChunkUpgrader()).upgrade();
/* 124 */     this.overworldDataStorage.saveAndJoin();
/* 125 */     conversionTime = Util.getMillis() - conversionTime;
/* 126 */     LOGGER.info("World optimizaton finished after {} seconds", Long.valueOf(conversionTime / 1000L));
/* 127 */     this.finished = true;
/*     */   }
/*     */ 
/*     */   
/* 131 */   public boolean isFinished() { return this.finished; }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public Set<ResourceKey<Level>> levels() { return this.levels; }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public float dimensionProgress(ResourceKey<Level> dimension) { return this.progressMap.getFloat(dimension); }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public float getProgress() { return this.progress; }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public int getTotalChunks() { return this.totalChunks; }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public int getConverted() { return this.converted; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public int getSkipped() { return this.skipped; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public Component getStatus() { return this.status; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public void close() { this.overworldDataStorage.close(); }
/*     */   static final class DimensionToUpgrade extends Record { private final ResourceKey<Level> dimensionKey; private final SimpleRegionStorage storage; private final ListIterator<WorldUpgrader.FileToUpgrade> files;
/*     */     
/* 167 */     DimensionToUpgrade(ResourceKey<Level> dimensionKey, SimpleRegionStorage storage, ListIterator<WorldUpgrader.FileToUpgrade> files) { this.dimensionKey = dimensionKey; this.storage = storage; this.files = files; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #167	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #167	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #167	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$DimensionToUpgrade;
/* 167 */       //   0	8	1	o	Ljava/lang/Object; } public ResourceKey<Level> dimensionKey() { return this.dimensionKey; } public SimpleRegionStorage storage() { return this.storage; } public ListIterator<WorldUpgrader.FileToUpgrade> files() { return this.files; } }
/*     */   
/*     */   static final class FileToUpgrade extends Record {
/*     */     private final RegionFile file;
/*     */     private final List<ChunkPos> chunksToUpgrade;
/*     */     
/* 173 */     FileToUpgrade(RegionFile file, List<ChunkPos> chunksToUpgrade) { this.file = file; this.chunksToUpgrade = chunksToUpgrade; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/worldupdate/WorldUpgrader$FileToUpgrade;
/* 173 */       //   0	8	1	o	Ljava/lang/Object; } public RegionFile file() { return this.file; } public List<ChunkPos> chunksToUpgrade() { return this.chunksToUpgrade; }
/*     */   }
/*     */ 
/*     */   
/*     */   private abstract class AbstractUpgrader
/*     */   {
/*     */     private final Component upgradingStatus;
/*     */     private final Component finishedStatus;
/*     */     private final String type;
/*     */     private final String folderName;
/*     */     protected CompletableFuture<Void> previousWriteFuture;
/*     */     protected final DataFixTypes dataFixType;
/*     */     
/*     */     private AbstractUpgrader(DataFixTypes dataFixType, String type, String folderName, Component upgradingStatus, Component finishedStatus) {
/* 187 */       this.dataFixType = dataFixType;
/* 188 */       this.type = type;
/* 189 */       this.folderName = folderName;
/* 190 */       this.upgradingStatus = upgradingStatus;
/* 191 */       this.finishedStatus = finishedStatus;
/*     */     }
/*     */     
/*     */     public void upgrade() {
/* 195 */       WorldUpgrader.this.totalFiles = 0;
/* 196 */       WorldUpgrader.this.totalChunks = 0;
/* 197 */       WorldUpgrader.this.converted = 0;
/* 198 */       WorldUpgrader.this.skipped = 0;
/* 199 */       List<WorldUpgrader.DimensionToUpgrade> dimensionsToUpgrade = getDimensionsToUpgrade();
/* 200 */       if (WorldUpgrader.this.totalChunks == 0) {
/*     */         return;
/*     */       }
/* 203 */       float totalSize = WorldUpgrader.this.totalFiles;
/*     */       
/* 205 */       WorldUpgrader.this.status = this.upgradingStatus;
/*     */       
/* 207 */       while (WorldUpgrader.this.running) {
/* 208 */         boolean worked = false;
/*     */         
/* 210 */         float totalProgress = 0.0F;
/* 211 */         for (WorldUpgrader.DimensionToUpgrade dimensionToUpgrade : dimensionsToUpgrade) {
/* 212 */           ResourceKey<Level> dimensionKey = dimensionToUpgrade.dimensionKey;
/* 213 */           ListIterator<WorldUpgrader.FileToUpgrade> iterator = dimensionToUpgrade.files;
/* 214 */           SimpleRegionStorage storage = dimensionToUpgrade.storage;
/* 215 */           if (iterator.hasNext()) {
/* 216 */             WorldUpgrader.FileToUpgrade fileToUpgrade = (WorldUpgrader.FileToUpgrade)iterator.next();
/* 217 */             boolean converted = true;
/* 218 */             for (ChunkPos chunkPos : fileToUpgrade.chunksToUpgrade) {
/* 219 */               converted = (converted && processOnePosition(dimensionKey, storage, chunkPos));
/* 220 */               worked = true;
/*     */             } 
/* 222 */             if (WorldUpgrader.this.recreateRegionFiles) {
/* 223 */               if (converted) {
/* 224 */                 onFileFinished(fileToUpgrade.file);
/*     */               } else {
/* 226 */                 WorldUpgrader.LOGGER.error("Failed to convert region file {}", fileToUpgrade.file.getPath());
/*     */               } 
/*     */             }
/*     */           } 
/*     */           
/* 231 */           float currentProgress = iterator.nextIndex() / totalSize;
/* 232 */           WorldUpgrader.this.progressMap.put(dimensionKey, currentProgress);
/* 233 */           totalProgress += currentProgress;
/*     */         } 
/*     */         
/* 236 */         WorldUpgrader.this.progress = totalProgress;
/*     */         
/* 238 */         if (!worked) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 243 */       WorldUpgrader.this.status = this.finishedStatus;
/*     */       
/* 245 */       for (WorldUpgrader.DimensionToUpgrade dimensionToUpgrade : dimensionsToUpgrade) {
/*     */         try {
/* 247 */           dimensionToUpgrade.storage.close();
/* 248 */         } catch (Exception e) {
/* 249 */           WorldUpgrader.LOGGER.error("Error upgrading chunk", e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private List<WorldUpgrader.DimensionToUpgrade> getDimensionsToUpgrade() {
/* 255 */       List<WorldUpgrader.DimensionToUpgrade> dimensionsToUpgrade = Lists.newArrayList();
/* 256 */       for (ResourceKey<Level> dimensionKey : WorldUpgrader.this.levels) {
/* 257 */         RegionStorageInfo info = new RegionStorageInfo(WorldUpgrader.this.levelStorage.getLevelId(), dimensionKey, this.type);
/* 258 */         Path regionFolder = WorldUpgrader.this.levelStorage.getDimensionPath(dimensionKey).resolve(this.folderName);
/* 259 */         SimpleRegionStorage storage = createStorage(info, regionFolder);
/* 260 */         ListIterator<WorldUpgrader.FileToUpgrade> files = getFilesToProcess(info, regionFolder);
/* 261 */         dimensionsToUpgrade.add(new WorldUpgrader.DimensionToUpgrade(dimensionKey, storage, files));
/*     */       } 
/* 263 */       return dimensionsToUpgrade;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private ListIterator<WorldUpgrader.FileToUpgrade> getFilesToProcess(RegionStorageInfo info, Path regionFolder) {
/* 269 */       List<WorldUpgrader.FileToUpgrade> filesToUpgrade = getAllChunkPositions(info, regionFolder);
/* 270 */       WorldUpgrader.this.totalFiles += filesToUpgrade.size();
/* 271 */       WorldUpgrader.this.totalChunks += filesToUpgrade.stream().mapToInt(fileToUpgrade -> fileToUpgrade.chunksToUpgrade.size()).sum();
/* 272 */       return filesToUpgrade.listIterator();
/*     */     }
/*     */     
/*     */     private static List<WorldUpgrader.FileToUpgrade> getAllChunkPositions(RegionStorageInfo info, Path regionFolder) {
/* 276 */       File[] files = regionFolder.toFile().listFiles((dir, name) -> name.endsWith(".mca"));
/*     */       
/* 278 */       if (files == null) {
/* 279 */         return List.of();
/*     */       }
/*     */       
/* 282 */       List<WorldUpgrader.FileToUpgrade> regionFileChunks = Lists.newArrayList();
/* 283 */       for (File regionFile : files) {
/* 284 */         Matcher regex = WorldUpgrader.REGEX.matcher(regionFile.getName());
/* 285 */         if (regex.matches()) {
/*     */ 
/*     */ 
/*     */           
/* 289 */           int xOffset = Integer.parseInt(regex.group(1)) << 5;
/* 290 */           int zOffset = Integer.parseInt(regex.group(2)) << 5;
/*     */           
/* 292 */           List<ChunkPos> chunkPositions = Lists.newArrayList(); 
/* 293 */           try { RegionFile regionSource = new RegionFile(info, regionFile.toPath(), regionFolder, true); 
/* 294 */             try { for (int x = 0; x < 32; x++) {
/* 295 */                 for (int z = 0; z < 32; z++) {
/* 296 */                   ChunkPos pos = new ChunkPos(x + xOffset, z + zOffset);
/* 297 */                   if (regionSource.doesChunkExist(pos)) {
/* 298 */                     chunkPositions.add(pos);
/*     */                   }
/*     */                 } 
/*     */               } 
/* 302 */               if (!chunkPositions.isEmpty()) {
/* 303 */                 regionFileChunks.add(new WorldUpgrader.FileToUpgrade(regionSource, chunkPositions));
/*     */               }
/* 305 */               regionSource.close(); } catch (Throwable throwable) { try { regionSource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable t)
/* 306 */           { WorldUpgrader.LOGGER.error("Failed to read chunks from region file {}", regionFile.toPath(), t); }
/*     */         
/*     */         } 
/* 309 */       }  return regionFileChunks;
/*     */     }
/*     */     
/*     */     private boolean processOnePosition(ResourceKey<Level> dimension, SimpleRegionStorage storage, ChunkPos pos) {
/* 313 */       boolean converted = false;
/*     */       try {
/* 315 */         converted = tryProcessOnePosition(storage, pos, dimension);
/* 316 */       } catch (ReportedException|java.util.concurrent.CompletionException e) {
/* 317 */         Throwable cause = e.getCause();
/* 318 */         if (cause instanceof IOException) {
/* 319 */           WorldUpgrader.LOGGER.error("Error upgrading chunk {}", pos, cause);
/*     */         } else {
/* 321 */           throw e;
/*     */         } 
/*     */       } 
/* 324 */       if (converted) {
/* 325 */         WorldUpgrader.this.converted++;
/*     */       } else {
/* 327 */         WorldUpgrader.this.skipped++;
/*     */       } 
/* 329 */       return converted;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void onFileFinished(RegionFile regionFile) {
/* 335 */       if (!WorldUpgrader.this.recreateRegionFiles) {
/*     */         return;
/*     */       }
/* 338 */       if (this.previousWriteFuture != null) {
/* 339 */         this.previousWriteFuture.join();
/*     */       }
/* 341 */       Path filePath = regionFile.getPath();
/* 342 */       Path directoryPath = filePath.getParent();
/* 343 */       Path newFilePath = WorldUpgrader.resolveRecreateDirectory(directoryPath).resolve(filePath.getFileName().toString());
/*     */       
/*     */       try {
/* 346 */         if (newFilePath.toFile().exists()) {
/* 347 */           Files.delete(filePath);
/* 348 */           Files.move(newFilePath, filePath, new java.nio.file.CopyOption[0]);
/*     */         } else {
/* 350 */           WorldUpgrader.LOGGER.error("Failed to replace an old region file. New file {} does not exist.", newFilePath);
/*     */         } 
/* 352 */       } catch (IOException e) {
/* 353 */         WorldUpgrader.LOGGER.error("Failed to replace an old region file", e);
/*     */       } 
/*     */     }
/*     */     protected abstract SimpleRegionStorage createStorage(RegionStorageInfo param1RegionStorageInfo, Path param1Path);
/*     */     protected abstract boolean tryProcessOnePosition(SimpleRegionStorage param1SimpleRegionStorage, ChunkPos param1ChunkPos, ResourceKey<Level> param1ResourceKey); }
/*     */   
/* 359 */   private static Path resolveRecreateDirectory(Path directoryPath) { return directoryPath.resolveSibling("new_" + directoryPath.getFileName().toString()); }
/*     */   
/*     */   private abstract class SimpleRegionStorageUpgrader
/*     */     extends AbstractUpgrader
/*     */   {
/* 364 */     private SimpleRegionStorageUpgrader(DataFixTypes type, String folderName, Component upgradingStatus, Component finishedStatus) { super(WorldUpgrader.this, type, folderName, folderName, upgradingStatus, finishedStatus); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected SimpleRegionStorage createStorage(RegionStorageInfo info, Path regionFolder) {
/* 369 */       return WorldUpgrader.this.recreateRegionFiles ? 
/* 370 */         new RecreatingSimpleRegionStorage(info
/* 371 */           .withTypeSuffix("source"), regionFolder, info
/* 372 */           .withTypeSuffix("target"), WorldUpgrader.resolveRecreateDirectory(regionFolder), WorldUpgrader.this.dataFixer, true, this.dataFixType, LegacyTagFixer.EMPTY) : 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 378 */         new SimpleRegionStorage(info, regionFolder, WorldUpgrader.this.dataFixer, true, this.dataFixType);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean tryProcessOnePosition(SimpleRegionStorage storage, ChunkPos pos, ResourceKey<Level> dimension) {
/* 383 */       CompoundTag chunkTag = (CompoundTag)((Optional)storage.read(pos).join()).orElse(null);
/* 384 */       if (chunkTag != null) {
/* 385 */         int version = NbtUtils.getDataVersion(chunkTag);
/*     */         
/* 387 */         CompoundTag upgradedTag = upgradeTag(storage, chunkTag);
/*     */         
/* 389 */         boolean changed = (version < SharedConstants.getCurrentVersion().dataVersion().version());
/* 390 */         if (changed || WorldUpgrader.this.recreateRegionFiles) {
/* 391 */           if (this.previousWriteFuture != null) {
/* 392 */             this.previousWriteFuture.join();
/*     */           }
/* 394 */           this.previousWriteFuture = storage.write(pos, upgradedTag);
/* 395 */           return true;
/*     */         } 
/*     */       } 
/* 398 */       return false;
/*     */     }
/*     */     
/*     */     protected abstract CompoundTag upgradeTag(SimpleRegionStorage param1SimpleRegionStorage, CompoundTag param1CompoundTag);
/*     */   }
/*     */   
/*     */   private class PoiUpgrader
/*     */     extends SimpleRegionStorageUpgrader {
/* 406 */     private PoiUpgrader(WorldUpgrader this$0) { super(this$0, DataFixTypes.POI_CHUNK, "poi", WorldUpgrader.STATUS_UPGRADING_POI, WorldUpgrader.STATUS_FINISHED_POI); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 412 */     protected CompoundTag upgradeTag(SimpleRegionStorage storage, CompoundTag chunkTag) { return storage.upgradeChunkTag(chunkTag, 1945); }
/*     */   }
/*     */   
/*     */   private class EntityUpgrader
/*     */     extends SimpleRegionStorageUpgrader
/*     */   {
/* 418 */     private EntityUpgrader(WorldUpgrader this$0) { super(this$0, DataFixTypes.ENTITY_CHUNK, "entities", WorldUpgrader.STATUS_UPGRADING_ENTITIES, WorldUpgrader.STATUS_FINISHED_ENTITIES); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 423 */     protected CompoundTag upgradeTag(SimpleRegionStorage storage, CompoundTag chunkTag) { return storage.upgradeChunkTag(chunkTag, -1); }
/*     */   }
/*     */   
/*     */   private class ChunkUpgrader
/*     */     extends AbstractUpgrader
/*     */   {
/* 429 */     private ChunkUpgrader() { super(WorldUpgrader.this, DataFixTypes.CHUNK, "chunk", "region", WorldUpgrader.STATUS_UPGRADING_CHUNKS, WorldUpgrader.STATUS_FINISHED_CHUNKS); }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean tryProcessOnePosition(SimpleRegionStorage storage, ChunkPos pos, ResourceKey<Level> dimension) {
/* 434 */       CompoundTag chunkTag = (CompoundTag)((Optional)storage.read(pos).join()).orElse(null);
/* 435 */       if (chunkTag != null) {
/* 436 */         int version = NbtUtils.getDataVersion(chunkTag);
/*     */         
/* 438 */         ChunkGenerator generator = ((LevelStem)WorldUpgrader.this.dimensions.getValueOrThrow(Registries.levelToLevelStem(dimension))).generator();
/* 439 */         CompoundTag upgradedTag = storage.upgradeChunkTag(chunkTag, -1, ChunkMap.getChunkDataFixContextTag(dimension, generator.getTypeNameForDataFixer()));
/*     */         
/* 441 */         ChunkPos storedPos = new ChunkPos(upgradedTag.getIntOr("xPos", 0), upgradedTag.getIntOr("zPos", 0));
/* 442 */         if (!storedPos.equals(pos)) {
/* 443 */           WorldUpgrader.LOGGER.warn("Chunk {} has invalid position {}", pos, storedPos);
/*     */         }
/*     */         
/* 446 */         boolean changed = (version < SharedConstants.getCurrentVersion().dataVersion().version());
/* 447 */         if (WorldUpgrader.this.eraseCache) {
/* 448 */           changed = (changed || upgradedTag.contains("Heightmaps"));
/* 449 */           upgradedTag.remove("Heightmaps");
/* 450 */           changed = (changed || upgradedTag.contains("isLightOn"));
/* 451 */           upgradedTag.remove("isLightOn");
/*     */           
/* 453 */           ListTag sections = upgradedTag.getListOrEmpty("sections");
/* 454 */           for (int i = 0; i < sections.size(); i++) {
/* 455 */             Optional<CompoundTag> maybeSection = sections.getCompound(i);
/* 456 */             if (!maybeSection.isEmpty()) {
/*     */ 
/*     */               
/* 459 */               CompoundTag section = (CompoundTag)maybeSection.get();
/* 460 */               changed = (changed || section.contains("BlockLight"));
/* 461 */               section.remove("BlockLight");
/* 462 */               changed = (changed || section.contains("SkyLight"));
/* 463 */               section.remove("SkyLight");
/*     */             } 
/*     */           } 
/*     */         } 
/* 467 */         if (changed || WorldUpgrader.this.recreateRegionFiles) {
/* 468 */           if (this.previousWriteFuture != null) {
/* 469 */             this.previousWriteFuture.join();
/*     */           }
/* 471 */           this.previousWriteFuture = storage.write(pos, upgradedTag);
/* 472 */           return true;
/*     */         } 
/*     */       } 
/* 475 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected SimpleRegionStorage createStorage(RegionStorageInfo info, Path regionFolder) {
/* 480 */       Supplier<LegacyTagFixer> legacyFixer = LegacyStructureDataHandler.getLegacyTagFixer(info.dimension(), () -> WorldUpgrader.this.overworldDataStorage, WorldUpgrader.this.dataFixer);
/* 481 */       return WorldUpgrader.this.recreateRegionFiles ? 
/* 482 */         new RecreatingSimpleRegionStorage(info
/* 483 */           .withTypeSuffix("source"), regionFolder, info
/* 484 */           .withTypeSuffix("target"), WorldUpgrader.resolveRecreateDirectory(regionFolder), WorldUpgrader.this.dataFixer, true, DataFixTypes.CHUNK, legacyFixer) : 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 490 */         new SimpleRegionStorage(info, regionFolder, WorldUpgrader.this.dataFixer, true, DataFixTypes.CHUNK, legacyFixer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\worldupdate\WorldUpgrader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */