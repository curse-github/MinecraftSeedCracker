/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.net.HostAndPort;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Writer;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.ConsoleInput;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.gui.MinecraftServerGui;
/*     */ import net.minecraft.server.jsonrpc.JsonRpcNotificationService;
/*     */ import net.minecraft.server.jsonrpc.ManagementServer;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.security.AuthenticationHandler;
/*     */ import net.minecraft.server.jsonrpc.security.JsonRpcSslContextProvider;
/*     */ import net.minecraft.server.jsonrpc.security.SecurityConfig;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*     */ import net.minecraft.server.network.ServerTextFilter;
/*     */ import net.minecraft.server.network.TextFilter;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.rcon.RconConsoleSource;
/*     */ import net.minecraft.server.rcon.thread.QueryThreadGs4;
/*     */ import net.minecraft.server.rcon.thread.RconThread;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*     */ import net.minecraft.util.debugchart.RemoteSampleLogger;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*     */ import net.minecraft.util.monitoring.jmx.MinecraftServerStatistics;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedServer
/*     */   extends MinecraftServer implements ServerInterface {
/*  80 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int CONVERSION_RETRY_DELAY_MS = 5000;
/*     */   private static final int CONVERSION_RETRIES = 2;
/*  84 */   private final List<ConsoleInput> consoleInput = Collections.synchronizedList(Lists.newArrayList());
/*     */   private QueryThreadGs4 queryThreadGs4;
/*     */   private final RconConsoleSource rconConsoleSource;
/*     */   private RconThread rconThread;
/*     */   private final DedicatedServerSettings settings;
/*     */   private MinecraftServerGui gui;
/*     */   private final ServerTextFilter serverTextFilter;
/*     */   private RemoteSampleLogger tickTimeLogger;
/*     */   private boolean isTickTimeLoggingEnabled;
/*     */   private final ServerLinks serverLinks;
/*     */   private final Map<String, String> codeOfConductTexts;
/*     */   private ManagementServer jsonRpcServer;
/*     */   private long lastHeartbeat;
/*     */   
/*     */   public DedicatedServer(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, WorldStem worldStem, DedicatedServerSettings settings, DataFixer fixerUpper, Services services) {
/*  99 */     super(serverThread, levelStorageSource, packRepository, worldStem, Proxy.NO_PROXY, fixerUpper, services, LoggingLevelLoadListener.forDedicatedServer());
/* 100 */     this.settings = settings;
/* 101 */     this.rconConsoleSource = new RconConsoleSource(this);
/* 102 */     this.serverTextFilter = ServerTextFilter.createFromConfig(settings.getProperties());
/*     */     
/* 104 */     this.serverLinks = createServerLinks(settings);
/* 105 */     if ((settings.getProperties()).codeOfConduct) {
/* 106 */       this.codeOfConductTexts = readCodeOfConducts();
/*     */     } else {
/* 108 */       this.codeOfConductTexts = Map.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Map<String, String> readCodeOfConducts() {
/* 113 */     path = Path.of("codeofconduct", new String[0]);
/* 114 */     if (!Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 115 */       throw new IllegalArgumentException("Code of Conduct folder does not exist: " + String.valueOf(path));
/*     */     }
/*     */     try {
/* 118 */       ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/* 119 */       Stream<Path> files = Files.list(path); 
/* 120 */       try { for (Path file : files.toList()) {
/* 121 */           String filename = file.getFileName().toString();
/* 122 */           if (filename.endsWith(".txt")) {
/* 123 */             String language = filename.substring(0, filename.length() - 4).toLowerCase(Locale.ROOT);
/* 124 */             if (!file.toRealPath(new LinkOption[0]).getParent().equals(path.toAbsolutePath())) {
/* 125 */               throw new IllegalArgumentException("Failed to read Code of Conduct file \"" + filename + "\" because it links to a file outside the allowed directory");
/*     */             }
/*     */             try {
/* 128 */               String codeOfConduct = String.join("\n", Files.readAllLines(file, StandardCharsets.UTF_8));
/* 129 */               builder.put(language, StringUtil.stripColor(codeOfConduct));
/* 130 */             } catch (IOException e) {
/* 131 */               throw new IllegalArgumentException("Failed to read Code of Conduct file " + filename, e);
/*     */             } 
/*     */           } 
/*     */         } 
/* 135 */         if (files != null) files.close();  } catch (Throwable throwable) { if (files != null)
/* 136 */           try { files.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return builder.build();
/* 137 */     } catch (IOException e) {
/* 138 */       throw new IllegalArgumentException("Failed to read Code of Conduct folder", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private SslContext createSslContext() {
/*     */     try {
/* 144 */       return JsonRpcSslContextProvider.createFrom(
/* 145 */           (getProperties()).managementServerTlsKeystore, 
/* 146 */           (getProperties()).managementServerTlsKeystorePassword);
/* 147 */     } catch (Exception e) {
/* 148 */       JsonRpcSslContextProvider.printInstructions();
/* 149 */       throw new IllegalStateException("Failed to configure TLS for the server management protocol", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean initServer() throws IOException {
/* 155 */     int managementPort = (getProperties()).managementServerPort;
/* 156 */     if ((getProperties()).managementServerEnabled) {
/*     */       
/* 158 */       String managementServerSecret = (this.settings.getProperties()).managementServerSecret;
/* 159 */       if (!SecurityConfig.isValid(managementServerSecret)) {
/* 160 */         throw new IllegalStateException("Invalid management server secret, must be 40 alphanumeric characters");
/*     */       }
/*     */       
/* 163 */       String managementHost = (getProperties()).managementServerHost;
/* 164 */       HostAndPort hostAndPort = HostAndPort.fromParts(managementHost, managementPort);
/* 165 */       SecurityConfig securityConfig = new SecurityConfig(managementServerSecret);
/* 166 */       String allowedOrigins = (getProperties()).managementServerAllowedOrigins;
/* 167 */       AuthenticationHandler authenticationHandler = new AuthenticationHandler(securityConfig, allowedOrigins);
/* 168 */       LOGGER.info("Starting json RPC server on {}", hostAndPort);
/* 169 */       this.jsonRpcServer = new ManagementServer(hostAndPort, authenticationHandler);
/* 170 */       MinecraftApi minecraftApi = MinecraftApi.of(this);
/* 171 */       minecraftApi.notificationManager().registerService(new JsonRpcNotificationService(minecraftApi, this.jsonRpcServer));
/*     */       
/* 173 */       if ((getProperties()).managementServerTlsEnabled) {
/* 174 */         SslContext sslContext = createSslContext();
/* 175 */         this.jsonRpcServer.startWithTls(minecraftApi, sslContext);
/*     */       } else {
/* 177 */         this.jsonRpcServer.startWithoutTls(minecraftApi);
/*     */       } 
/*     */     } 
/* 180 */     Thread consoleThread = new Thread("Server console handler")
/*     */       {
/*     */         public void run() {
/* 183 */           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
/*     */           try {
/*     */             String line;
/* 186 */             while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning() && (line = reader.readLine()) != null) {
/* 187 */               DedicatedServer.this.handleConsoleInput(line, DedicatedServer.this.createCommandSourceStack());
/*     */             }
/* 189 */           } catch (IOException e) {
/* 190 */             DedicatedServer.LOGGER.error("Exception handling console input", e);
/*     */           } 
/*     */         }
/*     */       };
/* 194 */     consoleThread.setDaemon(true);
/* 195 */     consoleThread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
/* 196 */     consoleThread.start();
/*     */     
/* 198 */     LOGGER.info("Starting minecraft server version {}", SharedConstants.getCurrentVersion().name());
/*     */     
/* 200 */     if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
/* 201 */       LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
/*     */     }
/*     */     
/* 204 */     LOGGER.info("Loading properties");
/* 205 */     DedicatedServerProperties properties = this.settings.getProperties();
/*     */     
/* 207 */     if (isSingleplayer()) {
/* 208 */       setLocalIp("127.0.0.1");
/*     */     } else {
/* 210 */       setUsesAuthentication(properties.onlineMode);
/* 211 */       setPreventProxyConnections(properties.preventProxyConnections);
/* 212 */       setLocalIp(properties.serverIp);
/*     */     } 
/*     */ 
/*     */     
/* 216 */     this.worldData.setGameType((GameType)properties.gameMode.get());
/* 217 */     LOGGER.info("Default game type: {}", properties.gameMode.get());
/*     */     
/* 219 */     InetAddress localAddress = null;
/* 220 */     if (!getLocalIp().isEmpty()) {
/* 221 */       localAddress = InetAddress.getByName(getLocalIp());
/*     */     }
/* 223 */     if (getPort() < 0) {
/* 224 */       setPort(properties.serverPort);
/*     */     }
/*     */     
/* 227 */     initializeKeyPair();
/*     */     
/* 229 */     LOGGER.info("Starting Minecraft server on {}:{}", getLocalIp().isEmpty() ? "*" : getLocalIp(), Integer.valueOf(getPort()));
/*     */     try {
/* 231 */       getConnection().startTcpServerListener(localAddress, getPort());
/* 232 */     } catch (IOException e) {
/* 233 */       LOGGER.warn("**** FAILED TO BIND TO PORT!");
/* 234 */       LOGGER.warn("The exception was: {}", e.toString());
/* 235 */       LOGGER.warn("Perhaps a server is already running on that port?");
/* 236 */       return false;
/*     */     } 
/*     */     
/* 239 */     if (!usesAuthentication()) {
/* 240 */       LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
/* 241 */       LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
/* 242 */       LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
/* 243 */       LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
/*     */     } 
/*     */     
/* 246 */     if (convertOldUsers()) {
/* 247 */       this.services.nameToIdCache().save();
/*     */     }
/* 249 */     if (!OldUsersConverter.serverReadyAfterUserconversion(this)) {
/* 250 */       return false;
/*     */     }
/*     */     
/* 253 */     setPlayerList(new DedicatedPlayerList(this, registries(), this.playerDataStorage));
/* 254 */     this.tickTimeLogger = new RemoteSampleLogger(TpsDebugDimensions.values().length, debugSubscribers(), RemoteDebugSampleType.TICK_TIME);
/*     */     
/* 256 */     long levelNanoTime = Util.getNanos();
/*     */     
/* 258 */     this.services.nameToIdCache().resolveOfflineUsers(!usesAuthentication());
/*     */     
/* 260 */     LOGGER.info("Preparing level \"{}\"", getLevelIdName());
/* 261 */     loadLevel();
/* 262 */     long elapsed = Util.getNanos() - levelNanoTime;
/* 263 */     String time = String.format(Locale.ROOT, "%.3fs", new Object[] { Double.valueOf(elapsed / 1.0E9D) });
/* 264 */     LOGGER.info("Done ({})! For help, type \"help\"", time);
/*     */ 
/*     */     
/* 267 */     if (properties.announcePlayerAchievements != null) {
/* 268 */       this.worldData.getGameRules().set(GameRules.SHOW_ADVANCEMENT_MESSAGES, properties.announcePlayerAchievements, this);
/*     */     }
/*     */     
/* 271 */     if (properties.enableQuery) {
/* 272 */       LOGGER.info("Starting GS4 status listener");
/* 273 */       this.queryThreadGs4 = QueryThreadGs4.create(this);
/*     */     } 
/* 275 */     if (properties.enableRcon) {
/* 276 */       LOGGER.info("Starting remote control listener");
/* 277 */       this.rconThread = RconThread.create(this);
/*     */     } 
/*     */     
/* 280 */     if (getMaxTickLength() > 0L) {
/* 281 */       Thread watchdog = new Thread(new ServerWatchdog(this));
/* 282 */       watchdog.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 283 */       watchdog.setName("Server Watchdog");
/* 284 */       watchdog.setDaemon(true);
/* 285 */       watchdog.start();
/*     */     } 
/*     */     
/* 288 */     if (properties.enableJmxMonitoring) {
/* 289 */       MinecraftServerStatistics.registerJmxMonitoring(this);
/* 290 */       LOGGER.info("JMX monitoring enabled");
/*     */     } 
/* 292 */     notificationManager().serverStarted();
/* 293 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 298 */   public boolean isEnforceWhitelist() throws IOException { return ((Boolean)(this.settings.getProperties()).enforceWhitelist.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 303 */   public void setEnforceWhitelist(boolean enforceWhitelist) { this.settings.update(p -> (DedicatedServerProperties)p.enforceWhitelist.update(registryAccess(), Boolean.valueOf(enforceWhitelist))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 308 */   public boolean isUsingWhitelist() throws IOException { return ((Boolean)(this.settings.getProperties()).whiteList.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 313 */   public void setUsingWhitelist(boolean usingWhitelist) { this.settings.update(p -> (DedicatedServerProperties)p.whiteList.update(registryAccess(), Boolean.valueOf(usingWhitelist))); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 318 */     super.tickServer(haveTime);
/* 319 */     if (this.jsonRpcServer != null) {
/* 320 */       this.jsonRpcServer.tick();
/*     */     }
/*     */     
/* 323 */     long millis = Util.getMillis();
/* 324 */     int heartbeatInterval = statusHeartbeatInterval();
/* 325 */     if (heartbeatInterval > 0) {
/* 326 */       long intervalMillis = heartbeatInterval * TimeUtil.MILLISECONDS_PER_SECOND;
/* 327 */       if (millis - this.lastHeartbeat >= intervalMillis) {
/* 328 */         this.lastHeartbeat = millis;
/* 329 */         notificationManager().statusHeartbeat();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean saveAllChunks(boolean silent, boolean flush, boolean force) {
/* 336 */     notificationManager().serverSaveStarted();
/* 337 */     boolean savedChunks = super.saveAllChunks(silent, flush, force);
/* 338 */     notificationManager().serverSaveCompleted();
/* 339 */     return savedChunks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 344 */   public boolean allowFlight() throws IOException { return ((Boolean)(this.settings.getProperties()).allowFlight.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 348 */   public void setAllowFlight(boolean allowed) { this.settings.update(p -> (DedicatedServerProperties)p.allowFlight.update(registryAccess(), Boolean.valueOf(allowed))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 353 */   public DedicatedServerProperties getProperties() { return this.settings.getProperties(); }
/*     */ 
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 357 */     this.settings.update(p -> (DedicatedServerProperties)p.difficulty.update(registryAccess(), difficulty));
/* 358 */     forceDifficulty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 363 */   protected void forceDifficulty() { setDifficulty((Difficulty)(getProperties()).difficulty.get(), true); }
/*     */ 
/*     */ 
/*     */   
/* 367 */   public int viewDistance() { return ((Integer)(this.settings.getProperties()).viewDistance.get()).intValue(); }
/*     */ 
/*     */   
/*     */   public void setViewDistance(int viewDistance) {
/* 371 */     this.settings.update(p -> (DedicatedServerProperties)p.viewDistance.update(registryAccess(), Integer.valueOf(viewDistance)));
/* 372 */     getPlayerList().setViewDistance(viewDistance);
/*     */   }
/*     */ 
/*     */   
/* 376 */   public int simulationDistance() { return ((Integer)(this.settings.getProperties()).simulationDistance.get()).intValue(); }
/*     */ 
/*     */   
/*     */   public void setSimulationDistance(int simulationDistance) {
/* 380 */     this.settings.update(p -> (DedicatedServerProperties)p.simulationDistance.update(registryAccess(), Integer.valueOf(simulationDistance)));
/* 381 */     getPlayerList().setSimulationDistance(simulationDistance);
/*     */   }
/*     */ 
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 386 */     systemReport.setDetail("Is Modded", () -> getModdedStatus().fullDescription());
/* 387 */     systemReport.setDetail("Type", () -> "Dedicated Server");
/*     */     
/* 389 */     return systemReport;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dumpServerProperties(Path path) throws IOException {
/* 394 */     DedicatedServerProperties serverProperties = getProperties();
/*     */     
/* 396 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]); 
/* 397 */     try { output.write(String.format(Locale.ROOT, "sync-chunk-writes=%s%n", new Object[] { Boolean.valueOf(serverProperties.syncChunkWrites) }));
/* 398 */       output.write(String.format(Locale.ROOT, "gamemode=%s%n", new Object[] { serverProperties.gameMode.get() }));
/* 399 */       output.write(String.format(Locale.ROOT, "entity-broadcast-range-percentage=%d%n", new Object[] { serverProperties.entityBroadcastRangePercentage.get() }));
/* 400 */       output.write(String.format(Locale.ROOT, "max-world-size=%d%n", new Object[] { Integer.valueOf(serverProperties.maxWorldSize) }));
/* 401 */       output.write(String.format(Locale.ROOT, "view-distance=%d%n", new Object[] { serverProperties.viewDistance.get() }));
/* 402 */       output.write(String.format(Locale.ROOT, "simulation-distance=%d%n", new Object[] { serverProperties.simulationDistance.get() }));
/* 403 */       output.write(String.format(Locale.ROOT, "generate-structures=%s%n", new Object[] { Boolean.valueOf(serverProperties.worldOptions.generateStructures()) }));
/* 404 */       output.write(String.format(Locale.ROOT, "use-native=%s%n", new Object[] { Boolean.valueOf(serverProperties.useNativeTransport) }));
/* 405 */       output.write(String.format(Locale.ROOT, "rate-limit=%d%n", new Object[] { Integer.valueOf(serverProperties.rateLimitPacketsPerSecond) }));
/* 406 */       if (output != null) output.close();  }
/*     */     catch (Throwable throwable) { if (output != null)
/*     */         try { output.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 411 */      } protected void onServerExit() { if (this.serverTextFilter != null) {
/* 412 */       this.serverTextFilter.close();
/*     */     }
/*     */     
/* 415 */     if (this.gui != null) {
/* 416 */       this.gui.close();
/*     */     }
/*     */     
/* 419 */     if (this.rconThread != null) {
/* 420 */       this.rconThread.stop();
/*     */     }
/*     */     
/* 423 */     if (this.queryThreadGs4 != null) {
/* 424 */       this.queryThreadGs4.stop();
/*     */     }
/*     */     
/* 427 */     if (this.jsonRpcServer != null) {
/*     */       try {
/* 429 */         this.jsonRpcServer.stop(true);
/* 430 */       } catch (InterruptedException e) {
/* 431 */         LOGGER.error("Interrupted while stopping the management server", e);
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickConnection() {
/* 438 */     super.tickConnection();
/* 439 */     handleConsoleInputs();
/*     */   }
/*     */ 
/*     */   
/* 443 */   public void handleConsoleInput(String msg, CommandSourceStack source) { this.consoleInput.add(new ConsoleInput(msg, source)); }
/*     */ 
/*     */   
/*     */   public void handleConsoleInputs() {
/* 447 */     while (!this.consoleInput.isEmpty()) {
/* 448 */       ConsoleInput input = (ConsoleInput)this.consoleInput.remove(0);
/* 449 */       getCommands().performPrefixedCommand(input.source, input.msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 455 */   public boolean isDedicatedServer() throws IOException { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 460 */   public int getRateLimitPacketsPerSecond() { return (getProperties()).rateLimitPacketsPerSecond; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 465 */   public boolean useNativeTransport() throws IOException { return (getProperties()).useNativeTransport; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 470 */   public DedicatedPlayerList getPlayerList() { return (DedicatedPlayerList)super.getPlayerList(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 475 */   public int getMaxPlayers() { return ((Integer)(this.settings.getProperties()).maxPlayers.get()).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 479 */   public void setMaxPlayers(int maxPlayers) { this.settings.update(p -> (DedicatedServerProperties)p.maxPlayers.update(registryAccess(), Integer.valueOf(maxPlayers))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 484 */   public boolean isPublished() throws IOException { return true; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 489 */   public String getServerIp() { return getLocalIp(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 494 */   public int getServerPort() { return getPort(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 499 */   public String getServerName() { return getMotd(); }
/*     */ 
/*     */   
/*     */   public void showGui() {
/* 503 */     if (this.gui == null) {
/* 504 */       this.gui = MinecraftServerGui.showFrameFor(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 509 */   public int spawnProtectionRadius() { return ((Integer)(getProperties()).spawnProtection.get()).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 513 */   public void setSpawnProtectionRadius(int spawnProtectionRadius) { this.settings.update(p -> (DedicatedServerProperties)p.spawnProtection.update(registryAccess(), Integer.valueOf(spawnProtectionRadius))); }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnderSpawnProtection(ServerLevel level, BlockPos pos, Player player) {
/* 518 */     LevelData.RespawnData respawnData = level.getRespawnData();
/* 519 */     if (level.dimension() != respawnData.dimension()) {
/* 520 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 524 */     if (getPlayerList().getOps().isEmpty()) {
/* 525 */       return false;
/*     */     }
/* 527 */     if (getPlayerList().isOp(player.nameAndId())) {
/* 528 */       return false;
/*     */     }
/* 530 */     if (spawnProtectionRadius() <= 0) {
/* 531 */       return false;
/*     */     }
/*     */     
/* 534 */     BlockPos spawnPos = respawnData.pos();
/* 535 */     int xd = Mth.abs(pos.getX() - spawnPos.getX());
/* 536 */     int zd = Mth.abs(pos.getZ() - spawnPos.getZ());
/* 537 */     int dist = Math.max(xd, zd);
/*     */     
/* 539 */     return (dist <= spawnProtectionRadius());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 544 */   public boolean repliesToStatus() throws IOException { return ((Boolean)(getProperties()).enableStatus.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 548 */   public void setRepliesToStatus(boolean enable) { this.settings.update(p -> (DedicatedServerProperties)p.enableStatus.update(registryAccess(), Boolean.valueOf(enable))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 553 */   public boolean hidesOnlinePlayers() throws IOException { return ((Boolean)(getProperties()).hideOnlinePlayers.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 557 */   public void setHidesOnlinePlayers(boolean hide) { this.settings.update(p -> (DedicatedServerProperties)p.hideOnlinePlayers.update(registryAccess(), Boolean.valueOf(hide))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 562 */   public LevelBasedPermissionSet operatorUserPermissions() { return (LevelBasedPermissionSet)(getProperties()).opPermissions.get(); }
/*     */ 
/*     */ 
/*     */   
/* 566 */   public void setOperatorUserPermissions(LevelBasedPermissionSet permissions) { this.settings.update(p -> (DedicatedServerProperties)p.opPermissions.update(registryAccess(), permissions)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 571 */   public PermissionSet getFunctionCompilationPermissions() { return (getProperties()).functionPermissions; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 576 */   public int playerIdleTimeout() { return ((Integer)(this.settings.getProperties()).playerIdleTimeout.get()).intValue(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 581 */   public void setPlayerIdleTimeout(int playerIdleTimeout) { this.settings.update(p -> (DedicatedServerProperties)p.playerIdleTimeout.update(registryAccess(), Integer.valueOf(playerIdleTimeout))); }
/*     */ 
/*     */ 
/*     */   
/* 585 */   public int statusHeartbeatInterval() { return ((Integer)(this.settings.getProperties()).statusHeartbeatInterval.get()).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 589 */   public void setStatusHeartbeatInterval(int statusHeartbeatInterval) { this.settings.update(p -> (DedicatedServerProperties)p.statusHeartbeatInterval.update(registryAccess(), Integer.valueOf(statusHeartbeatInterval))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 594 */   public String getMotd() { return (String)(this.settings.getProperties()).motd.get(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 599 */   public void setMotd(String motd) { this.settings.update(p -> (DedicatedServerProperties)p.motd.update(registryAccess(), motd)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 604 */   public boolean shouldRconBroadcast() throws IOException { return (getProperties()).broadcastRconToOps; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 609 */   public boolean shouldInformAdmins() throws IOException { return (getProperties()).broadcastConsoleToOps; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 614 */   public int getAbsoluteMaxWorldSize() { return (getProperties()).maxWorldSize; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 619 */   public int getCompressionThreshold() { return (getProperties()).networkCompressionThreshold; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean enforceSecureProfile() throws IOException {
/* 624 */     DedicatedServerProperties properties = getProperties();
/* 625 */     return (properties.enforceSecureProfile && properties.onlineMode && this.services.canValidateProfileKeys());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 630 */   public boolean logIPs() throws IOException { return (getProperties()).logIPs; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean convertOldUsers() throws IOException {
/* 636 */     boolean userBanlistConverted = false;
/* 637 */     int retries = 0;
/* 638 */     while (!userBanlistConverted && retries <= 2) {
/* 639 */       if (retries > 0) {
/* 640 */         LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
/* 641 */         waitForRetry();
/*     */       } 
/* 643 */       userBanlistConverted = OldUsersConverter.convertUserBanlist(this);
/* 644 */       retries++;
/*     */     } 
/*     */     
/* 647 */     boolean ipBanlistConverted = false;
/* 648 */     retries = 0;
/* 649 */     while (!ipBanlistConverted && retries <= 2) {
/* 650 */       if (retries > 0) {
/* 651 */         LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
/* 652 */         waitForRetry();
/*     */       } 
/* 654 */       ipBanlistConverted = OldUsersConverter.convertIpBanlist(this);
/* 655 */       retries++;
/*     */     } 
/*     */     
/* 658 */     boolean opListConverted = false;
/* 659 */     retries = 0;
/* 660 */     while (!opListConverted && retries <= 2) {
/* 661 */       if (retries > 0) {
/* 662 */         LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
/* 663 */         waitForRetry();
/*     */       } 
/* 665 */       opListConverted = OldUsersConverter.convertOpsList(this);
/* 666 */       retries++;
/*     */     } 
/*     */     
/* 669 */     boolean whitelistConverted = false;
/* 670 */     retries = 0;
/* 671 */     while (!whitelistConverted && retries <= 2) {
/* 672 */       if (retries > 0) {
/* 673 */         LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
/* 674 */         waitForRetry();
/*     */       } 
/* 676 */       whitelistConverted = OldUsersConverter.convertWhiteList(this);
/* 677 */       retries++;
/*     */     } 
/*     */     
/* 680 */     boolean playersConverted = false;
/* 681 */     retries = 0;
/* 682 */     while (!playersConverted && retries <= 2) {
/* 683 */       if (retries > 0) {
/* 684 */         LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
/* 685 */         waitForRetry();
/*     */       } 
/* 687 */       playersConverted = OldUsersConverter.convertPlayers(this);
/* 688 */       retries++;
/*     */     } 
/*     */     
/* 691 */     return (userBanlistConverted || ipBanlistConverted || opListConverted || whitelistConverted || playersConverted);
/*     */   }
/*     */ 
/*     */   
/*     */   private void waitForRetry() { try {
/* 696 */       Thread.sleep(5000L);
/* 697 */     } catch (InterruptedException ignored) {
/*     */       return;
/*     */     }  }
/*     */ 
/*     */ 
/*     */   
/* 703 */   public long getMaxTickLength() { return (getProperties()).maxTickTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 708 */   public int getMaxChainedNeighborUpdates() { return (getProperties()).maxChainedNeighborUpdates; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 713 */   public String getPluginNames() { return ""; }
/*     */ 
/*     */ 
/*     */   
/*     */   public String runCommand(String command) {
/* 718 */     this.rconConsoleSource.prepareForCommand();
/* 719 */     executeBlocking(() -> getCommands().performPrefixedCommand(this.rconConsoleSource.createCommandSourceStack(), command));
/* 720 */     return this.rconConsoleSource.getCommandResponse();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stopServer() {
/* 725 */     notificationManager().serverShuttingDown();
/* 726 */     super.stopServer();
/* 727 */     Util.shutdownExecutors();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 732 */   public boolean isSingleplayerOwner(NameAndId nameAndId) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 737 */   public int getScaledTrackingDistance(int range) { return entityBroadcastRangePercentage() * range / 100; }
/*     */ 
/*     */ 
/*     */   
/* 741 */   public int entityBroadcastRangePercentage() { return ((Integer)(getProperties()).entityBroadcastRangePercentage.get()).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 745 */   public void setEntityBroadcastRangePercentage(int range) { this.settings.update(p -> (DedicatedServerProperties)p.entityBroadcastRangePercentage.update(registryAccess(), Integer.valueOf(range))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 750 */   public String getLevelIdName() { return this.storageSource.getLevelId(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 755 */   public boolean forceSynchronousWrites() throws IOException { return (this.settings.getProperties()).syncChunkWrites; }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextFilter createTextFilterForPlayer(ServerPlayer player) {
/* 760 */     if (this.serverTextFilter != null) {
/* 761 */       return this.serverTextFilter.createContext(player.getGameProfile());
/*     */     }
/* 763 */     return TextFilter.DUMMY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 768 */   public GameType getForcedGameType() { return forceGameMode() ? this.worldData.getGameType() : null; }
/*     */ 
/*     */ 
/*     */   
/* 772 */   public boolean forceGameMode() throws IOException { return ((Boolean)(this.settings.getProperties()).forceGameMode.get()).booleanValue(); }
/*     */ 
/*     */   
/*     */   public void setForceGameMode(boolean forceGameMode) {
/* 776 */     this.settings.update(p -> (DedicatedServerProperties)p.forceGameMode.update(registryAccess(), Boolean.valueOf(forceGameMode)));
/* 777 */     enforceGameTypeForPlayers(getForcedGameType());
/*     */   }
/*     */ 
/*     */   
/* 781 */   public GameType gameMode() { return (GameType)(getProperties()).gameMode.get(); }
/*     */ 
/*     */   
/*     */   public void setGameMode(GameType gameMode) {
/* 785 */     this.settings.update(p -> (DedicatedServerProperties)p.gameMode.update(registryAccess(), gameMode));
/* 786 */     this.worldData.setGameType(gameMode());
/* 787 */     enforceGameTypeForPlayers(getForcedGameType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 792 */   public Optional<MinecraftServer.ServerResourcePackInfo> getServerResourcePack() { return (this.settings.getProperties()).serverResourcePackInfo; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endMetricsRecordingTick() {
/* 797 */     super.endMetricsRecordingTick();
/*     */     
/* 799 */     this.isTickTimeLoggingEnabled = debugSubscribers().hasAnySubscriberFor(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 804 */   protected SampleLogger getTickTimeLogger() { return this.tickTimeLogger; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 809 */   public boolean isTickTimeLoggingEnabled() throws IOException { return this.isTickTimeLoggingEnabled; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 814 */   public boolean acceptsTransfers() throws IOException { return ((Boolean)(this.settings.getProperties()).acceptsTransfers.get()).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 818 */   public void setAcceptsTransfers(boolean acceptTransfers) { this.settings.update(p -> (DedicatedServerProperties)p.acceptsTransfers.update(registryAccess(), Boolean.valueOf(acceptTransfers))); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 823 */   public ServerLinks serverLinks() { return this.serverLinks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 828 */   public int pauseWhenEmptySeconds() { return ((Integer)(this.settings.getProperties()).pauseWhenEmptySeconds.get()).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 832 */   public void setPauseWhenEmptySeconds(int seconds) { this.settings.update(p -> (DedicatedServerProperties)p.pauseWhenEmptySeconds.update(registryAccess(), Integer.valueOf(seconds))); }
/*     */ 
/*     */   
/*     */   private static ServerLinks createServerLinks(DedicatedServerSettings settings) {
/* 836 */     Optional<URI> bugReportLink = parseBugReportLink(settings.getProperties());
/* 837 */     return (ServerLinks)bugReportLink.map(bugLink -> new ServerLinks(List.of(ServerLinks.KnownLinkType.BUG_REPORT.create(bugLink)))).orElse(ServerLinks.EMPTY);
/*     */   }
/*     */   
/*     */   private static Optional<URI> parseBugReportLink(DedicatedServerProperties properties) {
/* 841 */     String bugReportLink = properties.bugReportLink;
/* 842 */     if (bugReportLink.isEmpty()) {
/* 843 */       return Optional.empty();
/*     */     }
/*     */     try {
/* 846 */       return Optional.of(Util.parseAndValidateUntrustedUri(bugReportLink));
/* 847 */     } catch (Exception e) {
/* 848 */       LOGGER.warn("Failed to parse bug link {}", bugReportLink, e);
/* 849 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 855 */   public Map<String, String> getCodeOfConducts() { return this.codeOfConductTexts; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */