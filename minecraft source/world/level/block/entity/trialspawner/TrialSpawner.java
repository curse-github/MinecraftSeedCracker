/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.block.TrialSpawnerBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class TrialSpawner {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */ 
/*     */   
/*     */   public static final int DETECT_PLAYER_SPAWN_BUFFER = 40;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_TARGET_COOLDOWN_LENGTH = 36000;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_PLAYER_SCAN_RANGE = 14;
/*     */ 
/*     */   
/*     */   private static final int MAX_MOB_TRACKING_DISTANCE = 47;
/*     */   
/*  72 */   private static final int MAX_MOB_TRACKING_DISTANCE_SQR = Mth.square(47); private static final float SPAWNING_AMBIENT_SOUND_CHANCE = 0.02F; private final TrialSpawnerStateData data; private FullConfig config; private final StateAccessor stateAccessor; private PlayerDetector playerDetector; private final PlayerDetector.EntitySelector entitySelector; private boolean overridePeacefulAndMobSpawnRule; private boolean isOminous;
/*     */   
/*     */   public TrialSpawner(FullConfig config, StateAccessor stateAccessor, PlayerDetector playerDetector, PlayerDetector.EntitySelector entitySelector) {
/*  75 */     this.data = new TrialSpawnerStateData();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  85 */     this.config = config;
/*  86 */     this.stateAccessor = stateAccessor;
/*  87 */     this.playerDetector = playerDetector;
/*  88 */     this.entitySelector = entitySelector;
/*     */   }
/*     */ 
/*     */   
/*  92 */   public TrialSpawnerConfig activeConfig() { return this.isOminous ? (TrialSpawnerConfig)this.config.ominous().value() : (TrialSpawnerConfig)this.config.normal.value(); }
/*     */ 
/*     */ 
/*     */   
/*  96 */   public TrialSpawnerConfig normalConfig() { return (TrialSpawnerConfig)this.config.normal.value(); }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public TrialSpawnerConfig ominousConfig() { return (TrialSpawnerConfig)this.config.ominous.value(); }
/*     */ 
/*     */   
/*     */   public void load(ValueInput input) {
/* 104 */     Objects.requireNonNull(this.data); input.read(TrialSpawnerStateData.Packed.MAP_CODEC).ifPresent(this.data::apply);
/* 105 */     this.config = (FullConfig)input.read(FullConfig.MAP_CODEC).orElse(FullConfig.DEFAULT);
/*     */   }
/*     */   
/*     */   public void store(ValueOutput output) {
/* 109 */     output.store(TrialSpawnerStateData.Packed.MAP_CODEC, this.data.pack());
/* 110 */     output.store(FullConfig.MAP_CODEC, this.config);
/*     */   }
/*     */   
/*     */   public void applyOminous(ServerLevel level, BlockPos spawnerPos) {
/* 114 */     level.setBlock(spawnerPos, (BlockState)level.getBlockState(spawnerPos).setValue(TrialSpawnerBlock.OMINOUS, Boolean.valueOf(true)), 3);
/* 115 */     level.levelEvent(3020, spawnerPos, 1);
/* 116 */     this.isOminous = true;
/* 117 */     this.data.resetAfterBecomingOminous(this, level);
/*     */   }
/*     */   
/*     */   public void removeOminous(ServerLevel level, BlockPos spawnerPos) {
/* 121 */     level.setBlock(spawnerPos, (BlockState)level.getBlockState(spawnerPos).setValue(TrialSpawnerBlock.OMINOUS, Boolean.valueOf(false)), 3);
/* 122 */     this.isOminous = false;
/*     */   }
/*     */ 
/*     */   
/* 126 */   public boolean isOminous() { return this.isOminous; }
/*     */ 
/*     */ 
/*     */   
/* 130 */   public int getTargetCooldownLength() { return this.config.targetCooldownLength; }
/*     */ 
/*     */ 
/*     */   
/* 134 */   public int getRequiredPlayerRange() { return this.config.requiredPlayerRange; }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public TrialSpawnerState getState() { return this.stateAccessor.getState(); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public TrialSpawnerStateData getStateData() { return this.data; }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public void setState(Level level, TrialSpawnerState state) { this.stateAccessor.setState(level, state); }
/*     */ 
/*     */ 
/*     */   
/* 150 */   public void markUpdated() { this.stateAccessor.markUpdated(); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public PlayerDetector getPlayerDetector() { return this.playerDetector; }
/*     */ 
/*     */ 
/*     */   
/* 158 */   public PlayerDetector.EntitySelector getEntitySelector() { return this.entitySelector; }
/*     */ 
/*     */   
/*     */   public boolean canSpawnInLevel(ServerLevel level) {
/* 162 */     if (!((Boolean)level.getGameRules().get(GameRules.SPAWNER_BLOCKS_WORK)).booleanValue()) {
/* 163 */       return false;
/*     */     }
/*     */     
/* 166 */     if (this.overridePeacefulAndMobSpawnRule) {
/* 167 */       return true;
/*     */     }
/*     */     
/* 170 */     if (level.getDifficulty() == Difficulty.PEACEFUL) {
/* 171 */       return false;
/*     */     }
/*     */     
/* 174 */     return ((Boolean)level.getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue();
/*     */   }
/*     */   
/*     */   public Optional<UUID> spawnMob(ServerLevel level, BlockPos spawnerPos) {
/* 178 */     RandomSource random = level.getRandom();
/* 179 */     SpawnData nextSpawnData = this.data.getOrCreateNextSpawnData(this, level.getRandom());
/* 180 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(() -> "spawner@" + String.valueOf(spawnerPos), LOGGER); 
/* 181 */     try { ValueInput input = TagValueInput.create(reporter, level.registryAccess(), nextSpawnData.entityToSpawn());
/* 182 */       Optional<EntityType<?>> entityType = EntityType.by(input);
/* 183 */       if (entityType.isEmpty())
/* 184 */       { Optional optional1 = Optional.empty();
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
/* 249 */         reporter.close(); return optional1; }  Vec3 spawnPos = (Vec3)input.read("Pos", Vec3.CODEC).orElseGet(() -> { TrialSpawnerConfig activeConfig = activeConfig(); return new Vec3(spawnerPos.getX() + (random.nextDouble() - random.nextDouble()) * activeConfig.spawnRange() + 0.5D, (spawnerPos.getY() + random.nextInt(3) - 1), spawnerPos.getZ() + (random.nextDouble() - random.nextDouble()) * activeConfig.spawnRange() + 0.5D); }); if (!level.noCollision(((EntityType)entityType.get()).getSpawnAABB(spawnPos.x, spawnPos.y, spawnPos.z))) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  if (!inLineOfSight(level, spawnerPos.getCenter(), spawnPos)) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  BlockPos spawnBlockPos = BlockPos.containing(spawnPos); if (!SpawnPlacements.checkSpawnRules((EntityType)entityType.get(), level, EntitySpawnReason.TRIAL_SPAWNER, spawnBlockPos, level.getRandom())) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  if (nextSpawnData.getCustomSpawnRules().isPresent()) { SpawnData.CustomSpawnRules customSpawnRules = (SpawnData.CustomSpawnRules)nextSpawnData.getCustomSpawnRules().get(); if (!customSpawnRules.isValidPosition(spawnBlockPos, level)) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  }  Entity entity = EntityType.loadEntityRecursive(input, level, EntitySpawnReason.TRIAL_SPAWNER, e -> { e.snapTo(spawnPos.x, spawnPos.y, spawnPos.z, random.nextFloat() * 360.0F, 0.0F); return e; }); if (entity == null) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  if (entity instanceof Mob) { Mob mob = (Mob)entity; if (!mob.checkSpawnObstruction(level)) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  boolean hasNoConfiguration = (nextSpawnData.getEntityToSpawn().size() == 1 && nextSpawnData.getEntityToSpawn().getString("id").isPresent()); if (hasNoConfiguration) mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.TRIAL_SPAWNER, null);  mob.setPersistenceRequired(); Objects.requireNonNull(mob); nextSpawnData.getEquipment().ifPresent(mob::equip); }  if (!level.tryAddFreshEntityWithPassengers(entity)) { Optional optional1 = Optional.empty(); reporter.close(); return optional1; }  FlameParticle flameParticle = this.isOminous ? FlameParticle.OMINOUS : FlameParticle.NORMAL; level.levelEvent(3011, spawnerPos, flameParticle.encode()); level.levelEvent(3012, spawnBlockPos, flameParticle.encode()); level.gameEvent(entity, GameEvent.ENTITY_PLACE, spawnBlockPos); Optional optional = Optional.of(entity.getUUID()); reporter.close(); return optional; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 253 */      } public void ejectReward(ServerLevel level, BlockPos pos, ResourceKey<LootTable> ejectingLootTable) { LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(ejectingLootTable);
/* 254 */     LootParams params = (new LootParams.Builder(level)).create(LootContextParamSets.EMPTY);
/*     */     
/* 256 */     ObjectArrayList<ItemStack> lootDrops = lootTable.getRandomItems(params);
/* 257 */     if (!lootDrops.isEmpty()) {
/* 258 */       for (ObjectListIterator objectListIterator = lootDrops.iterator(); objectListIterator.hasNext(); ) { ItemStack item = (ItemStack)objectListIterator.next();
/* 259 */         DefaultDispenseItemBehavior.spawnItem(level, item, 2, Direction.UP, Vec3.atBottomCenterOf(pos).relative(Direction.UP, 1.2D)); }
/*     */ 
/*     */       
/* 262 */       level.levelEvent(3014, pos, 0);
/*     */     }  }
/*     */ 
/*     */   
/*     */   public void tickClient(Level level, BlockPos spawnerPos, boolean isOminous) {
/* 267 */     TrialSpawnerState currentState = getState();
/* 268 */     currentState.emitParticles(level, spawnerPos, isOminous);
/*     */     
/* 270 */     if (currentState.hasSpinningMob()) {
/* 271 */       double spawnDelay = Math.max(0L, this.data.nextMobSpawnsAt - level.getGameTime());
/* 272 */       this.data.oSpin = this.data.spin;
/* 273 */       this.data.spin = (this.data.spin + currentState.spinningMobSpeed() / (spawnDelay + 200.0D)) % 360.0D;
/*     */     } 
/*     */     
/* 276 */     if (currentState.isCapableOfSpawning()) {
/* 277 */       RandomSource random = level.getRandom();
/* 278 */       if (random.nextFloat() <= 0.02F) {
/* 279 */         SoundEvent ambientSound = isOminous ? SoundEvents.TRIAL_SPAWNER_AMBIENT_OMINOUS : SoundEvents.TRIAL_SPAWNER_AMBIENT;
/* 280 */         level.playLocalSound(spawnerPos, ambientSound, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tickServer(ServerLevel serverLevel, BlockPos spawnerPos, boolean isOminous) {
/* 286 */     this.isOminous = isOminous;
/* 287 */     TrialSpawnerState currentState = getState();
/*     */     
/* 289 */     if (this.data.currentMobs.removeIf(id -> shouldMobBeUntracked(serverLevel, spawnerPos, id))) {
/* 290 */       this.data.nextMobSpawnsAt = serverLevel.getGameTime() + activeConfig().ticksBetweenSpawn();
/*     */     }
/*     */     
/* 293 */     TrialSpawnerState nextState = currentState.tickAndGetNext(spawnerPos, this, serverLevel);
/*     */     
/* 295 */     if (nextState != currentState) {
/* 296 */       setState(serverLevel, nextState);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean shouldMobBeUntracked(ServerLevel serverLevel, BlockPos spawnerPos, UUID id) {
/* 301 */     Entity entity = serverLevel.getEntity(id);
/* 302 */     return (entity == null || 
/* 303 */       !entity.isAlive() || 
/* 304 */       !entity.level().dimension().equals(serverLevel.dimension()) || entity
/* 305 */       .blockPosition().distSqr(spawnerPos) > MAX_MOB_TRACKING_DISTANCE_SQR);
/*     */   }
/*     */   
/*     */   private static boolean inLineOfSight(Level level, Vec3 origin, Vec3 dest) {
/* 309 */     BlockHitResult hitResult = level.clip(new ClipContext(dest, origin, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 310 */     return (hitResult.getBlockPos().equals(BlockPos.containing(origin)) || hitResult.getType() == HitResult.Type.MISS);
/*     */   }
/*     */   
/*     */   public static void addSpawnParticles(Level level, BlockPos pos, RandomSource random, SimpleParticleType particleType) {
/* 314 */     for (int i = 0; i < 20; i++) {
/* 315 */       double xP = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 316 */       double yP = pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 317 */       double zP = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/*     */       
/* 319 */       level.addParticle(ParticleTypes.SMOKE, xP, yP, zP, 0.0D, 0.0D, 0.0D);
/* 320 */       level.addParticle(particleType, xP, yP, zP, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addBecomeOminousParticles(Level level, BlockPos pos, RandomSource random) {
/* 325 */     for (int i = 0; i < 20; i++) {
/* 326 */       double xP = pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 327 */       double yP = pos.getY() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/* 328 */       double zP = pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 2.0D;
/*     */       
/* 330 */       double xa = random.nextGaussian() * 0.02D;
/* 331 */       double ya = random.nextGaussian() * 0.02D;
/* 332 */       double za = random.nextGaussian() * 0.02D;
/*     */       
/* 334 */       level.addParticle(ParticleTypes.TRIAL_OMEN, xP, yP, zP, xa, ya, za);
/* 335 */       level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, xP, yP, zP, xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addDetectPlayerParticles(Level level, BlockPos pos, RandomSource random, int data, ParticleOptions type) {
/* 340 */     for (int i = 0; i < 30 + Math.min(data, 10) * 5; i++) {
/* 341 */       double spreadX = (2.0F * random.nextFloat() - 1.0F) * 0.65D;
/* 342 */       double spreadZ = (2.0F * random.nextFloat() - 1.0F) * 0.65D;
/* 343 */       double xP = pos.getX() + 0.5D + spreadX;
/* 344 */       double yP = pos.getY() + 0.1D + random.nextFloat() * 0.8D;
/* 345 */       double zP = pos.getZ() + 0.5D + spreadZ;
/*     */       
/* 347 */       level.addParticle(type, xP, yP, zP, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void addEjectItemParticles(Level level, BlockPos pos, RandomSource random) {
/* 352 */     for (int i = 0; i < 20; i++) {
/* 353 */       double xp = pos.getX() + 0.4D + random.nextDouble() * 0.2D;
/* 354 */       double yp = pos.getY() + 0.4D + random.nextDouble() * 0.2D;
/* 355 */       double zp = pos.getZ() + 0.4D + random.nextDouble() * 0.2D;
/* 356 */       double xa = random.nextGaussian() * 0.02D;
/* 357 */       double ya = random.nextGaussian() * 0.02D;
/* 358 */       double za = random.nextGaussian() * 0.02D;
/* 359 */       level.addParticle(ParticleTypes.SMALL_FLAME, xp, yp, zp, xa, ya, za * 0.25D);
/* 360 */       level.addParticle(ParticleTypes.SMOKE, xp, yp, zp, xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void overrideEntityToSpawn(EntityType<?> type, Level level) {
/* 365 */     this.data.reset();
/* 366 */     this.config = this.config.overrideEntity(type);
/* 367 */     setState(level, TrialSpawnerState.INACTIVE);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   @VisibleForTesting
/* 373 */   public void setPlayerDetector(PlayerDetector playerDetector) { this.playerDetector = playerDetector; }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   @VisibleForTesting
/* 379 */   public void overridePeacefulAndMobSpawnRule() { this.overridePeacefulAndMobSpawnRule = true; }
/*     */   
/*     */   public enum FlameParticle
/*     */   {
/* 383 */     NORMAL(ParticleTypes.FLAME),
/* 384 */     OMINOUS(ParticleTypes.SOUL_FIRE_FLAME);
/*     */     
/*     */     public final SimpleParticleType particleType;
/*     */ 
/*     */     
/* 389 */     FlameParticle(SimpleParticleType particleType) { this.particleType = particleType; }
/*     */ 
/*     */     
/*     */     public static FlameParticle decode(int data) {
/* 393 */       FlameParticle[] arrayOfFlameParticle = values();
/* 394 */       if (data > arrayOfFlameParticle.length || data < 0) {
/* 395 */         return NORMAL;
/*     */       }
/* 397 */       return arrayOfFlameParticle[data];
/*     */     }
/*     */ 
/*     */     
/* 401 */     public int encode() { return ordinal(); }
/*     */   } public static interface StateAccessor { void setState(Level param1Level, TrialSpawnerState param1TrialSpawnerState); TrialSpawnerState getState();
/*     */     void markUpdated(); }
/*     */   public static final class FullConfig extends Record { private final Holder<TrialSpawnerConfig> normal; private final Holder<TrialSpawnerConfig> ominous;
/* 405 */     public FullConfig(Holder<TrialSpawnerConfig> normal, Holder<TrialSpawnerConfig> ominous, int targetCooldownLength, int requiredPlayerRange) { this.normal = normal; this.ominous = ominous; this.targetCooldownLength = targetCooldownLength; this.requiredPlayerRange = requiredPlayerRange; } private final int targetCooldownLength; private final int requiredPlayerRange; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #405	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;
/* 405 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<TrialSpawnerConfig> normal() { return this.normal; } public Holder<TrialSpawnerConfig> ominous() { return this.ominous; } public int targetCooldownLength() { return this.targetCooldownLength; } public int requiredPlayerRange() { return this.requiredPlayerRange; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     public static final MapCodec<FullConfig> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(TrialSpawnerConfig.CODEC
/* 412 */           .optionalFieldOf("normal_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(FullConfig::normal), TrialSpawnerConfig.CODEC
/* 413 */           .optionalFieldOf("ominous_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(FullConfig::ominous), ExtraCodecs.NON_NEGATIVE_INT
/* 414 */           .optionalFieldOf("target_cooldown_length", Integer.valueOf(36000)).forGetter(FullConfig::targetCooldownLength), 
/* 415 */           Codec.intRange(1, 128).optionalFieldOf("required_player_range", Integer.valueOf(14)).forGetter(FullConfig::requiredPlayerRange))
/* 416 */         .apply(i, FullConfig::new));
/*     */     
/* 418 */     public static final FullConfig DEFAULT = new FullConfig(
/* 419 */         Holder.direct(TrialSpawnerConfig.DEFAULT), 
/* 420 */         Holder.direct(TrialSpawnerConfig.DEFAULT), 36000, 14);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FullConfig overrideEntity(EntityType<?> type) {
/* 426 */       return new FullConfig(
/* 427 */           Holder.direct(((TrialSpawnerConfig)this.normal.value()).withSpawning(type)), 
/* 428 */           Holder.direct(((TrialSpawnerConfig)this.ominous.value()).withSpawning(type)), this.targetCooldownLength, this.requiredPlayerRange);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */