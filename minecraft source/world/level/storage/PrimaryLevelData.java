/*     */ package net.minecraft.world.level.storage;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.dimension.end.EndDragonFight;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.timers.TimerQueue;
/*     */ 
/*     */ public class PrimaryLevelData implements ServerLevelData, WorldData {
/*     */   public static final String LEVEL_NAME = "LevelName";
/*     */   protected static final String PLAYER = "Player";
/*     */   protected static final String WORLD_GEN_SETTINGS = "WorldGenSettings";
/*     */   private LevelSettings settings;
/*     */   private final WorldOptions worldOptions;
/*     */   private final SpecialWorldProperty specialWorldProperty;
/*     */   private final Lifecycle worldGenSettingsLifecycle;
/*     */   private LevelData.RespawnData respawnData;
/*     */   private long gameTime;
/*     */   private long dayTime;
/*     */   private final CompoundTag loadedPlayerTag;
/*     */   private final int version;
/*     */   private int clearWeatherTime;
/*     */   private boolean raining;
/*     */   private int rainTime;
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger(); private boolean thundering; private int thunderTime;
/*     */   private boolean initialized;
/*     */   private boolean difficultyLocked;
/*     */   @Deprecated
/*     */   private Optional<WorldBorder.Settings> legacyWorldBorderSettings;
/*     */   private EndDragonFight.Data endDragonFightData;
/*     */   private CompoundTag customBossEvents;
/*     */   private int wanderingTraderSpawnDelay;
/*     */   private int wanderingTraderSpawnChance;
/*     */   private UUID wanderingTraderId;
/*     */   private final Set<String> knownServerBrands;
/*     */   private boolean wasModded;
/*     */   private final Set<String> removedFeatureFlags;
/*     */   private final TimerQueue<MinecraftServer> scheduledEvents;
/*     */   
/*     */   @Deprecated
/*  59 */   public enum SpecialWorldProperty { NONE,
/*  60 */     FLAT,
/*  61 */     DEBUG; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PrimaryLevelData(CompoundTag loadedPlayerTag, boolean wasModded, LevelData.RespawnData respawnData, long gameTime, long dayTime, int version, int clearWeatherTime, int rainTime, boolean raining, int thunderTime, boolean thundering, boolean initialized, boolean difficultyLocked, Optional<WorldBorder.Settings> legacyWorldBorderSettings, int wanderingTraderSpawnDelay, int wanderingTraderSpawnChance, UUID wanderingTraderId, Set<String> knownServerBrands, Set<String> removedFeatureFlags, TimerQueue<MinecraftServer> scheduledEvents, CompoundTag customBossEvents, EndDragonFight.Data endDragonFightData, LevelSettings settings, WorldOptions worldOptions, SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle) {
/* 129 */     this.wasModded = wasModded;
/* 130 */     this.respawnData = respawnData;
/* 131 */     this.gameTime = gameTime;
/* 132 */     this.dayTime = dayTime;
/* 133 */     this.version = version;
/* 134 */     this.clearWeatherTime = clearWeatherTime;
/* 135 */     this.rainTime = rainTime;
/* 136 */     this.raining = raining;
/* 137 */     this.thunderTime = thunderTime;
/* 138 */     this.thundering = thundering;
/* 139 */     this.initialized = initialized;
/* 140 */     this.difficultyLocked = difficultyLocked;
/* 141 */     this.legacyWorldBorderSettings = legacyWorldBorderSettings;
/* 142 */     this.wanderingTraderSpawnDelay = wanderingTraderSpawnDelay;
/* 143 */     this.wanderingTraderSpawnChance = wanderingTraderSpawnChance;
/* 144 */     this.wanderingTraderId = wanderingTraderId;
/* 145 */     this.knownServerBrands = knownServerBrands;
/* 146 */     this.removedFeatureFlags = removedFeatureFlags;
/* 147 */     this.loadedPlayerTag = loadedPlayerTag;
/* 148 */     this.scheduledEvents = scheduledEvents;
/* 149 */     this.customBossEvents = customBossEvents;
/* 150 */     this.endDragonFightData = endDragonFightData;
/* 151 */     this.settings = settings;
/* 152 */     this.worldOptions = worldOptions;
/* 153 */     this.specialWorldProperty = specialWorldProperty;
/* 154 */     this.worldGenSettingsLifecycle = worldGenSettingsLifecycle;
/*     */   }
/*     */   
/*     */   public PrimaryLevelData(LevelSettings levelSettings, WorldOptions worldOptions, SpecialWorldProperty specialWorldProperty, Lifecycle lifecycle) {
/* 158 */     this(null, false, LevelData.RespawnData.DEFAULT, 0L, 0L, 19133, 0, 0, false, 0, false, false, false, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 172 */         Optional.empty(), 0, 0, null, 
/*     */ 
/*     */ 
/*     */         
/* 176 */         Sets.newLinkedHashSet(), new HashSet(), new TimerQueue(TimerCallbacks.SERVER_CALLBACKS), null, EndDragonFight.Data.DEFAULT, levelSettings
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         .copy(), worldOptions, specialWorldProperty, lifecycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> PrimaryLevelData parse(Dynamic<T> input, LevelSettings settings, SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle) {
/* 189 */     long gameTime = input.get("Time").asLong(0L);
/*     */ 
/*     */     
/* 192 */     Objects.requireNonNull(CompoundTag.CODEC);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     Objects.requireNonNull(LOGGER); return new PrimaryLevelData((CompoundTag)input.get("Player").flatMap(CompoundTag.CODEC::parse).result().orElse(null), input.get("WasModded").asBoolean(false), (LevelData.RespawnData)input.get("spawn").read(LevelData.RespawnData.CODEC).result().orElse(LevelData.RespawnData.DEFAULT), gameTime, input.get("DayTime").asLong(gameTime), LevelVersion.parse(input).levelDataVersion(), input.get("clearWeatherTime").asInt(0), input.get("rainTime").asInt(0), input.get("raining").asBoolean(false), input.get("thunderTime").asInt(0), input.get("thundering").asBoolean(false), input.get("initialized").asBoolean(true), input.get("DifficultyLocked").asBoolean(false), WorldBorder.Settings.CODEC.parse(input.get("world_border").orElseEmptyMap()).result(), input.get("WanderingTraderSpawnDelay").asInt(0), input.get("WanderingTraderSpawnChance").asInt(0), (UUID)input.get("WanderingTraderId").read(UUIDUtil.CODEC).result().orElse(null), (Set)input.get("ServerBrands").asStream().flatMap(b -> b.asString().result().stream()).collect(Collectors.toCollection(Sets::newLinkedHashSet)), (Set)input.get("removed_features").asStream().flatMap(b -> b.asString().result().stream()).collect(Collectors.toSet()), new TimerQueue(TimerCallbacks.SERVER_CALLBACKS, input.get("ScheduledEvents").asStream()), (CompoundTag)input.get("CustomBossEvents").orElseEmptyMap().getValue(), (EndDragonFight.Data)input.get("DragonFight").read(EndDragonFight.Data.CODEC).resultOrPartial(LOGGER::error).orElse(EndDragonFight.Data.DEFAULT), settings, worldOptions, specialWorldProperty, worldGenSettingsLifecycle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompoundTag createTag(RegistryAccess registryAccess, CompoundTag playerData) {
/* 223 */     if (playerData == null) {
/* 224 */       playerData = this.loadedPlayerTag;
/*     */     }
/* 226 */     CompoundTag tag = new CompoundTag();
/* 227 */     setTagData(registryAccess, tag, playerData);
/* 228 */     return tag;
/*     */   }
/*     */   
/*     */   private void setTagData(RegistryAccess registryAccess, CompoundTag tag, CompoundTag playerTag) {
/* 232 */     tag.put("ServerBrands", stringCollectionToTag(this.knownServerBrands));
/* 233 */     tag.putBoolean("WasModded", this.wasModded);
/*     */     
/* 235 */     if (!this.removedFeatureFlags.isEmpty()) {
/* 236 */       tag.put("removed_features", stringCollectionToTag(this.removedFeatureFlags));
/*     */     }
/*     */     
/* 239 */     CompoundTag worldVersion = new CompoundTag();
/* 240 */     worldVersion.putString("Name", SharedConstants.getCurrentVersion().name());
/* 241 */     worldVersion.putInt("Id", SharedConstants.getCurrentVersion().dataVersion().version());
/* 242 */     worldVersion.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().stable());
/* 243 */     worldVersion.putString("Series", SharedConstants.getCurrentVersion().dataVersion().series());
/* 244 */     tag.put("Version", worldVersion);
/*     */     
/* 246 */     NbtUtils.addCurrentDataVersion(tag);
/*     */     
/* 248 */     RegistryOps registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
/*     */ 
/*     */     
/* 251 */     Objects.requireNonNull(LOGGER); WorldGenSettings.encode(registryOps, this.worldOptions, registryAccess).resultOrPartial(Util.prefix("WorldGenSettings: ", LOGGER::error))
/* 252 */       .ifPresent(s -> tag.put("WorldGenSettings", s));
/*     */     
/* 254 */     tag.putInt("GameType", this.settings.gameType().getId());
/* 255 */     tag.store("spawn", LevelData.RespawnData.CODEC, this.respawnData);
/* 256 */     tag.putLong("Time", this.gameTime);
/* 257 */     tag.putLong("DayTime", this.dayTime);
/* 258 */     tag.putLong("LastPlayed", Util.getEpochMillis());
/* 259 */     tag.putString("LevelName", this.settings.levelName());
/* 260 */     tag.putInt("version", 19133);
/* 261 */     tag.putInt("clearWeatherTime", this.clearWeatherTime);
/* 262 */     tag.putInt("rainTime", this.rainTime);
/* 263 */     tag.putBoolean("raining", this.raining);
/* 264 */     tag.putInt("thunderTime", this.thunderTime);
/* 265 */     tag.putBoolean("thundering", this.thundering);
/* 266 */     tag.putBoolean("hardcore", this.settings.hardcore());
/* 267 */     tag.putBoolean("allowCommands", this.settings.allowCommands());
/* 268 */     tag.putBoolean("initialized", this.initialized);
/* 269 */     this.legacyWorldBorderSettings.ifPresent(settings -> tag.store("world_border", WorldBorder.Settings.CODEC, settings));
/* 270 */     tag.putByte("Difficulty", (byte)this.settings.difficulty().getId());
/* 271 */     tag.putBoolean("DifficultyLocked", this.difficultyLocked);
/* 272 */     tag.store("game_rules", GameRules.codec(enabledFeatures()), this.settings.gameRules());
/*     */     
/* 274 */     tag.store("DragonFight", EndDragonFight.Data.CODEC, this.endDragonFightData);
/*     */     
/* 276 */     if (playerTag != null) {
/* 277 */       tag.put("Player", playerTag);
/*     */     }
/*     */     
/* 280 */     tag.store(WorldDataConfiguration.MAP_CODEC, this.settings.getDataConfiguration());
/*     */     
/* 282 */     if (this.customBossEvents != null) {
/* 283 */       tag.put("CustomBossEvents", this.customBossEvents);
/*     */     }
/*     */     
/* 286 */     tag.put("ScheduledEvents", this.scheduledEvents.store());
/*     */     
/* 288 */     tag.putInt("WanderingTraderSpawnDelay", this.wanderingTraderSpawnDelay);
/* 289 */     tag.putInt("WanderingTraderSpawnChance", this.wanderingTraderSpawnChance);
/* 290 */     tag.storeNullable("WanderingTraderId", UUIDUtil.CODEC, this.wanderingTraderId);
/*     */   }
/*     */   
/*     */   private static ListTag stringCollectionToTag(Set<String> values) {
/* 294 */     ListTag result = new ListTag();
/* 295 */     Objects.requireNonNull(result); values.stream().map(StringTag::valueOf).forEach(result::add);
/* 296 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 301 */   public LevelData.RespawnData getRespawnData() { return this.respawnData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 306 */   public long getGameTime() { return this.gameTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 311 */   public long getDayTime() { return this.dayTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 316 */   public CompoundTag getLoadedPlayerTag() { return this.loadedPlayerTag; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 321 */   public void setGameTime(long time) { this.gameTime = time; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 326 */   public void setDayTime(long time) { this.dayTime = time; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   public void setSpawn(LevelData.RespawnData respawnData) { this.respawnData = respawnData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 336 */   public String getLevelName() { return this.settings.levelName(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 341 */   public int getVersion() { return this.version; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   public int getClearWeatherTime() { return this.clearWeatherTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 351 */   public void setClearWeatherTime(int clearWeatherTime) { this.clearWeatherTime = clearWeatherTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 356 */   public boolean isThundering() { return this.thundering; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 361 */   public void setThundering(boolean thundering) { this.thundering = thundering; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 366 */   public int getThunderTime() { return this.thunderTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 371 */   public void setThunderTime(int thunderTime) { this.thunderTime = thunderTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   public boolean isRaining() { return this.raining; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 381 */   public void setRaining(boolean raining) { this.raining = raining; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 386 */   public int getRainTime() { return this.rainTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 391 */   public void setRainTime(int rainTime) { this.rainTime = rainTime; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 396 */   public GameType getGameType() { return this.settings.gameType(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   public void setGameType(GameType gameType) { this.settings = this.settings.withGameType(gameType); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 406 */   public boolean isHardcore() { return this.settings.hardcore(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 411 */   public boolean isAllowCommands() { return this.settings.allowCommands(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 416 */   public boolean isInitialized() { return this.initialized; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   public void setInitialized(boolean initialized) { this.initialized = initialized; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 426 */   public GameRules getGameRules() { return this.settings.gameRules(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 431 */   public Optional<WorldBorder.Settings> getLegacyWorldBorderSettings() { return this.legacyWorldBorderSettings; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 436 */   public void setLegacyWorldBorderSettings(Optional<WorldBorder.Settings> settings) { this.legacyWorldBorderSettings = settings; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 441 */   public Difficulty getDifficulty() { return this.settings.difficulty(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 446 */   public void setDifficulty(Difficulty difficulty) { this.settings = this.settings.withDifficulty(difficulty); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 451 */   public boolean isDifficultyLocked() { return this.difficultyLocked; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 456 */   public void setDifficultyLocked(boolean difficultyLocked) { this.difficultyLocked = difficultyLocked; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 461 */   public TimerQueue<MinecraftServer> getScheduledEvents() { return this.scheduledEvents; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 466 */     super.fillCrashReportCategory(category, levelHeightAccessor);
/* 467 */     fillCrashReportCategory(category);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 472 */   public WorldOptions worldGenOptions() { return this.worldOptions; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 477 */   public boolean isFlatWorld() { return (this.specialWorldProperty == SpecialWorldProperty.FLAT); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 482 */   public boolean isDebugWorld() { return (this.specialWorldProperty == SpecialWorldProperty.DEBUG); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 487 */   public Lifecycle worldGenSettingsLifecycle() { return this.worldGenSettingsLifecycle; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 492 */   public EndDragonFight.Data endDragonFightData() { return this.endDragonFightData; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 497 */   public void setEndDragonFightData(EndDragonFight.Data data) { this.endDragonFightData = data; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 502 */   public WorldDataConfiguration getDataConfiguration() { return this.settings.getDataConfiguration(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 507 */   public void setDataConfiguration(WorldDataConfiguration dataConfiguration) { this.settings = this.settings.withDataConfiguration(dataConfiguration); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 512 */   public CompoundTag getCustomBossEvents() { return this.customBossEvents; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 517 */   public void setCustomBossEvents(CompoundTag customBossEvents) { this.customBossEvents = customBossEvents; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 522 */   public int getWanderingTraderSpawnDelay() { return this.wanderingTraderSpawnDelay; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 527 */   public void setWanderingTraderSpawnDelay(int wanderingTraderSpawnDelay) { this.wanderingTraderSpawnDelay = wanderingTraderSpawnDelay; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 532 */   public int getWanderingTraderSpawnChance() { return this.wanderingTraderSpawnChance; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 537 */   public void setWanderingTraderSpawnChance(int wanderingTraderSpawnChance) { this.wanderingTraderSpawnChance = wanderingTraderSpawnChance; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 543 */   public UUID getWanderingTraderId() { return this.wanderingTraderId; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 548 */   public void setWanderingTraderId(UUID wanderingTraderId) { this.wanderingTraderId = wanderingTraderId; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModdedInfo(String serverBrand, boolean isModded) {
/* 553 */     this.knownServerBrands.add(serverBrand);
/* 554 */     this.wasModded |= isModded;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 559 */   public boolean wasModded() { return this.wasModded; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 564 */   public Set<String> getKnownServerBrands() { return ImmutableSet.copyOf(this.knownServerBrands); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 569 */   public Set<String> getRemovedFeatureFlags() { return Set.copyOf(this.removedFeatureFlags); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 574 */   public ServerLevelData overworldData() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 579 */   public LevelSettings getLevelSettings() { return this.settings.copy(); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\PrimaryLevelData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */