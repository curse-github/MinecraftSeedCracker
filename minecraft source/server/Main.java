/*     */ package net.minecraft.server;
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.Proxy;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import joptsimple.util.PathConverter;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import net.minecraft.server.dedicated.DedicatedServerSettings;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.profiling.jfr.Environment;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.util.worldupdate.WorldUpgrader;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Main {
/*  63 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */   public static void main(String[] args) {
/*  67 */     SharedConstants.tryDetectVersion();
/*     */     
/*  69 */     OptionParser parser = new OptionParser();
/*  70 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("nogui");
/*  71 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
/*  72 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("demo");
/*  73 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("bonusChest");
/*  74 */     OptionSpecBuilder optionSpecBuilder5 = parser.accepts("forceUpgrade");
/*  75 */     OptionSpecBuilder optionSpecBuilder6 = parser.accepts("eraseCache");
/*  76 */     OptionSpecBuilder optionSpecBuilder7 = parser.accepts("recreateRegionFiles");
/*  77 */     OptionSpecBuilder optionSpecBuilder8 = parser.accepts("safeMode", "Loads level with vanilla datapack only");
/*  78 */     AbstractOptionSpec abstractOptionSpec = parser.accepts("help").forHelp();
/*  79 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("universe").withRequiredArg().defaultsTo(".", new String[0]);
/*  80 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("world").withRequiredArg();
/*  81 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(-1), new Integer[0]);
/*  82 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = parser.accepts("serverId").withRequiredArg();
/*  83 */     OptionSpecBuilder optionSpecBuilder9 = parser.accepts("jfrProfile");
/*  84 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = parser.accepts("pidFile").withRequiredArg().withValuesConvertedBy(new PathConverter(new joptsimple.util.PathProperties[0]));
/*  85 */     NonOptionArgumentSpec nonOptionArgumentSpec = parser.nonOptions(); try {
/*     */       WorldStem worldStem;
/*     */       Dynamic<?> loadedDataTag;
/*  88 */       OptionSet options = parser.parse(args);
/*     */       
/*  90 */       if (options.has(abstractOptionSpec)) {
/*  91 */         parser.printHelpOn(System.err);
/*     */         
/*     */         return;
/*     */       } 
/*  95 */       Path pidFilePath = (Path)options.valueOf(argumentAcceptingOptionSpec5);
/*  96 */       if (pidFilePath != null) {
/*  97 */         writePidFile(pidFilePath);
/*     */       }
/*     */       
/* 100 */       CrashReport.preload();
/*     */ 
/*     */       
/* 103 */       if (options.has(optionSpecBuilder9)) {
/* 104 */         JvmProfiler.INSTANCE.start(Environment.SERVER);
/*     */       }
/*     */       
/* 107 */       Bootstrap.bootStrap();
/* 108 */       Bootstrap.validate();
/*     */       
/* 110 */       Util.startTimerHackThread();
/*     */       
/* 112 */       Path settingsFile = Paths.get("server.properties", new String[0]);
/* 113 */       DedicatedServerSettings settings = new DedicatedServerSettings(settingsFile);
/* 114 */       settings.forceSave();
/*     */       
/* 116 */       RegionFileVersion.configure((settings.getProperties()).regionFileComression);
/*     */       
/* 118 */       Path eulaFile = Paths.get("eula.txt", new String[0]);
/* 119 */       Eula eula = new Eula(eulaFile);
/*     */       
/* 121 */       if (options.has(optionSpecBuilder2)) {
/* 122 */         LOGGER.info("Initialized '{}' and '{}'", settingsFile.toAbsolutePath(), eulaFile.toAbsolutePath());
/*     */         
/*     */         return;
/*     */       } 
/* 126 */       if (!eula.hasAgreedToEULA()) {
/* 127 */         LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
/*     */         
/*     */         return;
/*     */       } 
/* 131 */       File universePath = new File((String)options.valueOf(argumentAcceptingOptionSpec1));
/* 132 */       Services services = Services.create(new YggdrasilAuthenticationService(Proxy.NO_PROXY), universePath);
/*     */       
/* 134 */       String levelName = (String)Optional.ofNullable((String)options.valueOf(argumentAcceptingOptionSpec2)).orElse((settings.getProperties()).levelName);
/* 135 */       LevelStorageSource levelStorageSource = LevelStorageSource.createDefault(universePath.toPath());
/* 136 */       LevelStorageSource.LevelStorageAccess access = levelStorageSource.validateAndCreateAccess(levelName);
/*     */ 
/*     */       
/* 139 */       if (access.hasWorldData()) {
/*     */         LevelSummary summary;
/*     */         try {
/* 142 */           loadedDataTag = access.getDataTag();
/* 143 */           summary = access.getSummary(loadedDataTag);
/* 144 */         } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 145 */           LevelStorageSource.LevelDirectory levelDirectory = access.getLevelDirectory();
/* 146 */           LOGGER.warn("Failed to load world data from {}", levelDirectory.dataFile(), e);
/* 147 */           LOGGER.info("Attempting to use fallback");
/*     */           try {
/* 149 */             loadedDataTag = access.getDataTagFallback();
/* 150 */             summary = access.getSummary(loadedDataTag);
/* 151 */           } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException ex) {
/* 152 */             LOGGER.error("Failed to load world data from {}", levelDirectory.oldDataFile(), worldStem);
/* 153 */             LOGGER.error("Failed to load world data from {} and {}. World files may be corrupted. Shutting down.", levelDirectory.dataFile(), levelDirectory.oldDataFile());
/*     */             return;
/*     */           } 
/* 156 */           access.restoreLevelDataFromOld();
/*     */         } 
/* 158 */         if (summary.requiresManualConversion()) {
/* 159 */           LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
/*     */           return;
/*     */         } 
/* 162 */         if (!summary.isCompatible()) {
/* 163 */           LOGGER.info("This world was created by an incompatible version.");
/*     */           return;
/*     */         } 
/*     */       } else {
/* 167 */         loadedDataTag = null;
/*     */       } 
/* 169 */       Dynamic<?> levelDataTag = loadedDataTag;
/*     */       
/* 171 */       boolean safeModeEnabled = options.has(optionSpecBuilder8);
/* 172 */       if (safeModeEnabled) {
/* 173 */         LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
/*     */       }
/*     */       
/* 176 */       PackRepository packRepository = ServerPacksSource.createPackRepository(access);
/*     */ 
/*     */       
/*     */       try {
/* 180 */         WorldLoader.InitConfig worldLoadConfig = loadOrCreateConfig(settings.getProperties(), levelDataTag, safeModeEnabled, packRepository);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 202 */         worldStem = (WorldStem)Util.blockUntilDone(executor -> WorldLoader.load(worldLoadConfig, (), WorldStem::new, Util.backgroundExecutor(), executor)).get();
/* 203 */       } catch (Exception e) {
/* 204 */         LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", e);
/*     */         
/*     */         return;
/*     */       } 
/* 208 */       RegistryAccess.Frozen registryHolder = worldStem.registries().compositeAccess();
/* 209 */       WorldData data = worldStem.worldData();
/*     */       
/* 211 */       boolean recreateRegionFilesValue = options.has(optionSpecBuilder7);
/* 212 */       if (options.has(optionSpecBuilder5) || recreateRegionFilesValue) {
/* 213 */         forceUpgrade(access, data, DataFixers.getDataFixer(), options.has(optionSpecBuilder6), () -> true, registryHolder, recreateRegionFilesValue);
/*     */       }
/*     */       
/* 216 */       access.saveDataTag(registryHolder, data);
/*     */       
/* 218 */       final DedicatedServer dedicatedServer = (DedicatedServer)MinecraftServer.spin(thread -> {
/* 219 */             DedicatedServer server = new DedicatedServer(thread, access, packRepository, worldStem, settings, DataFixers.getDataFixer(), services);
/*     */             
/* 221 */             server.setPort(((Integer)options.valueOf(port)).intValue());
/* 222 */             server.setDemo(options.has(demo));
/* 223 */             server.setId((String)options.valueOf(serverId));
/*     */             
/* 225 */             boolean gui = (!options.has(nogui) && !options.valuesOf(nonOptions).contains("nogui"));
/* 226 */             if (gui && !GraphicsEnvironment.isHeadless()) {
/* 227 */               server.showGui();
/*     */             }
/* 229 */             return server;
/*     */           });
/*     */       
/* 232 */       Thread shutdownThread = new Thread("Server Shutdown Thread")
/*     */         {
/*     */           public void run() {
/* 235 */             dedicatedServer.halt(true);
/*     */           }
/*     */         };
/* 238 */       shutdownThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 239 */       Runtime.getRuntime().addShutdownHook(shutdownThread);
/* 240 */     } catch (Throwable t) {
/* 241 */       LOGGER.error(LogUtils.FATAL_MARKER, "Failed to start the minecraft server", t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static WorldLoader.DataLoadOutput<WorldData> createNewWorldData(DedicatedServerSettings settings, WorldLoader.DataLoadContext context, Registry<LevelStem> datapackDimensions, boolean demoMode, boolean bonusChest) {
/*     */     WorldDimensions dimensions;
/*     */     WorldOptions worldOptions;
/*     */     LevelSettings createLevelSettings;
/* 249 */     if (demoMode) {
/* 250 */       createLevelSettings = MinecraftServer.DEMO_SETTINGS;
/* 251 */       worldOptions = WorldOptions.DEMO_OPTIONS;
/* 252 */       dimensions = WorldPresets.createNormalWorldDimensions(context.datapackWorldgen());
/*     */     } else {
/* 254 */       DedicatedServerProperties properties = settings.getProperties();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 262 */       createLevelSettings = new LevelSettings(properties.levelName, (GameType)properties.gameMode.get(), properties.hardcore, (Difficulty)properties.difficulty.get(), false, new GameRules(context.dataConfiguration().enabledFeatures()), context.dataConfiguration());
/*     */       
/* 264 */       worldOptions = bonusChest ? properties.worldOptions.withBonusChest(true) : properties.worldOptions;
/* 265 */       dimensions = properties.createDimensions(context.datapackWorldgen());
/*     */     } 
/* 267 */     WorldDimensions.Complete finalDimensions = dimensions.bake(datapackDimensions);
/* 268 */     Lifecycle lifecycle = finalDimensions.lifecycle().add(context.datapackWorldgen().allRegistriesLifecycle());
/* 269 */     return new WorldLoader.DataLoadOutput(new PrimaryLevelData(createLevelSettings, worldOptions, finalDimensions
/* 270 */           .specialWorldProperty(), lifecycle), finalDimensions
/* 271 */         .dimensionsRegistryAccess());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writePidFile(Path path) {
/*     */     try {
/* 277 */       long pid = ProcessHandle.current().pid();
/* 278 */       Files.writeString(path, Long.toString(pid), new java.nio.file.OpenOption[0]);
/* 279 */     } catch (IOException e) {
/* 280 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static WorldLoader.InitConfig loadOrCreateConfig(DedicatedServerProperties properties, Dynamic<?> levelDataTag, boolean safeModeEnabled, PackRepository packRepository) {
/*     */     WorldDataConfiguration dataConfigToUse;
/*     */     boolean initMode;
/* 287 */     if (levelDataTag != null) {
/* 288 */       WorldDataConfiguration storedConfiguration = LevelStorageSource.readDataConfig(levelDataTag);
/* 289 */       initMode = false;
/* 290 */       dataConfigToUse = storedConfiguration;
/*     */     } else {
/* 292 */       initMode = true;
/* 293 */       dataConfigToUse = new WorldDataConfiguration(properties.initialDataPackConfiguration, FeatureFlags.DEFAULT_FLAGS);
/*     */     } 
/* 295 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, dataConfigToUse, safeModeEnabled, initMode);
/* 296 */     return new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.DEDICATED, properties.functionPermissions);
/*     */   }
/*     */   
/*     */   private static void forceUpgrade(LevelStorageSource.LevelStorageAccess storageSource, WorldData worldData, DataFixer fixerUpper, boolean eraseCache, BooleanSupplier isRunning, RegistryAccess registryAccess, boolean recreateRegionFiles) {
/* 300 */     LOGGER.info("Forcing world upgrade!");
/*     */     
/* 302 */     WorldUpgrader upgrader = new WorldUpgrader(storageSource, fixerUpper, worldData, registryAccess, eraseCache, recreateRegionFiles); try {
/* 303 */       Component lastStatus = null;
/* 304 */       while (!upgrader.isFinished()) {
/* 305 */         Component status = upgrader.getStatus();
/* 306 */         if (lastStatus != status) {
/* 307 */           lastStatus = status;
/* 308 */           LOGGER.info(upgrader.getStatus().getString());
/*     */         } 
/* 310 */         int totalChunks = upgrader.getTotalChunks();
/* 311 */         if (totalChunks > 0) {
/* 312 */           int done = upgrader.getConverted() + upgrader.getSkipped();
/* 313 */           LOGGER.info("{}% completed ({} / {} chunks)...", new Object[] { Integer.valueOf(Mth.floor(done / totalChunks * 100.0F)), Integer.valueOf(done), Integer.valueOf(totalChunks) });
/*     */         } 
/*     */         
/* 316 */         if (!isRunning.getAsBoolean()) {
/* 317 */           upgrader.cancel(); continue;
/*     */         } 
/*     */         try {
/* 320 */           Thread.sleep(1000L);
/* 321 */         } catch (InterruptedException interruptedException) {}
/*     */       } 
/*     */ 
/*     */       
/* 325 */       upgrader.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         upgrader.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\Main.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */