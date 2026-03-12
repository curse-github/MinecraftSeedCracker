/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.jsonrpc.security.SecurityConfig;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedServerProperties
/*     */   extends Settings<DedicatedServerProperties>
/*     */ {
/*  52 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  53 */   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
/*  54 */   private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults();
/*     */   
/*     */   public static final String MANAGEMENT_SERVER_TLS_ENABLED_KEY = "management-server-tls-enabled";
/*     */   public static final String MANAGEMENT_SERVER_TLS_KEYSTORE_KEY = "management-server-tls-keystore";
/*     */   public static final String MANAGEMENT_SERVER_TLS_KEYSTORE_PASSWORD_KEY = "management-server-tls-keystore-password";
/*  59 */   public final boolean onlineMode = get("online-mode", true);
/*  60 */   public final boolean preventProxyConnections = get("prevent-proxy-connections", false);
/*  61 */   public final String serverIp = get("server-ip", "");
/*  62 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> allowFlight = getMutable("allow-flight", false);
/*  63 */   public final Settings<DedicatedServerProperties>.MutableValue<String> motd = getMutable("motd", "A Minecraft Server");
/*  64 */   public final boolean codeOfConduct = get("enable-code-of-conduct", false);
/*  65 */   public final String bugReportLink = get("bug-report-link", "");
/*  66 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> forceGameMode = getMutable("force-gamemode", false);
/*  67 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> enforceWhitelist = getMutable("enforce-whitelist", false);
/*  68 */   public final Settings<DedicatedServerProperties>.MutableValue<Difficulty> difficulty = getMutable("difficulty", dispatchNumberOrString(Difficulty::byId, Difficulty::byName), Difficulty::getKey, Difficulty.EASY);
/*  69 */   public final Settings<DedicatedServerProperties>.MutableValue<GameType> gameMode = getMutable("gamemode", dispatchNumberOrString(GameType::byId, GameType::byName), GameType::getName, GameType.SURVIVAL);
/*  70 */   public final String levelName = get("level-name", "world");
/*  71 */   public final int serverPort = get("server-port", 25565);
/*     */   
/*  73 */   public final boolean managementServerEnabled = get("management-server-enabled", false);
/*  74 */   public final String managementServerHost = get("management-server-host", "localhost");
/*  75 */   public final int managementServerPort = get("management-server-port", 0);
/*  76 */   public final String managementServerSecret = get("management-server-secret", SecurityConfig.generateSecretKey());
/*  77 */   public final boolean managementServerTlsEnabled = get("management-server-tls-enabled", true);
/*  78 */   public final String managementServerTlsKeystore = get("management-server-tls-keystore", "");
/*  79 */   public final String managementServerTlsKeystorePassword = get("management-server-tls-keystore-password", "");
/*  80 */   public final String managementServerAllowedOrigins = get("management-server-allowed-origins", "");
/*     */   
/*  82 */   public final Boolean announcePlayerAchievements = getLegacyBoolean("announce-player-achievements");
/*  83 */   public final boolean enableQuery = get("enable-query", false);
/*  84 */   public final int queryPort = get("query.port", 25565);
/*  85 */   public final boolean enableRcon = get("enable-rcon", false);
/*  86 */   public final int rconPort = get("rcon.port", 25575);
/*  87 */   public final String rconPassword = get("rcon.password", "");
/*  88 */   public final boolean hardcore = get("hardcore", false);
/*  89 */   public final boolean useNativeTransport = get("use-native-transport", true);
/*  90 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> spawnProtection = getMutable("spawn-protection", 16);
/*  91 */   public final Settings<DedicatedServerProperties>.MutableValue<LevelBasedPermissionSet> opPermissions = getMutable("op-permission-level", DedicatedServerProperties::deserializePermission, DedicatedServerProperties::serializePermission, LevelBasedPermissionSet.OWNER);
/*  92 */   public final LevelBasedPermissionSet functionPermissions = (LevelBasedPermissionSet)get("function-permission-level", DedicatedServerProperties::deserializePermission, DedicatedServerProperties::serializePermission, LevelBasedPermissionSet.GAMEMASTER);
/*  93 */   public final long maxTickTime = get("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
/*  94 */   public final int maxChainedNeighborUpdates = get("max-chained-neighbor-updates", 1000000);
/*  95 */   public final int rateLimitPacketsPerSecond = get("rate-limit", 0);
/*  96 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> viewDistance = getMutable("view-distance", 10);
/*  97 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> simulationDistance = getMutable("simulation-distance", 10);
/*  98 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> maxPlayers = getMutable("max-players", 20);
/*  99 */   public final int networkCompressionThreshold = get("network-compression-threshold", 256);
/* 100 */   public final boolean broadcastRconToOps = get("broadcast-rcon-to-ops", true);
/* 101 */   public final boolean broadcastConsoleToOps = get("broadcast-console-to-ops", true);
/*     */   
/*     */   public final int maxWorldSize;
/*     */   
/*     */   public final boolean syncChunkWrites;
/*     */   public final String regionFileComression;
/*     */   public final boolean enableJmxMonitoring;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> enableStatus;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> hideOnlinePlayers;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> entityBroadcastRangePercentage;
/*     */   public final String textFilteringConfig;
/*     */   public final int textFilteringVersion;
/*     */   public final Optional<MinecraftServer.ServerResourcePackInfo> serverResourcePackInfo;
/*     */   public final DataPackConfig initialDataPackConfiguration;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> playerIdleTimeout;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> statusHeartbeatInterval;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> whiteList;
/*     */   public final boolean enforceSecureProfile;
/*     */   public final boolean logIPs;
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> pauseWhenEmptySeconds;
/*     */   private final WorldDimensionData worldDimensionData;
/*     */   public final WorldOptions worldOptions;
/*     */   public Settings<DedicatedServerProperties>.MutableValue<Boolean> acceptsTransfers;
/*     */   
/*     */   public DedicatedServerProperties(Properties settings) {
/* 126 */     super(settings); this.maxWorldSize = get("max-world-size", v -> Integer.valueOf(Mth.clamp(v.intValue(), 1, 29999984)), 29999984); this.syncChunkWrites = get("sync-chunk-writes", true); this.regionFileComression = get("region-file-compression", "deflate"); this.enableJmxMonitoring = get("enable-jmx-monitoring", false); this.enableStatus = getMutable("enable-status", true); this.hideOnlinePlayers = getMutable("hide-online-players", false); this.entityBroadcastRangePercentage = getMutable("entity-broadcast-range-percentage", v -> Integer.valueOf(Mth.clamp(Integer.parseInt(v), 10, 1000)), Integer.valueOf(100)); this.textFilteringConfig = get("text-filtering-config", ""); this.textFilteringVersion = get("text-filtering-version", 0); this.playerIdleTimeout = getMutable("player-idle-timeout", 0); this.statusHeartbeatInterval = getMutable("status-heartbeat-interval", 0); this.whiteList = getMutable("white-list", false); this.enforceSecureProfile = get("enforce-secure-profile", true); this.logIPs = get("log-ips", true); this.pauseWhenEmptySeconds = getMutable("pause-when-empty-seconds", 60);
/*     */     this.acceptsTransfers = getMutable("accepts-transfers", false);
/* 128 */     String levelSeed = get("level-seed", "");
/* 129 */     boolean generateStructures = get("generate-structures", true);
/*     */     
/* 131 */     long seed = WorldOptions.parseSeed(levelSeed).orElse(WorldOptions.randomSeed());
/* 132 */     this.worldOptions = new WorldOptions(seed, generateStructures, false);
/*     */     
/* 134 */     this
/*     */       
/* 136 */       .worldDimensionData = new WorldDimensionData((JsonObject)get("generator-settings", s -> GsonHelper.parse(!s.isEmpty() ? s : "{}"), new JsonObject()), (String)get("level-type", v -> v.toLowerCase(Locale.ROOT), WorldPresets.NORMAL.identifier().toString()));
/*     */     
/* 138 */     this.serverResourcePackInfo = getServerPackInfo(
/* 139 */         get("resource-pack-id", ""), 
/* 140 */         get("resource-pack", ""), 
/* 141 */         get("resource-pack-sha1", ""), 
/* 142 */         getLegacyString("resource-pack-hash"), 
/* 143 */         get("require-resource-pack", false), 
/* 144 */         get("resource-pack-prompt", ""));
/*     */ 
/*     */     
/* 147 */     this.initialDataPackConfiguration = getDatapackConfig(
/* 148 */         get("initial-enabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getEnabled())), 
/* 149 */         get("initial-disabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getDisabled())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public static DedicatedServerProperties fromFile(Path file) { return new DedicatedServerProperties(loadFromFile(file)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   protected DedicatedServerProperties reload(RegistryAccess registryAccess, Properties properties) { return new DedicatedServerProperties(properties); }
/*     */ 
/*     */   
/*     */   private static Component parseResourcePackPrompt(String prompt) {
/* 163 */     if (!Strings.isNullOrEmpty(prompt)) {
/*     */       try {
/* 165 */         JsonElement element = StrictJsonParser.parse(prompt);
/* 166 */         return (Component)ComponentSerialization.CODEC.parse(RegistryAccess.EMPTY.createSerializationContext(JsonOps.INSTANCE), element)
/* 167 */           .resultOrPartial(msg -> LOGGER.warn("Failed to parse resource pack prompt '{}': {}", prompt, msg)).orElse(null);
/* 168 */       } catch (Exception e) {
/* 169 */         LOGGER.warn("Failed to parse resource pack prompt '{}'", prompt, e);
/*     */       } 
/*     */     }
/* 172 */     return null;
/*     */   } private static Optional<MinecraftServer.ServerResourcePackInfo> getServerPackInfo(String id, String url, String resourcePackSha1, String resourcePackHash, boolean requireResourcePack, String resourcePackPrompt) {
/*     */     UUID parsedId;
/*     */     String hash;
/* 176 */     if (url.isEmpty()) {
/* 177 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 181 */     if (!resourcePackSha1.isEmpty()) {
/* 182 */       hash = resourcePackSha1;
/* 183 */       if (!Strings.isNullOrEmpty(resourcePackHash)) {
/* 184 */         LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored.");
/*     */       }
/* 186 */     } else if (!Strings.isNullOrEmpty(resourcePackHash)) {
/* 187 */       LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
/* 188 */       hash = resourcePackHash;
/*     */     } else {
/* 190 */       hash = "";
/*     */     } 
/*     */     
/* 193 */     if (hash.isEmpty()) {
/* 194 */       LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
/* 195 */     } else if (!SHA1.matcher(hash).matches()) {
/* 196 */       LOGGER.warn("Invalid sha1 for resource-pack-sha1");
/*     */     } 
/*     */     
/* 199 */     Component prompt = parseResourcePackPrompt(resourcePackPrompt);
/*     */ 
/*     */     
/* 202 */     if (id.isEmpty()) {
/* 203 */       parsedId = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
/* 204 */       LOGGER.warn("resource-pack-id missing, using default of {}", parsedId);
/*     */     } else {
/*     */       try {
/* 207 */         parsedId = UUID.fromString(id);
/* 208 */       } catch (IllegalArgumentException e) {
/* 209 */         LOGGER.warn("Failed to parse '{}' into UUID", id);
/* 210 */         return Optional.empty();
/*     */       } 
/*     */     } 
/*     */     
/* 214 */     return Optional.of(new MinecraftServer.ServerResourcePackInfo(parsedId, url, hash, requireResourcePack, prompt));
/*     */   }
/*     */   
/*     */   private static DataPackConfig getDatapackConfig(String enabledPacks, String disabledPacks) {
/* 218 */     List<String> enabledPacksIds = COMMA_SPLITTER.splitToList(enabledPacks);
/* 219 */     List<String> disabledPacksIds = COMMA_SPLITTER.splitToList(disabledPacks);
/* 220 */     return new DataPackConfig(enabledPacksIds, disabledPacksIds);
/*     */   }
/*     */   
/*     */   public static LevelBasedPermissionSet deserializePermission(String value) {
/*     */     try {
/* 225 */       PermissionLevel permissionLevel = PermissionLevel.byId(Integer.parseInt(value));
/* 226 */       return LevelBasedPermissionSet.forLevel(permissionLevel);
/* 227 */     } catch (NumberFormatException e) {
/* 228 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 233 */   public static String serializePermission(LevelBasedPermissionSet permission) { return Integer.toString(permission.level().id()); }
/*     */ 
/*     */ 
/*     */   
/* 237 */   public WorldDimensions createDimensions(HolderLookup.Provider registries) { return this.worldDimensionData.create(registries); }
/*     */   private static final class WorldDimensionData extends Record { private final JsonObject generatorSettings; private final String levelType;
/*     */     
/* 240 */     private WorldDimensionData(JsonObject generatorSettings, String levelType) { this.generatorSettings = generatorSettings; this.levelType = levelType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #240	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #240	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #240	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;
/* 240 */       //   0	8	1	o	Ljava/lang/Object; } public JsonObject generatorSettings() { return this.generatorSettings; } public String levelType() { return this.levelType; }
/* 241 */     private static final Map<String, ResourceKey<WorldPreset>> LEGACY_PRESET_NAMES = Map.of("default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WorldDimensions create(HolderLookup.Provider registries) {
/* 247 */       HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.WORLD_PRESET);
/*     */       
/* 249 */       Holder.Reference<WorldPreset> defaultHolder = (Holder.Reference)registryLookup.get(WorldPresets.NORMAL).or(() -> worldPresets.listElements().findAny()).orElseThrow(() -> new IllegalStateException("Invalid datapack contents: can't find default preset"));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 254 */       Objects.requireNonNull(registryLookup);
/* 255 */       Holder<WorldPreset> worldPreset = (Holder)Optional.ofNullable(Identifier.tryParse(this.levelType)).map(id -> ResourceKey.create(Registries.WORLD_PRESET, id)).or(() -> Optional.ofNullable((ResourceKey)LEGACY_PRESET_NAMES.get(this.levelType))).flatMap(registryLookup::get).orElseGet(() -> {
/* 256 */             DedicatedServerProperties.LOGGER.warn("Failed to parse level-type {}, defaulting to {}", this.levelType, defaultHolder.key().identifier());
/* 257 */             return defaultHolder;
/*     */           });
/*     */       
/* 260 */       WorldDimensions worldDimensions = ((WorldPreset)worldPreset.value()).createWorldDimensions();
/*     */ 
/*     */       
/* 263 */       if (worldPreset.is(WorldPresets.FLAT)) {
/* 264 */         RegistryOps<JsonElement> ops = registries.createSerializationContext(JsonOps.INSTANCE);
/* 265 */         Objects.requireNonNull(DedicatedServerProperties.LOGGER); Optional<FlatLevelGeneratorSettings> parsedSettings = FlatLevelGeneratorSettings.CODEC.parse(new Dynamic(ops, generatorSettings())).resultOrPartial(DedicatedServerProperties.LOGGER::error);
/* 266 */         if (parsedSettings.isPresent()) {
/* 267 */           return worldDimensions.replaceOverworldGenerator(registries, new FlatLevelSource((FlatLevelGeneratorSettings)parsedSettings.get()));
/*     */         }
/*     */       } 
/*     */       
/* 271 */       return worldDimensions;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dedicated\DedicatedServerProperties.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */