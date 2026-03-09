/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtFormatException;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.nbt.visitors.SkipFields;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.util.DirectoryLock;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import net.minecraft.world.level.validation.PathAllowList;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelStorageSource {
/*  80 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String TAG_DATA = "Data";
/*     */   
/*     */   private static final PathMatcher NO_SYMLINKS_ALLOWED = path -> false;
/*     */   public static final String ALLOWED_SYMLINKS_CONFIG_NAME = "allowed_symlinks.txt";
/*     */   private static final int DISK_SPACE_WARNING_THRESHOLD = 67108864;
/*     */   private final Path baseDir;
/*     */   private final Path backupDir;
/*     */   private final DataFixer fixerUpper;
/*     */   private final DirectoryValidator worldDirValidator;
/*     */   
/*     */   public LevelStorageSource(Path baseDir, Path backupDir, DirectoryValidator worldDirValidator, DataFixer fixerUpper) {
/*  93 */     this.fixerUpper = fixerUpper;
/*     */     try {
/*  95 */       FileUtil.createDirectoriesSafe(baseDir);
/*  96 */     } catch (IOException e) {
/*  97 */       throw new UncheckedIOException(e);
/*     */     } 
/*  99 */     this.baseDir = baseDir;
/* 100 */     this.backupDir = backupDir;
/*     */     
/* 102 */     this.worldDirValidator = worldDirValidator;
/*     */   }
/*     */   
/*     */   public static DirectoryValidator parseValidator(Path configPath) {
/* 106 */     if (Files.exists(configPath, new java.nio.file.LinkOption[0])) {
/* 107 */       try { BufferedReader reader = Files.newBufferedReader(configPath); 
/* 108 */         try { DirectoryValidator directoryValidator = new DirectoryValidator(PathAllowList.readPlain(reader));
/* 109 */           if (reader != null) reader.close();  return directoryValidator; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 110 */       { LOGGER.error("Failed to parse {}, disallowing all symbolic links", "allowed_symlinks.txt", e); }
/*     */     
/*     */     }
/*     */     
/* 114 */     return new DirectoryValidator(NO_SYMLINKS_ALLOWED);
/*     */   }
/*     */   
/*     */   public static LevelStorageSource createDefault(Path path) {
/* 118 */     DirectoryValidator validator = parseValidator(path.resolve("allowed_symlinks.txt"));
/* 119 */     return new LevelStorageSource(path, path.resolve("../backups"), validator, DataFixers.getDataFixer());
/*     */   }
/*     */ 
/*     */   
/* 123 */   public static WorldDataConfiguration readDataConfig(Dynamic<?> levelData) { Objects.requireNonNull(LOGGER); return (WorldDataConfiguration)WorldDataConfiguration.CODEC.parse(levelData).resultOrPartial(LOGGER::error).orElse(WorldDataConfiguration.DEFAULT); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public static WorldLoader.PackConfig getPackConfig(Dynamic<?> levelDataTag, PackRepository packRepository, boolean safeMode) { return new WorldLoader.PackConfig(packRepository, readDataConfig(levelDataTag), safeMode, false); }
/*     */ 
/*     */   
/*     */   public static LevelDataAndDimensions getLevelDataAndDimensions(Dynamic<?> levelDataTag, WorldDataConfiguration dataConfiguration, Registry<LevelStem> datapackDimensions, HolderLookup.Provider registryAccess) {
/* 131 */     Dynamic<?> dataTag = RegistryOps.injectRegistryContext(levelDataTag, registryAccess);
/* 132 */     Dynamic<?> worldGenSettingsTag = dataTag.get("WorldGenSettings").orElseEmptyMap();
/* 133 */     WorldGenSettings worldGenSettings = (WorldGenSettings)WorldGenSettings.CODEC.parse(worldGenSettingsTag).getOrThrow();
/* 134 */     LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
/* 135 */     WorldDimensions.Complete dimensions = worldGenSettings.dimensions().bake(datapackDimensions);
/* 136 */     Lifecycle lifecycle = dimensions.lifecycle().add(registryAccess.allRegistriesLifecycle());
/*     */     
/* 138 */     PrimaryLevelData worldData = PrimaryLevelData.parse(dataTag, settings, dimensions.specialWorldProperty(), worldGenSettings.options(), lifecycle);
/* 139 */     return new LevelDataAndDimensions(worldData, dimensions);
/*     */   }
/*     */ 
/*     */   
/* 143 */   public String getName() { return "Anvil"; }
/*     */ 
/*     */   
/*     */   public LevelCandidates findLevelCandidates() throws LevelStorageException {
/* 147 */     if (!Files.isDirectory(this.baseDir, new java.nio.file.LinkOption[0])) {
/* 148 */       throw new LevelStorageException(Component.translatable("selectWorld.load_folder_access"));
/*     */     }
/*     */     
/* 151 */     try { Stream<Path> paths = Files.list(this.baseDir);
/*     */ 
/*     */ 
/*     */       
/* 155 */       try { List<LevelDirectory> candidates = paths.filter(x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])).map(LevelDirectory::new).filter(directory -> (Files.isRegularFile(directory.dataFile(), new java.nio.file.LinkOption[0]) || Files.isRegularFile(directory.oldDataFile(), new java.nio.file.LinkOption[0]))).toList();
/*     */         
/* 157 */         LevelCandidates levelCandidates = new LevelCandidates(candidates);
/* 158 */         if (paths != null) paths.close();  return levelCandidates; } catch (Throwable throwable) { if (paths != null) try { paths.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 159 */     { throw new LevelStorageException(Component.translatable("selectWorld.load_folder_access")); }
/*     */   
/*     */   }
/*     */   
/*     */   public CompletableFuture<List<LevelSummary>> loadLevelSummaries(LevelCandidates candidates) {
/* 164 */     List<CompletableFuture<LevelSummary>> futures = new ArrayList<CompletableFuture<LevelSummary>>(candidates.levels.size());
/* 165 */     for (Iterator iterator = candidates.levels.iterator(); iterator.hasNext(); ) { LevelDirectory level = (LevelDirectory)iterator.next();
/* 166 */       futures.add(CompletableFuture.supplyAsync(() -> {
/*     */               boolean locked;
/*     */               try {
/* 169 */                 locked = DirectoryLock.isLocked(level.path());
/* 170 */               } catch (Exception e) {
/* 171 */                 LOGGER.warn("Failed to read {} lock", level.path(), e);
/* 172 */                 return null;
/*     */               } 
/*     */               
/*     */               try {
/* 176 */                 return readLevelSummary(level, locked);
/* 177 */               } catch (OutOfMemoryError e) {
/* 178 */                 MemoryReserve.release();
/*     */                 
/* 180 */                 String detailedMessage = "Ran out of memory trying to read summary of world folder \"" + level.directoryName() + "\"";
/* 181 */                 LOGGER.error(LogUtils.FATAL_MARKER, detailedMessage);
/*     */                 
/* 183 */                 OutOfMemoryError detailedException = new OutOfMemoryError("Ran out of memory reading level data");
/* 184 */                 detailedException.initCause(e);
/* 185 */                 CrashReport crashReport = CrashReport.forThrowable(detailedException, detailedMessage);
/* 186 */                 CrashReportCategory worldDetails = crashReport.addCategory("World details");
/* 187 */                 worldDetails.setDetail("Folder Name", level.directoryName());
/*     */                 
/*     */                 try {
/* 190 */                   long size = Files.size(level.dataFile());
/* 191 */                   worldDetails.setDetail("level.dat size", Long.valueOf(size));
/* 192 */                 } catch (IOException ex) {
/* 193 */                   worldDetails.setDetailError("level.dat size", ex);
/*     */                 } 
/*     */                 
/* 196 */                 throw new ReportedException(crashReport);
/*     */               } 
/* 198 */             }Util.backgroundExecutor().forName("loadLevelSummaries"))); }
/*     */ 
/*     */     
/* 201 */     return Util.sequenceFailFastAndCancel(futures)
/* 202 */       .thenApply(levels -> levels.stream()
/* 203 */         .filter(Objects::nonNull)
/* 204 */         .sorted()
/* 205 */         .toList());
/*     */   }
/*     */ 
/*     */   
/* 209 */   private int getStorageVersion() { return 19133; }
/*     */ 
/*     */ 
/*     */   
/* 213 */   private static CompoundTag readLevelDataTagRaw(Path dataFile) throws IOException { return NbtIo.readCompressed(dataFile, NbtAccounter.uncompressedQuota()); }
/*     */ 
/*     */   
/*     */   private static Dynamic<?> readLevelDataTagFixed(Path dataFile, DataFixer dataFixer) throws IOException {
/* 217 */     CompoundTag root = readLevelDataTagRaw(dataFile);
/* 218 */     CompoundTag dataTag = root.getCompoundOrEmpty("Data");
/*     */     
/* 220 */     int dataVersion = NbtUtils.getDataVersion(dataTag);
/* 221 */     updated = DataFixTypes.LEVEL.updateToCurrentVersion(dataFixer, new Dynamic(NbtOps.INSTANCE, dataTag), dataVersion);
/*     */     
/* 223 */     updated = updated.update("Player", playerTagUnfixed -> 
/* 224 */         DataFixTypes.PLAYER.updateToCurrentVersion(dataFixer, playerTagUnfixed, dataVersion));
/*     */ 
/*     */     
/* 227 */     return updated.update("WorldGenSettings", worldGenSettingsTagUnfixed -> 
/* 228 */         DataFixTypes.WORLD_GEN_SETTINGS.updateToCurrentVersion(dataFixer, worldGenSettingsTagUnfixed, dataVersion));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private LevelSummary readLevelSummary(LevelDirectory level, boolean locked) {
/* 235 */     Path dataFile = level.dataFile();
/* 236 */     if (Files.exists(dataFile, new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 238 */         if (Files.isSymbolicLink(dataFile)) {
/* 239 */           List<ForbiddenSymlinkInfo> issues = this.worldDirValidator.validateSymlink(dataFile);
/* 240 */           if (!issues.isEmpty()) {
/* 241 */             LOGGER.warn("{}", ContentValidationException.getMessage(dataFile, issues));
/* 242 */             return new LevelSummary.SymlinkLevelSummary(level.directoryName(), level.iconFile());
/*     */           } 
/*     */         } 
/* 245 */         Tag result = readLightweightData(dataFile);
/* 246 */         if (result instanceof CompoundTag) { CompoundTag root = (CompoundTag)result;
/* 247 */           CompoundTag tag = root.getCompoundOrEmpty("Data");
/* 248 */           int dataVersion = NbtUtils.getDataVersion(tag);
/* 249 */           Dynamic<?> updated = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(this.fixerUpper, new Dynamic(NbtOps.INSTANCE, tag), dataVersion);
/* 250 */           return makeLevelSummary(updated, level, locked); }
/*     */         
/* 252 */         LOGGER.warn("Invalid root tag in {}", dataFile);
/*     */       }
/* 254 */       catch (Exception e) {
/* 255 */         LOGGER.error("Exception reading {}", dataFile, e);
/*     */       } 
/*     */     }
/* 258 */     return new LevelSummary.CorruptedLevelSummary(level.directoryName(), level.iconFile(), getFileModificationTime(level));
/*     */   }
/*     */   
/*     */   private static long getFileModificationTime(LevelDirectory level) {
/* 262 */     Instant timeStamp = getFileModificationTime(level.dataFile());
/* 263 */     if (timeStamp == null) {
/* 264 */       timeStamp = getFileModificationTime(level.oldDataFile());
/*     */     }
/* 266 */     return (timeStamp == null) ? -1L : timeStamp.toEpochMilli();
/*     */   }
/*     */   
/*     */   private static Instant getFileModificationTime(Path path) {
/*     */     try {
/* 271 */       return Files.getLastModifiedTime(path, new java.nio.file.LinkOption[0]).toInstant();
/* 272 */     } catch (IOException iOException) {
/*     */       
/* 274 */       return null;
/*     */     } 
/*     */   }
/*     */   private LevelSummary makeLevelSummary(Dynamic<?> dataTag, LevelDirectory levelDirectory, boolean locked) {
/* 278 */     LevelVersion levelVersion = LevelVersion.parse(dataTag);
/* 279 */     int levelDataVersion = levelVersion.levelDataVersion();
/* 280 */     if (levelDataVersion == 19132 || levelDataVersion == 19133) {
/* 281 */       boolean requiresManualConversion = (levelDataVersion != getStorageVersion());
/* 282 */       Path icon = levelDirectory.iconFile();
/* 283 */       WorldDataConfiguration dataConfiguration = readDataConfig(dataTag);
/* 284 */       LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
/* 285 */       FeatureFlagSet enabledFeatureFlags = parseFeatureFlagsFromSummary(dataTag);
/* 286 */       boolean experimental = FeatureFlags.isExperimental(enabledFeatureFlags);
/* 287 */       return new LevelSummary(settings, levelVersion, levelDirectory.directoryName(), requiresManualConversion, locked, experimental, icon);
/*     */     } 
/* 289 */     throw new NbtFormatException("Unknown data version: " + Integer.toHexString(levelDataVersion));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FeatureFlagSet parseFeatureFlagsFromSummary(Dynamic<?> tag) {
/* 297 */     Set<Identifier> enabledFlags = (Set)tag.get("enabled_features").asStream().flatMap(entry -> entry.asString().result().map(Identifier::tryParse).stream()).collect(Collectors.toSet());
/*     */     
/* 299 */     return FeatureFlags.REGISTRY.fromNames(enabledFlags, unknownId -> {
/*     */         
/*     */         });
/*     */   } private static Tag readLightweightData(Path dataFile) throws IOException {
/* 303 */     SkipFields parser = new SkipFields(new FieldSelector[] { new FieldSelector("Data", CompoundTag.TYPE, "Player"), new FieldSelector("Data", CompoundTag.TYPE, "WorldGenSettings") });
/*     */ 
/*     */ 
/*     */     
/* 307 */     NbtIo.parseCompressed(dataFile, parser, NbtAccounter.uncompressedQuota());
/* 308 */     return parser.getResult();
/*     */   }
/*     */   
/*     */   public boolean isNewLevelIdAcceptable(String levelId) {
/*     */     try {
/* 313 */       Path fullPath = getLevelPath(levelId);
/* 314 */       Files.createDirectory(fullPath, new java.nio.file.attribute.FileAttribute[0]);
/* 315 */       Files.deleteIfExists(fullPath);
/* 316 */       return true;
/* 317 */     } catch (IOException e) {
/* 318 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean levelExists(String levelId) {
/*     */     try {
/* 324 */       return Files.isDirectory(getLevelPath(levelId), new java.nio.file.LinkOption[0]);
/* 325 */     } catch (InvalidPathException e) {
/* 326 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 331 */   public Path getLevelPath(String levelId) { return this.baseDir.resolve(levelId); }
/*     */ 
/*     */ 
/*     */   
/* 335 */   public Path getBaseDir() { return this.baseDir; }
/*     */ 
/*     */ 
/*     */   
/* 339 */   public Path getBackupPath() { return this.backupDir; }
/*     */ 
/*     */   
/*     */   public LevelStorageAccess validateAndCreateAccess(String levelId) throws IOException, ContentValidationException {
/* 343 */     Path levelPath = getLevelPath(levelId);
/* 344 */     List<ForbiddenSymlinkInfo> validationResults = this.worldDirValidator.validateDirectory(levelPath, true);
/* 345 */     if (!validationResults.isEmpty()) {
/* 346 */       throw new ContentValidationException(levelPath, validationResults);
/*     */     }
/* 348 */     return new LevelStorageAccess(levelId, levelPath);
/*     */   }
/*     */   
/*     */   public LevelStorageAccess createAccess(String levelId) throws IOException, ContentValidationException {
/* 352 */     Path levelPath = getLevelPath(levelId);
/* 353 */     return new LevelStorageAccess(levelId, levelPath);
/*     */   }
/*     */ 
/*     */   
/* 357 */   public DirectoryValidator getWorldDirValidator() { return this.worldDirValidator; }
/*     */   public class LevelStorageAccess implements AutoCloseable { private final DirectoryLock lock;
/*     */     private final LevelStorageSource.LevelDirectory levelDirectory;
/*     */     private final String levelId;
/*     */     private final Map<LevelResource, Path> resources;
/*     */     
/*     */     private LevelStorageAccess(String levelId, Path path) throws IOException {
/* 364 */       this.resources = Maps.newHashMap();
/*     */ 
/*     */       
/* 367 */       this.levelId = levelId;
/* 368 */       this.levelDirectory = new LevelStorageSource.LevelDirectory(path);
/* 369 */       this.lock = DirectoryLock.create(path);
/*     */     }
/*     */     
/*     */     public long estimateDiskSpace() {
/*     */       try {
/* 374 */         return Files.getFileStore(this.levelDirectory.path).getUsableSpace();
/* 375 */       } catch (Exception ignored) {
/*     */         
/* 377 */         return Float.MAX_VALUE;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 382 */     public boolean checkForLowDiskSpace() { return (estimateDiskSpace() < 67108864L); }
/*     */ 
/*     */     
/*     */     public void safeClose() {
/*     */       try {
/* 387 */         close();
/* 388 */       } catch (IOException e) {
/* 389 */         LevelStorageSource.LOGGER.warn("Failed to unlock access to level {}", getLevelId(), e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 394 */     public LevelStorageSource parent() { return LevelStorageSource.this; }
/*     */ 
/*     */ 
/*     */     
/* 398 */     public LevelStorageSource.LevelDirectory getLevelDirectory() { return this.levelDirectory; }
/*     */ 
/*     */ 
/*     */     
/* 402 */     public String getLevelId() { return this.levelId; }
/*     */ 
/*     */ 
/*     */     
/* 406 */     public Path getLevelPath(LevelResource resource) { Objects.requireNonNull(this.levelDirectory); return (Path)this.resources.computeIfAbsent(resource, this.levelDirectory::resourcePath); }
/*     */ 
/*     */ 
/*     */     
/* 410 */     public Path getDimensionPath(ResourceKey<Level> name) { return DimensionType.getStorageFolder(name, this.levelDirectory.path()); }
/*     */ 
/*     */     
/*     */     private void checkLock() {
/* 414 */       if (!this.lock.isValid()) {
/* 415 */         throw new IllegalStateException("Lock is no longer valid");
/*     */       }
/*     */     }
/*     */     
/*     */     public PlayerDataStorage createPlayerStorage() {
/* 420 */       checkLock();
/* 421 */       return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
/*     */     }
/*     */     
/*     */     public LevelSummary getSummary(Dynamic<?> dataTag) {
/* 425 */       checkLock();
/* 426 */       return LevelStorageSource.this.makeLevelSummary(dataTag, this.levelDirectory, false);
/*     */     }
/*     */ 
/*     */     
/* 430 */     public Dynamic<?> getDataTag() throws IOException { return getDataTag(false); }
/*     */ 
/*     */ 
/*     */     
/* 434 */     public Dynamic<?> getDataTagFallback() throws IOException { return getDataTag(true); }
/*     */ 
/*     */     
/*     */     private Dynamic<?> getDataTag(boolean useFallback) throws IOException {
/* 438 */       checkLock();
/* 439 */       return LevelStorageSource.readLevelDataTagFixed(useFallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile(), LevelStorageSource.this.fixerUpper);
/*     */     }
/*     */ 
/*     */     
/* 443 */     public void saveDataTag(RegistryAccess registryAccess, WorldData levelData) { saveDataTag(registryAccess, levelData, null); }
/*     */ 
/*     */     
/*     */     public void saveDataTag(RegistryAccess registryAccess, WorldData levelData, CompoundTag playerData) {
/* 447 */       CompoundTag dataTag = levelData.createTag(registryAccess, playerData);
/*     */       
/* 449 */       CompoundTag root = new CompoundTag();
/* 450 */       root.put("Data", dataTag);
/*     */       
/* 452 */       saveLevelData(root);
/*     */     }
/*     */     
/*     */     private void saveLevelData(CompoundTag root) {
/* 456 */       Path worldDir = this.levelDirectory.path();
/*     */       try {
/* 458 */         Path dataFile = Files.createTempFile(worldDir, "level", ".dat", new java.nio.file.attribute.FileAttribute[0]);
/* 459 */         NbtIo.writeCompressed(root, dataFile);
/*     */         
/* 461 */         Path oldDataFile = this.levelDirectory.oldDataFile();
/* 462 */         Path currentFile = this.levelDirectory.dataFile();
/* 463 */         Util.safeReplaceFile(currentFile, dataFile, oldDataFile);
/* 464 */       } catch (Exception e) {
/* 465 */         LevelStorageSource.LOGGER.error("Failed to save level {}", worldDir, e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Optional<Path> getIconFile() {
/* 470 */       if (!this.lock.isValid()) {
/* 471 */         return Optional.empty();
/*     */       }
/* 473 */       return Optional.of(this.levelDirectory.iconFile());
/*     */     }
/*     */     
/*     */     public void deleteLevel() {
/* 477 */       checkLock();
/*     */       
/* 479 */       final Path lockPath = this.levelDirectory.lockFile();
/*     */       
/* 481 */       LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);
/* 482 */       for (int attempt = 1; attempt <= 5; attempt++) {
/* 483 */         LevelStorageSource.LOGGER.info("Attempt {}...", Integer.valueOf(attempt));
/*     */         
/*     */         try {
/* 486 */           Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>()
/*     */               {
/*     */                 public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 489 */                   if (!file.equals(lockPath)) {
/* 490 */                     LevelStorageSource.LOGGER.debug("Deleting {}", file);
/* 491 */                     Files.delete(file);
/*     */                   } 
/* 493 */                   return FileVisitResult.CONTINUE;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 498 */                   if (exc != null) {
/* 499 */                     throw exc;
/*     */                   }
/*     */                   
/* 502 */                   if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/*     */                     
/* 504 */                     LevelStorageSource.LevelStorageAccess.this.lock.close();
/* 505 */                     Files.deleteIfExists(lockPath);
/*     */                   } 
/* 507 */                   Files.delete(dir);
/* 508 */                   return FileVisitResult.CONTINUE;
/*     */                 }
/*     */               });
/*     */           break;
/* 512 */         } catch (IOException e) {
/* 513 */           if (attempt < 5) {
/* 514 */             LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), e);
/*     */             try {
/* 516 */               Thread.sleep(500L);
/* 517 */             } catch (InterruptedException interruptedException) {}
/*     */           } else {
/*     */             
/* 520 */             throw e;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 527 */     public void renameLevel(String newName) throws IOException { modifyLevelDataWithoutDatafix(tag -> tag.putString("LevelName", newName.trim())); }
/*     */ 
/*     */     
/*     */     public void renameAndDropPlayer(String newName) throws IOException {
/* 531 */       modifyLevelDataWithoutDatafix(tag -> {
/* 532 */             tag.putString("LevelName", newName.trim());
/* 533 */             tag.remove("Player");
/*     */           });
/*     */     }
/*     */     
/*     */     private void modifyLevelDataWithoutDatafix(Consumer<CompoundTag> updater) throws IOException {
/* 538 */       checkLock();
/*     */       
/* 540 */       CompoundTag root = LevelStorageSource.readLevelDataTagRaw(this.levelDirectory.dataFile());
/* 541 */       updater.accept(root.getCompoundOrEmpty("Data"));
/* 542 */       saveLevelData(root);
/*     */     }
/*     */     
/*     */     public long makeWorldBackup() {
/* 546 */       checkLock();
/* 547 */       String zipFilePrefix = FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now()) + "_" + FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now());
/*     */       
/* 549 */       Path root = LevelStorageSource.this.getBackupPath();
/*     */       try {
/* 551 */         FileUtil.createDirectoriesSafe(root);
/* 552 */       } catch (IOException e) {
/* 553 */         throw new RuntimeException(e);
/*     */       } 
/* 555 */       Path zipFilePath = root.resolve(FileUtil.findAvailableName(root, zipFilePrefix, ".zip"));
/*     */       
/* 557 */       final ZipOutputStream stream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath, new java.nio.file.OpenOption[0]))); 
/* 558 */       try { final Path rootPath = Paths.get(this.levelId, new String[0]);
/*     */         
/* 560 */         Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>()
/*     */             {
/*     */               public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 563 */                 if (path.endsWith("session.lock")) {
/* 564 */                   return FileVisitResult.CONTINUE;
/*     */                 }
/* 566 */                 String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/* 567 */                 ZipEntry entry = new ZipEntry(entryPath);
/* 568 */                 stream.putNextEntry(entry);
/* 569 */                 Files.asByteSource(path.toFile()).copyTo(stream);
/* 570 */                 stream.closeEntry();
/* 571 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */             });
/* 574 */         stream.close(); } catch (Throwable throwable) { try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/* 576 */        return Files.size(zipFilePath);
/*     */     }
/*     */ 
/*     */     
/* 580 */     public boolean hasWorldData() { return (Files.exists(this.levelDirectory.dataFile(), new java.nio.file.LinkOption[0]) || Files.exists(this.levelDirectory.oldDataFile(), new java.nio.file.LinkOption[0])); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 585 */     public void close() { this.lock.close(); }
/*     */ 
/*     */ 
/*     */     
/* 589 */     public boolean restoreLevelDataFromOld() { return Util.safeReplaceOrMoveFile(this.levelDirectory.dataFile(), this.levelDirectory.oldDataFile(), this.levelDirectory.corruptedDataFile(ZonedDateTime.now()), true); }
/*     */ 
/*     */ 
/*     */     
/* 593 */     public Instant getFileModificationTime(boolean fallback) { return LevelStorageSource.getFileModificationTime(fallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile()); } } class null extends SimpleFileVisitor<Path> {
/*     */     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException { if (!file.equals(lockPath)) { LevelStorageSource.LOGGER.debug("Deleting {}", file); Files.delete(file); }  return FileVisitResult.CONTINUE; } public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException { if (exc != null) throw exc;  if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) { LevelStorageSource.LevelStorageAccess.this.lock.close(); Files.deleteIfExists(lockPath); }  Files.delete(dir); return FileVisitResult.CONTINUE; }
/*     */   } class null extends SimpleFileVisitor<Path> {
/*     */     public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException { if (path.endsWith("session.lock")) return FileVisitResult.CONTINUE;  String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/'); ZipEntry entry = new ZipEntry(entryPath); stream.putNextEntry(entry); Files.asByteSource(path.toFile()).copyTo(stream); stream.closeEntry(); return FileVisitResult.CONTINUE; }
/* 597 */   } public static final class LevelCandidates extends Record implements Iterable<LevelDirectory> { public LevelCandidates(List<LevelStorageSource.LevelDirectory> levels) { this.levels = levels; } private final List<LevelStorageSource.LevelDirectory> levels; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #597	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #597	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #597	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/* 597 */       //   0	8	1	o	Ljava/lang/Object; } public List<LevelStorageSource.LevelDirectory> levels() { return this.levels; }
/*     */     
/* 599 */     public boolean isEmpty() { return this.levels.isEmpty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 604 */     public Iterator<LevelStorageSource.LevelDirectory> iterator() { return this.levels.iterator(); } }
/*     */   
/*     */   public static final class LevelDirectory extends Record { private final Path path;
/*     */     
/* 608 */     public LevelDirectory(Path path) { this.path = path; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #608	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #608	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #608	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/* 608 */       //   0	8	1	o	Ljava/lang/Object; } public Path path() { return this.path; }
/*     */     
/* 610 */     public String directoryName() { return this.path.getFileName().toString(); }
/*     */ 
/*     */ 
/*     */     
/* 614 */     public Path dataFile() { return resourcePath(LevelResource.LEVEL_DATA_FILE); }
/*     */ 
/*     */ 
/*     */     
/* 618 */     public Path oldDataFile() { return resourcePath(LevelResource.OLD_LEVEL_DATA_FILE); }
/*     */ 
/*     */ 
/*     */     
/* 622 */     public Path corruptedDataFile(ZonedDateTime time) { return this.path.resolve(LevelResource.LEVEL_DATA_FILE.getId() + "_corrupted_" + LevelResource.LEVEL_DATA_FILE.getId()); }
/*     */ 
/*     */ 
/*     */     
/* 626 */     public Path rawDataFile(ZonedDateTime time) { return this.path.resolve(LevelResource.LEVEL_DATA_FILE.getId() + "_raw_" + LevelResource.LEVEL_DATA_FILE.getId()); }
/*     */ 
/*     */ 
/*     */     
/* 630 */     public Path iconFile() { return resourcePath(LevelResource.ICON_FILE); }
/*     */ 
/*     */ 
/*     */     
/* 634 */     public Path lockFile() { return resourcePath(LevelResource.LOCK_FILE); }
/*     */ 
/*     */ 
/*     */     
/* 638 */     public Path resourcePath(LevelResource resource) { return this.path.resolve(resource.getId()); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelStorageSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */