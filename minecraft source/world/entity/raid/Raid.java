/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnPlacementType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.Rarity;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BannerPatterns;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raid {
/*  66 */   public static final SpawnPlacementType RAVAGER_SPAWN_PLACEMENT_TYPE = SpawnPlacements.getPlacementType(EntityType.RAVAGER);
/*     */   
/*     */   private enum RaidStatus implements StringRepresentable {
/*  69 */     ONGOING("ongoing"),
/*  70 */     VICTORY("victory"),
/*  71 */     LOSS("loss"),
/*  72 */     STOPPED("stopped"); public static final Codec<RaidStatus> CODEC; private final String name;
/*     */     static  {
/*  74 */       CODEC = StringRepresentable.fromEnum(RaidStatus::values);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  79 */     RaidStatus(String name) { this.name = name; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     public String getSerializedName() { return this.name; }
/*     */   }
/*     */   
/*     */   private enum RaiderType
/*     */   {
/*  89 */     VINDICATOR(EntityType.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }),
/*  90 */     EVOKER(EntityType.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2
/*     */       }),
/*  92 */     PILLAGER(EntityType.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }),
/*  93 */     WITCH(EntityType.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }),
/*  94 */     RAVAGER(EntityType.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 }); private static final RaiderType[] VALUES; private final EntityType<? extends Raider> entityType; private final int[] spawnsPerWaveBeforeBonus;
/*     */     
/*     */     static  {
/*  97 */       VALUES = values();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     RaiderType(EntityType<? extends Raider> entityType, int[] spawnsPerWaveBeforeBonus) {
/* 103 */       this.entityType = entityType;
/* 104 */       this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
/*     */     }
/*     */   }
/*     */   
/* 108 */   public static final MapCodec<Raid> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 109 */         .fieldOf("started").forGetter(()), Codec.BOOL
/* 110 */         .fieldOf("active").forGetter(()), Codec.LONG
/* 111 */         .fieldOf("ticks_active").forGetter(()), Codec.INT
/* 112 */         .fieldOf("raid_omen_level").forGetter(()), Codec.INT
/* 113 */         .fieldOf("groups_spawned").forGetter(()), Codec.INT
/* 114 */         .fieldOf("cooldown_ticks").forGetter(()), Codec.INT
/* 115 */         .fieldOf("post_raid_ticks").forGetter(()), Codec.FLOAT
/* 116 */         .fieldOf("total_health").forGetter(()), Codec.INT
/* 117 */         .fieldOf("group_count").forGetter(()), RaidStatus.CODEC
/* 118 */         .fieldOf("status").forGetter(()), BlockPos.CODEC
/* 119 */         .fieldOf("center").forGetter(()), UUIDUtil.CODEC_SET
/* 120 */         .fieldOf("heroes_of_the_village").forGetter(()))
/* 121 */       .apply(i, Raid::new));
/*     */   
/*     */   private static final int ALLOW_SPAWNING_WITHIN_VILLAGE_SECONDS_THRESHOLD = 7;
/*     */   
/*     */   private static final int SECTION_RADIUS_FOR_FINDING_NEW_VILLAGE_CENTER = 2;
/*     */   private static final int VILLAGE_SEARCH_RADIUS = 32;
/*     */   private static final int RAID_TIMEOUT_TICKS = 48000;
/*     */   private static final int NUM_SPAWN_ATTEMPTS = 5;
/* 129 */   private static final Component OMINOUS_BANNER_PATTERN_NAME = Component.translatable("block.minecraft.ominous_banner");
/*     */   
/*     */   private static final String RAIDERS_REMAINING = "event.minecraft.raid.raiders_remaining";
/*     */   
/*     */   public static final int VILLAGE_RADIUS_BUFFER = 16;
/*     */   private static final int POST_RAID_TICK_LIMIT = 40;
/*     */   private static final int DEFAULT_PRE_RAID_TICKS = 300;
/*     */   public static final int MAX_NO_ACTION_TIME = 2400;
/*     */   public static final int MAX_CELEBRATION_TICKS = 600;
/*     */   private static final int OUTSIDE_RAID_BOUNDS_TIMEOUT = 30;
/*     */   public static final int DEFAULT_MAX_RAID_OMEN_LEVEL = 5;
/*     */   private static final int LOW_MOB_THRESHOLD = 2;
/* 141 */   private static final Component RAID_NAME_COMPONENT = Component.translatable("event.minecraft.raid");
/* 142 */   private static final Component RAID_BAR_VICTORY_COMPONENT = Component.translatable("event.minecraft.raid.victory.full"); private static final int HERO_OF_THE_VILLAGE_DURATION = 48000; private static final int VALID_RAID_RADIUS = 96; public static final int VALID_RAID_RADIUS_SQR = 9216; public static final int RAID_REMOVAL_THRESHOLD_SQR = 12544; private final Map<Integer, Raider> groupToLeaderMap; private final Map<Integer, Set<Raider>> groupRaiderMap; private final Set<UUID> heroesOfTheVillage; private long ticksActive; private BlockPos center; private boolean started; private float totalHealth;
/* 143 */   private static final Component RAID_BAR_DEFEAT_COMPONENT = Component.translatable("event.minecraft.raid.defeat.full"); private int raidOmenLevel; private boolean active; private int groupsSpawned; private final ServerBossEvent raidEvent; private int postRaidTicks;
/*     */   private int raidCooldownTicks;
/*     */   private final RandomSource random;
/*     */   private final int numGroups;
/*     */   private RaidStatus status;
/*     */   private int celebrationTicks;
/*     */   private Optional<BlockPos> waveSpawnPos;
/*     */   
/*     */   public Raid(BlockPos center, Difficulty difficulty) {
/* 152 */     this.groupToLeaderMap = Maps.newHashMap();
/* 153 */     this.groupRaiderMap = Maps.newHashMap();
/*     */     
/* 155 */     this.heroesOfTheVillage = Sets.newHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     this.raidEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
/*     */ 
/*     */     
/* 167 */     this.random = RandomSource.create();
/*     */ 
/*     */ 
/*     */     
/* 171 */     this.waveSpawnPos = Optional.empty();
/*     */ 
/*     */     
/* 174 */     this.active = true;
/* 175 */     this.raidCooldownTicks = 300;
/* 176 */     this.raidEvent.setProgress(0.0F);
/* 177 */     this.center = center;
/* 178 */     this.numGroups = getNumGroups(difficulty);
/* 179 */     this.status = RaidStatus.ONGOING; } private Raid(boolean started, boolean active, long ticksActive, int raidOmenLevel, int groupsSpawned, int raidCooldownTicks, int postRaidTicks, float totalHealth, int numGroups, RaidStatus status, BlockPos center, Set<UUID> heroesOfTheVillage) { this.groupToLeaderMap = Maps.newHashMap(); this.groupRaiderMap = Maps.newHashMap(); this.heroesOfTheVillage = Sets.newHashSet();
/*     */     this.raidEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
/*     */     this.random = RandomSource.create();
/*     */     this.waveSpawnPos = Optional.empty();
/* 183 */     this.started = started;
/* 184 */     this.active = active;
/* 185 */     this.ticksActive = ticksActive;
/* 186 */     this.raidOmenLevel = raidOmenLevel;
/* 187 */     this.groupsSpawned = groupsSpawned;
/* 188 */     this.raidCooldownTicks = raidCooldownTicks;
/* 189 */     this.postRaidTicks = postRaidTicks;
/* 190 */     this.totalHealth = totalHealth;
/* 191 */     this.center = center;
/* 192 */     this.numGroups = numGroups;
/* 193 */     this.status = status;
/* 194 */     this.heroesOfTheVillage.addAll(heroesOfTheVillage); }
/*     */ 
/*     */ 
/*     */   
/* 198 */   public boolean isOver() { return (isVictory() || isLoss()); }
/*     */ 
/*     */ 
/*     */   
/* 202 */   public boolean isBetweenWaves() { return (hasFirstWaveSpawned() && getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0); }
/*     */ 
/*     */ 
/*     */   
/* 206 */   public boolean hasFirstWaveSpawned() { return (this.groupsSpawned > 0); }
/*     */ 
/*     */ 
/*     */   
/* 210 */   public boolean isStopped() { return (this.status == RaidStatus.STOPPED); }
/*     */ 
/*     */ 
/*     */   
/* 214 */   public boolean isVictory() { return (this.status == RaidStatus.VICTORY); }
/*     */ 
/*     */ 
/*     */   
/* 218 */   public boolean isLoss() { return (this.status == RaidStatus.LOSS); }
/*     */ 
/*     */ 
/*     */   
/* 222 */   public float getTotalHealth() { return this.totalHealth; }
/*     */ 
/*     */   
/*     */   public Set<Raider> getAllRaiders() {
/* 226 */     Set<Raider> raiders = Sets.newHashSet();
/* 227 */     for (Set<Raider> raiderSet : this.groupRaiderMap.values()) {
/* 228 */       raiders.addAll(raiderSet);
/*     */     }
/* 230 */     return raiders;
/*     */   }
/*     */ 
/*     */   
/* 234 */   public boolean isStarted() { return this.started; }
/*     */ 
/*     */ 
/*     */   
/* 238 */   public int getGroupsSpawned() { return this.groupsSpawned; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Predicate<ServerPlayer> validPlayer() {
/* 245 */     return player -> {
/* 246 */         BlockPos pos = player.blockPosition();
/* 247 */         return (player.isAlive() && player.level().getRaidAt(pos) == this);
/*     */       };
/*     */   }
/*     */   
/*     */   private void updatePlayers(ServerLevel level) {
/* 252 */     Set<ServerPlayer> currentPlayersInRaid = Sets.newHashSet(this.raidEvent.getPlayers());
/* 253 */     List<ServerPlayer> newPlayersInRaid = level.getPlayers(validPlayer());
/*     */     
/* 255 */     for (ServerPlayer player : newPlayersInRaid) {
/* 256 */       if (!currentPlayersInRaid.contains(player)) {
/* 257 */         this.raidEvent.addPlayer(player);
/*     */       }
/*     */     } 
/*     */     
/* 261 */     for (ServerPlayer player : currentPlayersInRaid) {
/* 262 */       if (!newPlayersInRaid.contains(player)) {
/* 263 */         this.raidEvent.removePlayer(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 269 */   public int getMaxRaidOmenLevel() { return 5; }
/*     */ 
/*     */ 
/*     */   
/* 273 */   public int getRaidOmenLevel() { return this.raidOmenLevel; }
/*     */ 
/*     */ 
/*     */   
/* 277 */   public void setRaidOmenLevel(int raidOmenLevel) { this.raidOmenLevel = raidOmenLevel; }
/*     */ 
/*     */   
/*     */   public boolean absorbRaidOmen(ServerPlayer player) {
/* 281 */     MobEffectInstance effect = player.getEffect(MobEffects.RAID_OMEN);
/* 282 */     if (effect == null) {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     this.raidOmenLevel += effect.getAmplifier() + 1;
/* 287 */     this.raidOmenLevel = Mth.clamp(this.raidOmenLevel, 0, getMaxRaidOmenLevel());
/*     */     
/* 289 */     if (!hasFirstWaveSpawned()) {
/* 290 */       player.awardStat(Stats.RAID_TRIGGER);
/* 291 */       CriteriaTriggers.RAID_OMEN.trigger(player);
/*     */     } 
/*     */     
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 298 */     this.active = false;
/* 299 */     this.raidEvent.removeAllPlayers();
/* 300 */     this.status = RaidStatus.STOPPED;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level) {
/* 304 */     if (isStopped()) {
/*     */       return;
/*     */     }
/*     */     
/* 308 */     if (this.status == RaidStatus.ONGOING) {
/* 309 */       boolean oldActive = this.active;
/* 310 */       this.active = level.hasChunkAt(this.center);
/*     */       
/* 312 */       if (level.getDifficulty() == Difficulty.PEACEFUL) {
/* 313 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/* 317 */       if (oldActive != this.active) {
/* 318 */         this.raidEvent.setVisible(this.active);
/*     */       }
/*     */ 
/*     */       
/* 322 */       if (!this.active) {
/*     */         return;
/*     */       }
/*     */       
/* 326 */       if (!level.isVillage(this.center))
/*     */       {
/*     */         
/* 329 */         moveRaidCenterToNearbyVillageSection(level);
/*     */       }
/*     */ 
/*     */       
/* 333 */       if (!level.isVillage(this.center))
/*     */       {
/* 335 */         if (this.groupsSpawned > 0) {
/* 336 */           this.status = RaidStatus.LOSS;
/*     */         } else {
/* 338 */           stop();
/*     */         } 
/*     */       }
/*     */       
/* 342 */       this.ticksActive++;
/*     */       
/* 344 */       if (this.ticksActive >= 48000L) {
/* 345 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/* 349 */       int raidersAlive = getTotalRaidersAlive();
/*     */ 
/*     */       
/* 352 */       if (raidersAlive == 0 && hasMoreWaves()) {
/* 353 */         if (this.raidCooldownTicks > 0) {
/* 354 */           boolean hasCachedWaveSpawnPos = this.waveSpawnPos.isPresent();
/* 355 */           boolean shouldTryToFindSpawnPos = (!hasCachedWaveSpawnPos && this.raidCooldownTicks % 5 == 0);
/*     */ 
/*     */           
/* 358 */           if (hasCachedWaveSpawnPos && !level.isPositionEntityTicking((BlockPos)this.waveSpawnPos.get())) {
/* 359 */             shouldTryToFindSpawnPos = true;
/*     */           }
/*     */ 
/*     */           
/* 363 */           if (shouldTryToFindSpawnPos) {
/* 364 */             this.waveSpawnPos = getValidSpawnPos(level);
/*     */           }
/*     */           
/* 367 */           if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0) {
/* 368 */             updatePlayers(level);
/*     */           }
/* 370 */           this.raidCooldownTicks--;
/* 371 */           this.raidEvent.setProgress(Mth.clamp((300 - this.raidCooldownTicks) / 300.0F, 0.0F, 1.0F));
/* 372 */         } else if (this.raidCooldownTicks == 0 && this.groupsSpawned > 0) {
/* 373 */           this.raidCooldownTicks = 300;
/* 374 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           
/*     */           return;
/*     */         } 
/*     */       }
/*     */       
/* 380 */       if (this.ticksActive % 20L == 0L) {
/* 381 */         updatePlayers(level);
/* 382 */         updateRaiders(level);
/*     */         
/* 384 */         if (raidersAlive > 0) {
/*     */           
/* 386 */           if (raidersAlive <= 2) {
/* 387 */             this.raidEvent.setName(RAID_NAME_COMPONENT.copy().append(" - ").append(Component.translatable("event.minecraft.raid.raiders_remaining", new Object[] { Integer.valueOf(raidersAlive) })));
/*     */           } else {
/* 389 */             this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           } 
/*     */         } else {
/* 392 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */         } 
/*     */       } 
/*     */       
/* 396 */       if (SharedConstants.DEBUG_RAIDS) {
/* 397 */         this.raidEvent.setName(RAID_NAME_COMPONENT.copy().append(" wave: ").append("" + this.groupsSpawned).append(CommonComponents.SPACE).append("Raiders alive: ").append("" + getTotalRaidersAlive())
/* 398 */             .append(CommonComponents.SPACE).append("" + getHealthOfLivingRaiders()).append(" / ").append("" + this.totalHealth).append(" Is bonus? ").append("" + ((hasBonusWave() && hasSpawnedBonusWave()) ? 1 : 0))
/* 399 */             .append(" Status: ").append(this.status.getSerializedName()));
/*     */       }
/*     */       
/* 402 */       boolean soundPlayed = false;
/* 403 */       int attempt = 0;
/* 404 */       while (shouldSpawnGroup()) {
/*     */         
/* 406 */         BlockPos spawnPos = (BlockPos)this.waveSpawnPos.orElseGet(() -> findRandomSpawnPos(level, 20));
/* 407 */         if (spawnPos != null) {
/* 408 */           this.started = true;
/* 409 */           spawnGroup(level, spawnPos);
/* 410 */           if (!soundPlayed) {
/* 411 */             playSound(level, spawnPos);
/* 412 */             soundPlayed = true;
/*     */           } 
/*     */         } else {
/* 415 */           attempt++;
/*     */         } 
/*     */         
/* 418 */         if (attempt > 5) {
/* 419 */           stop();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 425 */       if (isStarted() && !hasMoreWaves() && raidersAlive == 0) {
/* 426 */         if (this.postRaidTicks < 40) {
/* 427 */           this.postRaidTicks++;
/*     */         } else {
/* 429 */           this.status = RaidStatus.VICTORY;
/* 430 */           for (UUID heroUUID : this.heroesOfTheVillage) {
/* 431 */             Entity entity = level.getEntity(heroUUID);
/* 432 */             if (entity instanceof LivingEntity) { LivingEntity hero = (LivingEntity)entity; if (!entity.isSpectator()) {
/* 433 */                 hero.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.raidOmenLevel - 1, false, false, true));
/* 434 */                 if (hero instanceof ServerPlayer) { ServerPlayer playerHero = (ServerPlayer)hero;
/* 435 */                   playerHero.awardStat(Stats.RAID_WIN);
/* 436 */                   CriteriaTriggers.RAID_WIN.trigger(playerHero); }
/*     */               
/*     */               }  }
/*     */           
/*     */           } 
/*     */         } 
/*     */       }
/* 443 */       setDirty(level);
/* 444 */     } else if (isOver()) {
/* 445 */       this.celebrationTicks++;
/* 446 */       if (this.celebrationTicks >= 600) {
/* 447 */         stop();
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 452 */       if (this.celebrationTicks % 20 == 0) {
/* 453 */         updatePlayers(level);
/* 454 */         this.raidEvent.setVisible(true);
/* 455 */         if (isVictory()) {
/* 456 */           this.raidEvent.setProgress(0.0F);
/* 457 */           this.raidEvent.setName(RAID_BAR_VICTORY_COMPONENT);
/*     */         } else {
/* 459 */           this.raidEvent.setName(RAID_BAR_DEFEAT_COMPONENT);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveRaidCenterToNearbyVillageSection(ServerLevel level) {
/* 466 */     Stream<SectionPos> sectionsToSearchForVillage = SectionPos.cube(SectionPos.of(this.center), 2);
/*     */ 
/*     */     
/* 469 */     Objects.requireNonNull(level); sectionsToSearchForVillage.filter(level::isVillage)
/* 470 */       .map(SectionPos::center)
/* 471 */       .min(Comparator.comparingDouble(pos -> pos.distSqr(this.center)))
/* 472 */       .ifPresent(this::setCenter);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getValidSpawnPos(ServerLevel level) {
/* 476 */     BlockPos spawnPos = findRandomSpawnPos(level, 8);
/* 477 */     if (spawnPos != null) {
/* 478 */       return Optional.of(spawnPos);
/*     */     }
/* 480 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private boolean hasMoreWaves() {
/* 484 */     if (hasBonusWave()) {
/* 485 */       return !hasSpawnedBonusWave();
/*     */     }
/* 487 */     return !isFinalWave();
/*     */   }
/*     */ 
/*     */   
/* 491 */   private boolean isFinalWave() { return (getGroupsSpawned() == this.numGroups); }
/*     */ 
/*     */ 
/*     */   
/* 495 */   private boolean hasBonusWave() { return (this.raidOmenLevel > 1); }
/*     */ 
/*     */ 
/*     */   
/* 499 */   private boolean hasSpawnedBonusWave() { return (getGroupsSpawned() > this.numGroups); }
/*     */ 
/*     */ 
/*     */   
/* 503 */   private boolean shouldSpawnBonusGroup() { return (isFinalWave() && getTotalRaidersAlive() == 0 && hasBonusWave()); }
/*     */ 
/*     */   
/*     */   private void updateRaiders(ServerLevel level) {
/* 507 */     Iterator<Set<Raider>> raiders = this.groupRaiderMap.values().iterator();
/* 508 */     Set<Raider> toRemove = Sets.newHashSet();
/*     */     
/* 510 */     while (raiders.hasNext()) {
/* 511 */       Set<Raider> raiderSet = (Set)raiders.next();
/* 512 */       for (Raider raider : raiderSet) {
/* 513 */         BlockPos raiderPos = raider.blockPosition();
/*     */ 
/*     */ 
/*     */         
/* 517 */         if (raider.isRemoved() || raider.level().dimension() != level.dimension() || this.center.distSqr(raiderPos) >= 12544.0D) {
/* 518 */           toRemove.add(raider); continue;
/*     */         } 
/* 520 */         if (raider.tickCount <= 600) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 525 */         if (level.getEntity(raider.getUUID()) == null) {
/* 526 */           toRemove.add(raider);
/*     */         }
/*     */ 
/*     */         
/* 530 */         if (!level.isVillage(raiderPos) && raider.getNoActionTime() > 2400) {
/* 531 */           raider.setTicksOutsideRaid(raider.getTicksOutsideRaid() + 1);
/*     */         }
/*     */         
/* 534 */         if (raider.getTicksOutsideRaid() >= 30) {
/* 535 */           toRemove.add(raider);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 540 */     for (Raider raider : toRemove) {
/* 541 */       removeFromRaid(level, raider, true);
/* 542 */       if (raider.isPatrolLeader()) {
/* 543 */         removeLeader(raider.getWave());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playSound(ServerLevel level, BlockPos soundOrigin) {
/* 549 */     float distAway = 13.0F;
/* 550 */     int range = 64;
/*     */     
/* 552 */     Collection<ServerPlayer> playersInRaid = this.raidEvent.getPlayers();
/* 553 */     long seed = this.random.nextLong();
/* 554 */     for (ServerPlayer player : level.players()) {
/* 555 */       Vec3 playerLoc = player.position();
/* 556 */       Vec3 raidLoc = Vec3.atCenterOf(soundOrigin);
/* 557 */       double distBtwn = Math.sqrt((raidLoc.x - playerLoc.x) * (raidLoc.x - playerLoc.x) + (raidLoc.z - playerLoc.z) * (raidLoc.z - playerLoc.z));
/*     */       
/* 559 */       double x3 = playerLoc.x + 13.0D / distBtwn * (raidLoc.x - playerLoc.x);
/* 560 */       double z3 = playerLoc.z + 13.0D / distBtwn * (raidLoc.z - playerLoc.z);
/*     */       
/* 562 */       if (distBtwn <= 64.0D || playersInRaid.contains(player)) {
/* 563 */         player.connection.send(new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.NEUTRAL, x3, player.getY(), z3, 64.0F, 1.0F, seed));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGroup(ServerLevel level, BlockPos pos) {
/* 569 */     boolean leaderSet = false;
/* 570 */     int groupNumber = this.groupsSpawned + 1;
/* 571 */     this.totalHealth = 0.0F;
/* 572 */     DifficultyInstance difficulty = level.getCurrentDifficultyAt(pos);
/* 573 */     boolean isBonusGroup = shouldSpawnBonusGroup();
/*     */     
/* 575 */     for (RaiderType raiderType : RaiderType.VALUES) {
/* 576 */       int numSpawns = getDefaultNumSpawns(raiderType, groupNumber, isBonusGroup) + getPotentialBonusSpawns(raiderType, this.random, groupNumber, difficulty, isBonusGroup);
/* 577 */       int ravagersSpawned = 0;
/*     */       
/* 579 */       for (int i = 0; i < numSpawns; i++) {
/* 580 */         Raider raider = (Raider)raiderType.entityType.create(level, EntitySpawnReason.EVENT);
/* 581 */         if (raider == null) {
/*     */           break;
/*     */         }
/*     */         
/* 585 */         if (!leaderSet && raider.canBeLeader()) {
/* 586 */           raider.setPatrolLeader(true);
/* 587 */           setLeader(groupNumber, raider);
/* 588 */           leaderSet = true;
/*     */         } 
/*     */         
/* 591 */         joinRaid(level, groupNumber, raider, pos, false);
/*     */         
/* 593 */         if (raiderType.entityType == EntityType.RAVAGER) {
/* 594 */           Raider ridingRaider = null;
/* 595 */           if (groupNumber == getNumGroups(Difficulty.NORMAL)) {
/* 596 */             ridingRaider = (Raider)EntityType.PILLAGER.create(level, EntitySpawnReason.EVENT);
/* 597 */           } else if (groupNumber >= getNumGroups(Difficulty.HARD)) {
/*     */             
/* 599 */             if (ravagersSpawned == 0) {
/* 600 */               ridingRaider = (Raider)EntityType.EVOKER.create(level, EntitySpawnReason.EVENT);
/*     */             } else {
/* 602 */               ridingRaider = (Raider)EntityType.VINDICATOR.create(level, EntitySpawnReason.EVENT);
/*     */             } 
/*     */           } 
/* 605 */           ravagersSpawned++;
/*     */           
/* 607 */           if (ridingRaider != null) {
/* 608 */             joinRaid(level, groupNumber, ridingRaider, pos, false);
/* 609 */             ridingRaider.snapTo(pos, 0.0F, 0.0F);
/* 610 */             ridingRaider.startRiding(raider, false, false);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 616 */     this.waveSpawnPos = Optional.empty();
/* 617 */     this.groupsSpawned++;
/* 618 */     updateBossbar();
/* 619 */     setDirty(level);
/*     */   }
/*     */   
/*     */   public void joinRaid(ServerLevel level, int groupNumber, Raider raider, BlockPos pos, boolean exists) {
/* 623 */     boolean added = addWaveMob(level, groupNumber, raider);
/*     */     
/* 625 */     if (added) {
/* 626 */       raider.setCurrentRaid(this);
/* 627 */       raider.setWave(groupNumber);
/* 628 */       raider.setCanJoinRaid(true);
/* 629 */       raider.setTicksOutsideRaid(0);
/*     */       
/* 631 */       if (!exists && pos != null) {
/* 632 */         raider.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
/* 633 */         raider.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.EVENT, null);
/* 634 */         raider.applyRaidBuffs(level, groupNumber, false);
/* 635 */         raider.setOnGround(true);
/* 636 */         level.addFreshEntityWithPassengers(raider);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 642 */   public void updateBossbar() { this.raidEvent.setProgress(Mth.clamp(getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F)); }
/*     */ 
/*     */   
/*     */   public float getHealthOfLivingRaiders() {
/* 646 */     float health = 0.0F;
/* 647 */     for (Set<Raider> raiders : this.groupRaiderMap.values()) {
/* 648 */       for (Raider raider : raiders) {
/* 649 */         health += raider.getHealth();
/*     */       }
/*     */     } 
/* 652 */     return health;
/*     */   }
/*     */ 
/*     */   
/* 656 */   private boolean shouldSpawnGroup() { return (this.raidCooldownTicks == 0 && (this.groupsSpawned < this.numGroups || shouldSpawnBonusGroup()) && getTotalRaidersAlive() == 0); }
/*     */ 
/*     */ 
/*     */   
/* 660 */   public int getTotalRaidersAlive() { return this.groupRaiderMap.values().stream().mapToInt(Set::size).sum(); }
/*     */ 
/*     */   
/*     */   public void removeFromRaid(ServerLevel level, Raider raider, boolean removeFromTotalHealth) {
/* 664 */     Set<Raider> raiders = (Set)this.groupRaiderMap.get(Integer.valueOf(raider.getWave()));
/* 665 */     if (raiders != null) {
/* 666 */       boolean couldRemove = raiders.remove(raider);
/* 667 */       if (couldRemove) {
/*     */ 
/*     */         
/* 670 */         if (removeFromTotalHealth) {
/* 671 */           this.totalHealth -= raider.getHealth();
/*     */         }
/* 673 */         raider.setCurrentRaid(null);
/* 674 */         updateBossbar();
/* 675 */         setDirty(level);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 681 */   private void setDirty(ServerLevel level) { level.getRaids().setDirty(); }
/*     */ 
/*     */   
/*     */   public static ItemStack getOminousBannerInstance(HolderGetter<BannerPattern> patternGetter) {
/* 685 */     ItemStack banner = new ItemStack(Items.WHITE_BANNER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 696 */     BannerPatternLayers patterns = (new BannerPatternLayers.Builder()).addIfRegistered(patternGetter, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.CYAN).addIfRegistered(patternGetter, BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.STRIPE_CENTER, DyeColor.GRAY).addIfRegistered(patternGetter, BannerPatterns.BORDER, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.STRIPE_MIDDLE, DyeColor.BLACK).addIfRegistered(patternGetter, BannerPatterns.HALF_HORIZONTAL, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.BORDER, DyeColor.BLACK).build();
/*     */     
/* 698 */     banner.set(DataComponents.BANNER_PATTERNS, patterns);
/* 699 */     banner.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.BANNER_PATTERNS, true));
/* 700 */     banner.set(DataComponents.ITEM_NAME, OMINOUS_BANNER_PATTERN_NAME);
/* 701 */     banner.set(DataComponents.RARITY, Rarity.UNCOMMON);
/*     */     
/* 703 */     return banner;
/*     */   }
/*     */ 
/*     */   
/* 707 */   public Raider getLeader(int wave) { return (Raider)this.groupToLeaderMap.get(Integer.valueOf(wave)); }
/*     */ 
/*     */   
/*     */   private BlockPos findRandomSpawnPos(ServerLevel level, int maxTries) {
/* 711 */     int secondsRemaining = this.raidCooldownTicks / 20;
/*     */ 
/*     */     
/* 714 */     float howFar = 0.22F * secondsRemaining - 0.24F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 719 */     BlockPos.MutableBlockPos spawnPos = new BlockPos.MutableBlockPos();
/* 720 */     float startAngle = level.random.nextFloat() * 6.2831855F;
/*     */     
/* 722 */     for (int i = 0; i < maxTries; i++) {
/* 723 */       float angle = startAngle + 3.1415927F * i / 8.0F;
/* 724 */       int spawnX = this.center.getX() + Mth.floor(Mth.cos(angle) * 32.0F * howFar) + level.random.nextInt(3) * Mth.floor(howFar);
/* 725 */       int spawnZ = this.center.getZ() + Mth.floor(Mth.sin(angle) * 32.0F * howFar) + level.random.nextInt(3) * Mth.floor(howFar);
/* 726 */       int spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ);
/*     */ 
/*     */       
/* 729 */       if (Mth.abs(spawnY - this.center.getY()) <= 96) {
/*     */ 
/*     */ 
/*     */         
/* 733 */         spawnPos.set(spawnX, spawnY, spawnZ);
/*     */ 
/*     */ 
/*     */         
/* 737 */         if (!level.isVillage(spawnPos) || secondsRemaining <= 7) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 742 */           int delta = 10;
/* 743 */           if (level.hasChunksAt(spawnPos.getX() - 10, spawnPos.getZ() - 10, spawnPos.getX() + 10, spawnPos.getZ() + 10))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 749 */             if (level.isPositionEntityTicking(spawnPos))
/*     */             {
/*     */ 
/*     */ 
/*     */               
/* 754 */               if (RAVAGER_SPAWN_PLACEMENT_TYPE.isSpawnPositionOk(level, spawnPos, EntityType.RAVAGER) || (level
/* 755 */                 .getBlockState(spawnPos.below()).is(Blocks.SNOW) && level.getBlockState(spawnPos).isAir()))
/*     */               {
/* 757 */                 return spawnPos; }  }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 761 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 765 */   private boolean addWaveMob(ServerLevel level, int wave, Raider raider) { return addWaveMob(level, wave, raider, true); }
/*     */ 
/*     */   
/*     */   public boolean addWaveMob(ServerLevel level, int wave, Raider raider, boolean updateHealth) {
/* 769 */     this.groupRaiderMap.computeIfAbsent(Integer.valueOf(wave), v -> Sets.newHashSet());
/* 770 */     Set<Raider> raiders = (Set)this.groupRaiderMap.get(Integer.valueOf(wave));
/* 771 */     Raider existingCopy = null;
/*     */ 
/*     */ 
/*     */     
/* 775 */     for (Raider r : raiders) {
/* 776 */       if (r.getUUID().equals(raider.getUUID())) {
/* 777 */         existingCopy = r;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 782 */     if (existingCopy != null) {
/* 783 */       raiders.remove(existingCopy);
/* 784 */       raiders.add(raider);
/*     */     } 
/*     */     
/* 787 */     raiders.add(raider);
/* 788 */     if (updateHealth) {
/* 789 */       this.totalHealth += raider.getHealth();
/*     */     }
/* 791 */     updateBossbar();
/* 792 */     setDirty(level);
/* 793 */     return true;
/*     */   }
/*     */   
/*     */   public void setLeader(int wave, Raider raider) {
/* 797 */     this.groupToLeaderMap.put(Integer.valueOf(wave), raider);
/* 798 */     raider.setItemSlot(EquipmentSlot.HEAD, getOminousBannerInstance(raider.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/* 799 */     raider.setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */   }
/*     */ 
/*     */   
/* 803 */   public void removeLeader(int wave) { this.groupToLeaderMap.remove(Integer.valueOf(wave)); }
/*     */ 
/*     */ 
/*     */   
/* 807 */   public BlockPos getCenter() { return this.center; }
/*     */ 
/*     */ 
/*     */   
/* 811 */   private void setCenter(BlockPos center) { this.center = center; }
/*     */ 
/*     */ 
/*     */   
/* 815 */   private int getDefaultNumSpawns(RaiderType type, int wav, boolean isBonusWave) { return isBonusWave ? type.spawnsPerWaveBeforeBonus[this.numGroups] : type.spawnsPerWaveBeforeBonus[wav]; }
/*     */ 
/*     */   
/*     */   private int getPotentialBonusSpawns(RaiderType type, RandomSource random, int wav, DifficultyInstance difficultyInstance, boolean isBonusWave) {
/*     */     int bonusSpawns, bonusSpawns;
/* 820 */     Difficulty difficulty = difficultyInstance.getDifficulty();
/* 821 */     boolean isEasy = (difficulty == Difficulty.EASY);
/* 822 */     boolean isNormal = (difficulty == Difficulty.NORMAL);
/*     */     
/* 824 */     switch (type.ordinal()) {
/*     */       
/*     */       case 3:
/* 827 */         if (!isEasy && wav > 2 && wav != 4) {
/* 828 */           int bonusSpawns = 1;
/*     */           break;
/*     */         } 
/* 831 */         return 0;
/*     */       
/*     */       case 0:
/*     */       case 2:
/* 835 */         if (isEasy) {
/* 836 */           int bonusSpawns = random.nextInt(2); break;
/* 837 */         }  if (isNormal) {
/* 838 */           int bonusSpawns = 1; break;
/*     */         } 
/* 840 */         bonusSpawns = 2;
/*     */         break;
/*     */       
/*     */       case 4:
/* 844 */         bonusSpawns = (!isEasy && isBonusWave) ? 1 : 0;
/*     */         break;
/*     */       default:
/* 847 */         return 0;
/*     */     } 
/*     */     
/* 850 */     return (bonusSpawns > 0) ? random.nextInt(bonusSpawns + 1) : 0;
/*     */   }
/*     */ 
/*     */   
/* 854 */   public boolean isActive() { return this.active; }
/*     */ 
/*     */   
/*     */   public int getNumGroups(Difficulty difficulty) {
/* 858 */     switch (difficulty) { default: throw new MatchException(null, null);case PEACEFUL: case EASY: case NORMAL: case HARD: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 862 */       7;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEnchantOdds() {
/* 867 */     int raidOmenLvl = getRaidOmenLevel();
/* 868 */     if (raidOmenLvl == 2) {
/* 869 */       return 0.1F;
/*     */     }
/* 871 */     if (raidOmenLvl == 3) {
/* 872 */       return 0.25F;
/*     */     }
/* 874 */     if (raidOmenLvl == 4) {
/* 875 */       return 0.5F;
/*     */     }
/* 877 */     if (raidOmenLvl == 5) {
/* 878 */       return 0.75F;
/*     */     }
/* 880 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/* 884 */   public void addHeroOfTheVillage(Entity killer) { this.heroesOfTheVillage.add(killer.getUUID()); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\raid\Raid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */