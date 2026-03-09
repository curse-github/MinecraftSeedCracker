/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ 
/*     */ public class TrialSpawnerStateData {
/*     */   private static final String TAG_SPAWN_DATA = "spawn_data";
/*     */   private static final String TAG_NEXT_MOB_SPAWNS_AT = "next_mob_spawns_at";
/*     */   private static final int DELAY_BETWEEN_PLAYER_SCANS = 20;
/*     */   private static final int TRIAL_OMEN_PER_BAD_OMEN_LEVEL = 18000;
/*  49 */   final Set<UUID> detectedPlayers = new HashSet();
/*  50 */   final Set<UUID> currentMobs = new HashSet();
/*     */   long cooldownEndsAt;
/*     */   long nextMobSpawnsAt;
/*     */   int totalMobsSpawned;
/*  54 */   Optional<SpawnData> nextSpawnData = Optional.empty();
/*  55 */   Optional<ResourceKey<LootTable>> ejectingLootTable = Optional.empty();
/*     */   private Entity displayEntity;
/*     */   private WeightedList<ItemStack> dispensing;
/*     */   double spin;
/*     */   double oSpin;
/*     */   
/*     */   public Packed pack() {
/*  62 */     return new Packed(
/*  63 */         Set.copyOf(this.detectedPlayers), 
/*  64 */         Set.copyOf(this.currentMobs), this.cooldownEndsAt, this.nextMobSpawnsAt, this.totalMobsSpawned, this.nextSpawnData, this.ejectingLootTable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(Packed packed) {
/*  74 */     this.detectedPlayers.clear();
/*  75 */     this.detectedPlayers.addAll(packed.detectedPlayers);
/*     */     
/*  77 */     this.currentMobs.clear();
/*  78 */     this.currentMobs.addAll(packed.currentMobs);
/*     */     
/*  80 */     this.cooldownEndsAt = packed.cooldownEndsAt;
/*  81 */     this.nextMobSpawnsAt = packed.nextMobSpawnsAt;
/*  82 */     this.totalMobsSpawned = packed.totalMobsSpawned;
/*  83 */     this.nextSpawnData = packed.nextSpawnData;
/*  84 */     this.ejectingLootTable = packed.ejectingLootTable;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  88 */     this.currentMobs.clear();
/*  89 */     this.nextSpawnData = Optional.empty();
/*  90 */     resetStatistics();
/*     */   }
/*     */   
/*     */   public void resetStatistics() {
/*  94 */     this.detectedPlayers.clear();
/*  95 */     this.totalMobsSpawned = 0;
/*  96 */     this.nextMobSpawnsAt = 0L;
/*  97 */     this.cooldownEndsAt = 0L;
/*     */   }
/*     */   
/*     */   public boolean hasMobToSpawn(TrialSpawner trialSpawner, RandomSource random) {
/* 101 */     boolean hasNextMobToSpawn = getOrCreateNextSpawnData(trialSpawner, random).getEntityToSpawn().getString("id").isPresent();
/* 102 */     return (hasNextMobToSpawn || !trialSpawner.activeConfig().spawnPotentialsDefinition().isEmpty());
/*     */   }
/*     */ 
/*     */   
/* 106 */   public boolean hasFinishedSpawningAllMobs(TrialSpawnerConfig config, int additionalPlayers) { return (this.totalMobsSpawned >= config.calculateTargetTotalMobs(additionalPlayers)); }
/*     */ 
/*     */ 
/*     */   
/* 110 */   public boolean haveAllCurrentMobsDied() { return this.currentMobs.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 114 */   public boolean isReadyToSpawnNextMob(ServerLevel serverLevel, TrialSpawnerConfig config, int additionalPlayers) { return (serverLevel.getGameTime() >= this.nextMobSpawnsAt && this.currentMobs.size() < config.calculateTargetSimultaneousMobs(additionalPlayers)); }
/*     */ 
/*     */   
/*     */   public int countAdditionalPlayers(BlockPos pos) {
/* 118 */     if (this.detectedPlayers.isEmpty()) {
/* 119 */       Util.logAndPauseIfInIde("Trial Spawner at " + String.valueOf(pos) + " has no detected players");
/*     */     }
/* 121 */     return Math.max(0, this.detectedPlayers.size() - 1);
/*     */   }
/*     */   
/*     */   public void tryDetectPlayers(ServerLevel level, BlockPos pos, TrialSpawner trialSpawner) {
/* 125 */     boolean becameOminous, isThrottled = ((pos.asLong() + level.getGameTime()) % 20L != 0L);
/* 126 */     if (isThrottled) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if (trialSpawner.getState().equals(TrialSpawnerState.COOLDOWN) && trialSpawner.isOminous()) {
/*     */       return;
/*     */     }
/*     */     
/* 134 */     List<UUID> inLineOfSightPlayers = trialSpawner.getPlayerDetector().detect(level, trialSpawner.getEntitySelector(), pos, trialSpawner.getRequiredPlayerRange(), true);
/*     */ 
/*     */     
/* 137 */     if (trialSpawner.isOminous() || inLineOfSightPlayers.isEmpty()) {
/* 138 */       becameOminous = false;
/*     */     } else {
/* 140 */       Optional<Pair<Player, Holder<MobEffect>>> playerWithOminousEffect = findPlayerWithOminousEffect(level, inLineOfSightPlayers);
/* 141 */       playerWithOminousEffect.ifPresent(playerAndEffect -> {
/* 142 */             Player player = (Player)playerAndEffect.getFirst();
/* 143 */             if (playerAndEffect.getSecond() == MobEffects.BAD_OMEN) {
/* 144 */               transformBadOmenIntoTrialOmen(player);
/*     */             }
/* 146 */             level.levelEvent(3020, BlockPos.containing(player.getEyePosition()), 0);
/* 147 */             trialSpawner.applyOminous(level, pos);
/*     */           });
/* 149 */       becameOminous = playerWithOminousEffect.isPresent();
/*     */     } 
/*     */     
/* 152 */     if (trialSpawner.getState().equals(TrialSpawnerState.COOLDOWN) && !becameOminous) {
/*     */       return;
/*     */     }
/*     */     
/* 156 */     boolean isSearchingForFirstPlayer = (trialSpawner.getStateData()).detectedPlayers.isEmpty();
/*     */ 
/*     */     
/* 159 */     List<UUID> foundPlayers = isSearchingForFirstPlayer ? inLineOfSightPlayers : trialSpawner.getPlayerDetector().detect(level, trialSpawner.getEntitySelector(), pos, trialSpawner.getRequiredPlayerRange(), false);
/*     */     
/* 161 */     if (this.detectedPlayers.addAll(foundPlayers)) {
/*     */       
/* 163 */       this.nextMobSpawnsAt = Math.max(level.getGameTime() + 40L, this.nextMobSpawnsAt);
/*     */       
/* 165 */       if (!becameOminous) {
/* 166 */         int event = trialSpawner.isOminous() ? 3019 : 3013;
/* 167 */         level.levelEvent(event, pos, this.detectedPlayers.size());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Pair<Player, Holder<MobEffect>>> findPlayerWithOminousEffect(ServerLevel level, List<UUID> inLineOfSightPlayers) {
/* 173 */     Player playerWithBadOmen = null;
/*     */     
/* 175 */     for (UUID playerUuid : inLineOfSightPlayers) {
/* 176 */       Player player = level.getPlayerByUUID(playerUuid);
/* 177 */       if (player == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 181 */       Holder<MobEffect> trialOmen = MobEffects.TRIAL_OMEN;
/* 182 */       if (player.hasEffect(trialOmen)) {
/* 183 */         return Optional.of(Pair.of(player, trialOmen));
/*     */       }
/*     */       
/* 186 */       if (player.hasEffect(MobEffects.BAD_OMEN)) {
/* 187 */         playerWithBadOmen = player;
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return Optional.ofNullable(playerWithBadOmen).map(player -> Pair.of(player, MobEffects.BAD_OMEN));
/*     */   }
/*     */   
/*     */   public void resetAfterBecomingOminous(TrialSpawner trialSpawner, ServerLevel level) {
/* 195 */     Objects.requireNonNull(level); this.currentMobs.stream().map(level::getEntity).forEach(entity -> {
/* 196 */           if (entity == null) {
/*     */             return;
/*     */           }
/*     */           
/* 200 */           level.levelEvent(3012, entity.blockPosition(), TrialSpawner.FlameParticle.NORMAL.encode());
/* 201 */           if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 202 */             mob.dropPreservedEquipment(level); }
/*     */           
/* 204 */           entity.remove(Entity.RemovalReason.DISCARDED);
/*     */         });
/*     */     
/* 207 */     if (!trialSpawner.ominousConfig().spawnPotentialsDefinition().isEmpty()) {
/* 208 */       this.nextSpawnData = Optional.empty();
/*     */     }
/*     */     
/* 211 */     this.totalMobsSpawned = 0;
/* 212 */     this.currentMobs.clear();
/* 213 */     this.nextMobSpawnsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenSpawn();
/* 214 */     trialSpawner.markUpdated();
/* 215 */     this.cooldownEndsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenItemSpawners();
/*     */   }
/*     */   
/*     */   private static void transformBadOmenIntoTrialOmen(Player player) {
/* 219 */     MobEffectInstance badOmen = player.getEffect(MobEffects.BAD_OMEN);
/* 220 */     if (badOmen == null) {
/*     */       return;
/*     */     }
/*     */     
/* 224 */     int amplifier = badOmen.getAmplifier() + 1;
/* 225 */     int duration = 18000 * amplifier;
/* 226 */     player.removeEffect(MobEffects.BAD_OMEN);
/* 227 */     player.addEffect(new MobEffectInstance(MobEffects.TRIAL_OMEN, duration, 0));
/*     */   }
/*     */   
/*     */   public boolean isReadyToOpenShutter(ServerLevel serverLevel, float delayBeforeOpen, int targetCooldownLength) {
/* 231 */     long cooldownStartedAt = this.cooldownEndsAt - targetCooldownLength;
/* 232 */     return ((float)serverLevel.getGameTime() >= (float)cooldownStartedAt + delayBeforeOpen);
/*     */   }
/*     */   
/*     */   public boolean isReadyToEjectItems(ServerLevel serverLevel, float timeBetweenEjections, int targetCooldownLength) {
/* 236 */     long cooldownStartedAt = this.cooldownEndsAt - targetCooldownLength;
/* 237 */     return ((float)(serverLevel.getGameTime() - cooldownStartedAt) % timeBetweenEjections == 0.0F);
/*     */   }
/*     */ 
/*     */   
/* 241 */   public boolean isCooldownFinished(ServerLevel serverLevel) { return (serverLevel.getGameTime() >= this.cooldownEndsAt); }
/*     */ 
/*     */   
/*     */   protected SpawnData getOrCreateNextSpawnData(TrialSpawner trialSpawner, RandomSource random) {
/* 245 */     if (this.nextSpawnData.isPresent()) {
/* 246 */       return (SpawnData)this.nextSpawnData.get();
/*     */     }
/* 248 */     WeightedList<SpawnData> spawnPotentials = trialSpawner.activeConfig().spawnPotentialsDefinition();
/* 249 */     Optional<SpawnData> selected = spawnPotentials.isEmpty() ? this.nextSpawnData : spawnPotentials.getRandom(random);
/* 250 */     this.nextSpawnData = Optional.of((SpawnData)selected.orElseGet(SpawnData::new));
/* 251 */     trialSpawner.markUpdated();
/* 252 */     return (SpawnData)this.nextSpawnData.get();
/*     */   }
/*     */   
/*     */   public Entity getOrCreateDisplayEntity(TrialSpawner trialSpawner, Level level, TrialSpawnerState state) {
/* 256 */     if (!state.hasSpinningMob()) {
/* 257 */       return null;
/*     */     }
/*     */     
/* 260 */     if (this.displayEntity == null) {
/* 261 */       CompoundTag entityToSpawn = getOrCreateNextSpawnData(trialSpawner, level.getRandom()).getEntityToSpawn();
/* 262 */       if (entityToSpawn.getString("id").isPresent()) {
/* 263 */         this.displayEntity = EntityType.loadEntityRecursive(entityToSpawn, level, EntitySpawnReason.TRIAL_SPAWNER, EntityProcessor.NOP);
/*     */       }
/*     */     } 
/*     */     
/* 267 */     return this.displayEntity;
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(TrialSpawnerState state) {
/* 271 */     CompoundTag tag = new CompoundTag();
/*     */     
/* 273 */     if (state == TrialSpawnerState.ACTIVE) {
/* 274 */       tag.putLong("next_mob_spawns_at", this.nextMobSpawnsAt);
/*     */     }
/*     */     
/* 277 */     this.nextSpawnData.ifPresent(spawnData -> 
/* 278 */         tag.store("spawn_data", SpawnData.CODEC, spawnData));
/*     */ 
/*     */     
/* 281 */     return tag;
/*     */   }
/*     */ 
/*     */   
/* 285 */   public double getSpin() { return this.spin; }
/*     */ 
/*     */ 
/*     */   
/* 289 */   public double getOSpin() { return this.oSpin; }
/*     */ 
/*     */   
/*     */   WeightedList<ItemStack> getDispensingItems(ServerLevel level, TrialSpawnerConfig config, BlockPos pos) {
/* 293 */     if (this.dispensing != null) {
/* 294 */       return this.dispensing;
/*     */     }
/*     */     
/* 297 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(config.itemsToDropWhenOminous());
/* 298 */     LootParams params = (new LootParams.Builder(level)).create(LootContextParamSets.EMPTY);
/*     */     
/* 300 */     long simplePositionalSeed = lowResolutionPosition(level, pos);
/* 301 */     ObjectArrayList<ItemStack> lootDrops = lootTable.getRandomItems(params, simplePositionalSeed);
/*     */     
/* 303 */     if (lootDrops.isEmpty()) {
/* 304 */       return WeightedList.of();
/*     */     }
/*     */     
/* 307 */     WeightedList.Builder<ItemStack> builder = WeightedList.builder();
/*     */     
/* 309 */     for (ObjectListIterator objectListIterator = lootDrops.iterator(); objectListIterator.hasNext(); ) { ItemStack drop = (ItemStack)objectListIterator.next();
/* 310 */       builder.add(drop.copyWithCount(1), drop.getCount()); }
/*     */ 
/*     */     
/* 313 */     this.dispensing = builder.build();
/* 314 */     return this.dispensing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long lowResolutionPosition(ServerLevel level, BlockPos pos) {
/* 325 */     BlockPos lowResolutionPosition = new BlockPos(Mth.floor(pos.getX() / 30.0F), Mth.floor(pos.getY() / 20.0F), Mth.floor(pos.getZ() / 30.0F));
/*     */ 
/*     */     
/* 328 */     return level.getSeed() + lowResolutionPosition.asLong();
/*     */   }
/*     */   public static final class Packed extends Record { private final Set<UUID> detectedPlayers; private final Set<UUID> currentMobs; private final long cooldownEndsAt;
/* 331 */     public Packed(Set<UUID> detectedPlayers, Set<UUID> currentMobs, long cooldownEndsAt, long nextMobSpawnsAt, int totalMobsSpawned, Optional<SpawnData> nextSpawnData, Optional<ResourceKey<LootTable>> ejectingLootTable) { this.detectedPlayers = detectedPlayers; this.currentMobs = currentMobs; this.cooldownEndsAt = cooldownEndsAt; this.nextMobSpawnsAt = nextMobSpawnsAt; this.totalMobsSpawned = totalMobsSpawned; this.nextSpawnData = nextSpawnData; this.ejectingLootTable = ejectingLootTable; } private final long nextMobSpawnsAt; private final int totalMobsSpawned; private final Optional<SpawnData> nextSpawnData; private final Optional<ResourceKey<LootTable>> ejectingLootTable; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #331	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #331	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #331	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/* 331 */       //   0	8	1	o	Ljava/lang/Object; } public Set<UUID> detectedPlayers() { return this.detectedPlayers; } public Set<UUID> currentMobs() { return this.currentMobs; } public long cooldownEndsAt() { return this.cooldownEndsAt; } public long nextMobSpawnsAt() { return this.nextMobSpawnsAt; } public int totalMobsSpawned() { return this.totalMobsSpawned; } public Optional<SpawnData> nextSpawnData() { return this.nextSpawnData; } public Optional<ResourceKey<LootTable>> ejectingLootTable() { return this.ejectingLootTable; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     public static final MapCodec<Packed> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(UUIDUtil.CODEC_SET
/* 341 */           .lenientOptionalFieldOf("registered_players", Set.of()).forGetter(Packed::detectedPlayers), UUIDUtil.CODEC_SET
/* 342 */           .lenientOptionalFieldOf("current_mobs", Set.of()).forGetter(Packed::currentMobs), Codec.LONG
/* 343 */           .lenientOptionalFieldOf("cooldown_ends_at", Long.valueOf(0L)).forGetter(Packed::cooldownEndsAt), Codec.LONG
/* 344 */           .lenientOptionalFieldOf("next_mob_spawns_at", Long.valueOf(0L)).forGetter(Packed::nextMobSpawnsAt), 
/* 345 */           Codec.intRange(0, 2147483647).lenientOptionalFieldOf("total_mobs_spawned", Integer.valueOf(0)).forGetter(Packed::totalMobsSpawned), SpawnData.CODEC
/* 346 */           .lenientOptionalFieldOf("spawn_data").forGetter(Packed::nextSpawnData), LootTable.KEY_CODEC
/* 347 */           .lenientOptionalFieldOf("ejecting_loot_table").forGetter(Packed::ejectingLootTable))
/* 348 */         .apply(i, Packed::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerStateData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */