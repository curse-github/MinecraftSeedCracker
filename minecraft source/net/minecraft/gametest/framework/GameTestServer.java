/*     */ package net.minecraft.gametest.framework;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceSelectorArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*     */ import net.minecraft.server.notifications.EmptyNotificationService;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.server.players.UserNameToIdResolver;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestServer extends MinecraftServer {
/*  77 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   private static final int PROGRESS_REPORT_INTERVAL = 20;
/*     */   
/*     */   private static final int TEST_POSITION_RANGE = 14999992;
/*     */   
/*  84 */   private static final Services NO_SERVICES = new Services(null, ServicesKeySet.EMPTY, null, new MockUserNameToIdResolver(), new MockProfileResolver());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  91 */   private static final FeatureFlagSet ENABLED_FEATURES = FeatureFlags.REGISTRY.allFlags().subtract(FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS, new FeatureFlag[] { FeatureFlags.MINECART_IMPROVEMENTS }));
/*     */   
/*  93 */   private final LocalSampleLogger sampleLogger = new LocalSampleLogger(4);
/*     */   private final Optional<String> testSelection;
/*     */   private final boolean verify;
/*  96 */   private List<GameTestBatch> testBatches = new ArrayList();
/*  97 */   private final Stopwatch stopwatch = Stopwatch.createUnstarted();
/*     */   
/*  99 */   private static final WorldOptions WORLD_OPTIONS = new WorldOptions(0L, false, false);
/*     */   
/*     */   private MultipleTestTracker testTracker;
/*     */ 
/*     */   
/*     */   public static GameTestServer create(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, Optional<String> testSelection, boolean verify) {
/* 105 */     packRepository.reload();
/*     */ 
/*     */     
/* 108 */     ArrayList<String> enabledPacks = new ArrayList<String>(packRepository.getAvailableIds());
/* 109 */     enabledPacks.remove("vanilla");
/* 110 */     enabledPacks.addFirst("vanilla");
/*     */     
/* 112 */     WorldDataConfiguration defaultTestConfig = new WorldDataConfiguration(new DataPackConfig(enabledPacks, List.of()), ENABLED_FEATURES);
/*     */ 
/*     */ 
/*     */     
/* 116 */     LevelSettings testSettings = new LevelSettings("Test Level", GameType.CREATIVE, false, Difficulty.NORMAL, true, new GameRules(ENABLED_FEATURES), defaultTestConfig);
/*     */     
/* 118 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, defaultTestConfig, false, true);
/* 119 */     WorldLoader.InitConfig initConfig = new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.DEDICATED, LevelBasedPermissionSet.OWNER);
/*     */     
/*     */     try {
/* 122 */       LOGGER.debug("Starting resource loading");
/* 123 */       Stopwatch stopwatch = Stopwatch.createStarted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       WorldStem worldStem = (WorldStem)Util.blockUntilDone(executor -> WorldLoader.load(initConfig, (), WorldStem::new, Util.backgroundExecutor(), executor)).get();
/* 142 */       stopwatch.stop();
/* 143 */       LOGGER.debug("Finished resource loading after {} ms", Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/* 144 */       return new GameTestServer(serverThread, levelStorageSource, packRepository, worldStem, testSelection, verify);
/* 145 */     } catch (Exception e) {
/* 146 */       LOGGER.warn("Failed to load vanilla datapack, bit oops", e);
/* 147 */       System.exit(-1);
/* 148 */       throw new IllegalStateException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private GameTestServer(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, WorldStem worldStem, Optional<String> testSelection, boolean verify) {
/* 153 */     super(serverThread, levelStorageSource, packRepository, worldStem, Proxy.NO_PROXY, DataFixers.getDataFixer(), NO_SERVICES, LoggingLevelLoadListener.forDedicatedServer());
/* 154 */     this.testSelection = testSelection;
/* 155 */     this.verify = verify;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean initServer() {
/* 160 */     setPlayerList(new PlayerList(this, this, registries(), this.playerDataStorage, new EmptyNotificationService()) {  });
/* 161 */     Gizmos.withCollector(GizmoCollector.NOOP);
/* 162 */     loadLevel();
/* 163 */     ServerLevel level = overworld();
/* 164 */     this.testBatches = evaluateTestsToRun(level);
/* 165 */     LOGGER.info("Started game test server");
/* 166 */     return true;
/*     */   } private List<GameTestBatch> evaluateTestsToRun(ServerLevel level) {
/*     */     GameTestBatchFactory.TestDecorator decorator;
/*     */     Collection<Holder.Reference<GameTestInstance>> tests;
/* 170 */     Registry<GameTestInstance> testRegistry = level.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE);
/*     */ 
/*     */     
/* 173 */     if (this.testSelection.isPresent()) {
/* 174 */       tests = getTestsForSelection(level.registryAccess(), (String)this.testSelection.get()).filter(test -> !((GameTestInstance)test.value()).manualOnly()).toList();
/* 175 */       if (this.verify) {
/* 176 */         decorator = GameTestServer::rotateAndMultiply;
/* 177 */         LOGGER.info("Verify requested. Will run each test that matches {} {} times", this.testSelection.get(), Integer.valueOf(100 * Rotation.values().length));
/*     */       } else {
/* 179 */         decorator = GameTestBatchFactory.DIRECT;
/* 180 */         LOGGER.info("Will run tests matching {} ({} tests)", this.testSelection.get(), Integer.valueOf(tests.size()));
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 185 */       tests = testRegistry.listElements().filter(test -> !((GameTestInstance)test.value()).manualOnly()).toList();
/* 186 */       decorator = GameTestBatchFactory.DIRECT;
/*     */     } 
/*     */     
/* 189 */     return GameTestBatchFactory.divideIntoBatches(tests, decorator, level);
/*     */   }
/*     */   
/*     */   private static Stream<GameTestInfo> rotateAndMultiply(Holder.Reference<GameTestInstance> test, ServerLevel level) {
/* 193 */     Stream.Builder<GameTestInfo> builder = Stream.builder();
/* 194 */     for (Rotation rotation : Rotation.values()) {
/* 195 */       for (int i = 0; i < 100; i++) {
/* 196 */         builder.add(new GameTestInfo(test, rotation, level, RetryOptions.noRetries()));
/*     */       }
/*     */     } 
/* 199 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/* 203 */   public static Stream<Holder.Reference<GameTestInstance>> getTestsForSelection(RegistryAccess registries, String selection) { return ResourceSelectorArgument.parse(new StringReader(selection), registries.lookupOrThrow(Registries.TEST_INSTANCE)).stream(); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 208 */     super.tickServer(haveTime);
/*     */     
/* 210 */     ServerLevel level = overworld();
/*     */     
/* 212 */     if (!haveTestsStarted()) {
/* 213 */       startTests(level);
/*     */     }
/*     */     
/* 216 */     if (level.getGameTime() % 20L == 0L) {
/* 217 */       LOGGER.info(this.testTracker.getProgressBar());
/*     */     }
/*     */     
/* 220 */     if (this.testTracker.isDone()) {
/* 221 */       halt(false);
/* 222 */       LOGGER.info(this.testTracker.getProgressBar());
/*     */       
/* 224 */       GlobalTestReporter.finish();
/*     */       
/* 226 */       LOGGER.info("========= {} GAME TESTS COMPLETE IN {} ======================", Integer.valueOf(this.testTracker.getTotalCount()), this.stopwatch.stop());
/* 227 */       if (this.testTracker.hasFailedRequired()) {
/* 228 */         LOGGER.info("{} required tests failed :(", Integer.valueOf(this.testTracker.getFailedRequiredCount()));
/* 229 */         this.testTracker.getFailedRequired().forEach(GameTestServer::logFailedTest);
/*     */       } else {
/* 231 */         LOGGER.info("All {} required tests passed :)", Integer.valueOf(this.testTracker.getTotalCount()));
/*     */       } 
/* 233 */       if (this.testTracker.hasFailedOptional()) {
/* 234 */         LOGGER.info("{} optional tests failed", Integer.valueOf(this.testTracker.getFailedOptionalCount()));
/* 235 */         this.testTracker.getFailedOptional().forEach(GameTestServer::logFailedTest);
/*     */       } 
/* 237 */       LOGGER.info("====================================================");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void logFailedTest(GameTestInfo testInfo) {
/* 242 */     if (testInfo.getRotation() != Rotation.NONE) {
/* 243 */       LOGGER.info("   - {} with rotation {}: {}", new Object[] { testInfo.id(), testInfo.getRotation().getSerializedName(), testInfo.getError().getDescription().getString() });
/*     */     } else {
/* 245 */       LOGGER.info("   - {}: {}", testInfo.id(), testInfo.getError().getDescription().getString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 251 */   protected SampleLogger getTickTimeLogger() { return this.sampleLogger; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   public boolean isTickTimeLoggingEnabled() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   protected void waitUntilNextTick() { runAllTasks(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 266 */     systemReport.setDetail("Type", "Game test server");
/* 267 */     return systemReport;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onServerExit() {
/* 272 */     super.onServerExit();
/* 273 */     LOGGER.info("Game test server shutting down");
/* 274 */     System.exit((this.testTracker != null) ? this.testTracker.getFailedRequiredCount() : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onServerCrash(CrashReport report) {
/* 279 */     super.onServerCrash(report);
/* 280 */     LOGGER.error("Game test server crashed\n{}", report.getFriendlyReport(ReportType.CRASH));
/* 281 */     System.exit(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startTests(ServerLevel level) {
/* 288 */     BlockPos startPos = new BlockPos(level.random.nextIntBetweenInclusive(-14999992, 14999992), -59, level.random.nextIntBetweenInclusive(-14999992, 14999992));
/*     */     
/* 290 */     level.setRespawnData(LevelData.RespawnData.of(level.dimension(), startPos, 0.0F, 0.0F));
/*     */ 
/*     */     
/* 293 */     GameTestRunner runner = GameTestRunner.Builder.fromBatches(this.testBatches, level).newStructureSpawner(new StructureGridSpawner(startPos, 8, false)).build();
/* 294 */     Collection<GameTestInfo> testInfos = runner.getTestInfos();
/* 295 */     this.testTracker = new MultipleTestTracker(testInfos);
/* 296 */     LOGGER.info("{} tests are now running at position {}!", Integer.valueOf(this.testTracker.getTotalCount()), startPos.toShortString());
/* 297 */     this.stopwatch.reset();
/* 298 */     this.stopwatch.start();
/* 299 */     runner.start();
/*     */   }
/*     */ 
/*     */   
/* 303 */   private boolean haveTestsStarted() { return (this.testTracker != null); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public boolean isHardcore() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   public LevelBasedPermissionSet operatorUserPermissions() { return LevelBasedPermissionSet.ALL; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   public PermissionSet getFunctionCompilationPermissions() { return LevelBasedPermissionSet.OWNER; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   public boolean shouldRconBroadcast() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 328 */   public boolean isDedicatedServer() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 333 */   public int getRateLimitPacketsPerSecond() { return 0; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   public boolean useNativeTransport() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 343 */   public boolean isPublished() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 348 */   public boolean shouldInformAdmins() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   public boolean isSingleplayerOwner(NameAndId nameAndId) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 358 */   public int getMaxPlayers() { return 1; }
/*     */   
/*     */   private static class MockUserNameToIdResolver
/*     */     implements UserNameToIdResolver {
/* 362 */     private final Set<NameAndId> savedIds = new HashSet();
/*     */ 
/*     */ 
/*     */     
/* 366 */     public void add(NameAndId nameAndId) { this.savedIds.add(nameAndId); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     public Optional<NameAndId> get(String name) { return this.savedIds.stream().filter(e -> e.name().equals(name)).findFirst()
/* 372 */         .or(() -> Optional.of(NameAndId.createOffline(name))); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     public Optional<NameAndId> get(UUID id) { return this.savedIds.stream().filter(e -> e.id().equals(id)).findFirst(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void resolveOfflineUsers(boolean value) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void save() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class MockProfileResolver
/*     */     implements ProfileResolver
/*     */   {
/* 394 */     public Optional<GameProfile> fetchByName(String name) { return Optional.empty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 399 */     public Optional<GameProfile> fetchById(UUID id) { return Optional.empty(); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */