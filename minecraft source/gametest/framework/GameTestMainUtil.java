/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestMainUtil
/*     */ {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_UNIVERSE_DIR = "gametestserver";
/*     */   private static final String LEVEL_NAME = "gametestworld";
/*  31 */   private static final OptionParser parser = new OptionParser();
/*  32 */   private static final OptionSpec<String> universe = parser.accepts("universe", "The path to where the test server world will be created. Any existing folder will be replaced.").withRequiredArg().defaultsTo("gametestserver", new String[0]);
/*  33 */   private static final OptionSpec<File> report = parser.accepts("report", "Exports results in a junit-like XML report at the given path.").withRequiredArg().ofType(File.class);
/*  34 */   private static final OptionSpec<String> tests = parser.accepts("tests", "Which test(s) to run (namespaced ID selector using wildcards). Empty means run all.").withRequiredArg();
/*  35 */   private static final OptionSpec<Boolean> verify = parser.accepts("verify", "Runs the tests specified with `test` or `testNamespace` 100 times for each 90 degree rotation step").withRequiredArg().ofType(Boolean.class).defaultsTo(Boolean.valueOf(false), new Boolean[0]);
/*  36 */   private static final OptionSpec<String> packs = parser.accepts("packs", "A folder of datapacks to include in the world").withRequiredArg();
/*  37 */   private static final OptionSpec<Void> help = parser.accepts("help").forHelp();
/*     */   
/*     */   @SuppressForbidden(reason = "Using System.err due to no bootstrap")
/*     */   public static void runGameTestServer(String[] args, Consumer<String> onUniverseCreated) throws Exception {
/*  41 */     parser.allowsUnrecognizedOptions();
/*     */     
/*  43 */     OptionSet options = parser.parse(args);
/*  44 */     if (options.has(help)) {
/*  45 */       parser.printHelpOn(System.err);
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     if (((Boolean)options.valueOf(verify)).booleanValue() && !options.has(tests)) {
/*  50 */       LOGGER.error("Please specify a test selection to run the verify option. For example: --verify --tests example:test_something_*");
/*  51 */       System.exit(-1);
/*     */     } 
/*     */     
/*  54 */     LOGGER.info("Running GameTestMain with cwd '{}', universe path '{}'", System.getProperty("user.dir"), options.valueOf(universe));
/*     */     
/*  56 */     if (options.has(report)) {
/*  57 */       GlobalTestReporter.replaceWith(new JUnitLikeTestReporter((File)report.value(options)));
/*     */     }
/*     */     
/*  60 */     Bootstrap.bootStrap();
/*  61 */     Util.startTimerHackThread();
/*     */     
/*  63 */     String universePath = (String)options.valueOf(universe);
/*  64 */     createOrResetDir(universePath);
/*  65 */     onUniverseCreated.accept(universePath);
/*  66 */     if (options.has(packs)) {
/*  67 */       String packFolder = (String)options.valueOf(packs);
/*  68 */       copyPacks(universePath, packFolder);
/*     */     } 
/*     */     
/*  71 */     LevelStorageSource.LevelStorageAccess levelStorageSource = LevelStorageSource.createDefault(Paths.get(universePath, new String[0])).createAccess("gametestworld");
/*  72 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelStorageSource);
/*  73 */     MinecraftServer.spin(thread -> GameTestServer.create(thread, levelStorageSource, packRepository, optionalFromOption(options, tests), options.has(verify)));
/*     */   }
/*     */ 
/*     */   
/*  77 */   private static Optional<String> optionalFromOption(OptionSet options, OptionSpec<String> option) { return options.has(option) ? Optional.of((String)options.valueOf(option)) : Optional.empty(); }
/*     */ 
/*     */   
/*     */   private static void createOrResetDir(String universePath) throws IOException {
/*  81 */     Path universeDir = Paths.get(universePath, new String[0]);
/*  82 */     if (Files.exists(universeDir, new java.nio.file.LinkOption[0])) {
/*  83 */       FileUtils.deleteDirectory(universeDir.toFile());
/*     */     }
/*  85 */     Files.createDirectories(universeDir, new java.nio.file.attribute.FileAttribute[0]);
/*     */   }
/*     */   
/*     */   private static void copyPacks(String serverPath, String packSourcePath) throws IOException {
/*  89 */     Path worldPackFolder = Paths.get(serverPath, new String[0]).resolve("gametestworld").resolve("datapacks");
/*  90 */     if (!Files.exists(worldPackFolder, new java.nio.file.LinkOption[0])) {
/*  91 */       Files.createDirectories(worldPackFolder, new java.nio.file.attribute.FileAttribute[0]);
/*     */     }
/*  93 */     Path sourceFolder = Paths.get(packSourcePath, new String[0]);
/*  94 */     if (Files.exists(sourceFolder, new java.nio.file.LinkOption[0])) {
/*  95 */       Stream<Path> list = Files.list(sourceFolder); try {
/*  96 */         for (Path path : list.toList()) {
/*  97 */           Path destination = worldPackFolder.resolve(path.getFileName());
/*  98 */           if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/*  99 */             if (Files.isRegularFile(path.resolve("pack.mcmeta"), new java.nio.file.LinkOption[0])) {
/* 100 */               FileUtils.copyDirectory(path.toFile(), destination.toFile());
/* 101 */               LOGGER.info("Included folder pack {}", path.getFileName());
/*     */             }  continue;
/* 103 */           }  if (path.toString().endsWith(".zip")) {
/* 104 */             Files.copy(path, destination, new java.nio.file.CopyOption[0]);
/* 105 */             LOGGER.info("Included zip pack {}", path.getFileName());
/*     */           } 
/*     */         } 
/* 108 */         if (list != null) list.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (list != null)
/*     */           try {
/*     */             list.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestMainUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */