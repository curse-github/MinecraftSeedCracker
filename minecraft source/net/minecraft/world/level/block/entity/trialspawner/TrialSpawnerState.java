/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.OminousItemSpawner;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public static enum TrialSpawnerState implements StringRepresentable {
/*     */   private static final float DELAY_BEFORE_EJECT_AFTER_KILLING_LAST_MOB = 40.0F;
/*     */   private static final int TIME_BETWEEN_EACH_EJECTION;
/*  34 */   INACTIVE("inactive", 0, ParticleEmission.NONE, -1.0D, false),
/*  35 */   WAITING_FOR_PLAYERS("waiting_for_players", 4, ParticleEmission.SMALL_FLAMES, 200.0D, true),
/*  36 */   ACTIVE("active", 8, ParticleEmission.FLAMES_AND_SMOKE, 1000.0D, true),
/*  37 */   WAITING_FOR_REWARD_EJECTION("waiting_for_reward_ejection", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  38 */   EJECTING_REWARD("ejecting_reward", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  39 */   COOLDOWN("cooldown", 0, ParticleEmission.SMOKE_INSIDE_AND_TOP_FACE, -1.0D, false); private final String name;
/*     */   
/*     */   static  {
/*  42 */     TIME_BETWEEN_EACH_EJECTION = Mth.floor(30.0F);
/*     */   }
/*     */   private final int lightLevel;
/*     */   private final double spinningMobSpeed;
/*     */   private final ParticleEmission particleEmission;
/*     */   private final boolean isCapableOfSpawning;
/*     */   
/*     */   TrialSpawnerState(String name, int lightLevel, ParticleEmission particleEmission, double spinningMobSpeed, boolean isCapableOfSpawning) {
/*  50 */     this.name = name;
/*  51 */     this.lightLevel = lightLevel;
/*  52 */     this.particleEmission = particleEmission;
/*  53 */     this.spinningMobSpeed = spinningMobSpeed;
/*  54 */     this.isCapableOfSpawning = isCapableOfSpawning;
/*     */   }
/*     */   TrialSpawnerState tickAndGetNext(BlockPos spawnerPos, TrialSpawner trialSpawner, ServerLevel serverLevel) {
/*     */     int additionalPlayers;
/*  58 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/*  59 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/*     */     
/*  61 */     switch (ordinal()) { default: throw new MatchException(null, null);case 0: return 
/*  62 */           (data.getOrCreateDisplayEntity(trialSpawner, serverLevel, WAITING_FOR_PLAYERS) == null) ? 
/*  63 */           this : 
/*  64 */           WAITING_FOR_PLAYERS;
/*     */       
/*     */       case 1:
/*  67 */         data.resetStatistics();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  73 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*  74 */         return !trialSpawner.canSpawnInLevel(serverLevel) ? this : (!data.hasMobToSpawn(trialSpawner, serverLevel.random) ? INACTIVE : (data.detectedPlayers.isEmpty() ? 
/*  75 */           this : 
/*  76 */           ACTIVE));
/*     */ 
/*     */       
/*     */       case 2:
/*  80 */         data.resetStatistics();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  88 */         additionalPlayers = data.countAdditionalPlayers(spawnerPos);
/*  89 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*     */         
/*  91 */         if (trialSpawner.isOminous()) {
/*  92 */           spawnOminousOminousItemSpawner(serverLevel, spawnerPos, trialSpawner);
/*     */         }
/*     */         
/*  95 */         if (data.hasFinishedSpawningAllMobs(config, additionalPlayers)) {
/*  96 */           if (data.haveAllCurrentMobsDied()) {
/*  97 */             data.cooldownEndsAt = serverLevel.getGameTime() + trialSpawner.getTargetCooldownLength();
/*  98 */             data.totalMobsSpawned = 0;
/*  99 */             data.nextMobSpawnsAt = 0L;
/*     */           }
/*     */         
/* 102 */         } else if (data.isReadyToSpawnNextMob(serverLevel, config, additionalPlayers)) {
/* 103 */           trialSpawner.spawnMob(serverLevel, spawnerPos).ifPresent(entityId -> {
/* 104 */                 data.currentMobs.add(entityId);
/* 105 */                 data.totalMobsSpawned++;
/* 106 */                 data.nextMobSpawnsAt = serverLevel.getGameTime() + config.ticksBetweenSpawn();
/*     */                 
/* 108 */                 config.spawnPotentialsDefinition().getRandom(serverLevel.getRandom()).ifPresent(());
/*     */               });
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 115 */         return !trialSpawner.canSpawnInLevel(serverLevel) ? WAITING_FOR_PLAYERS : (!data.hasMobToSpawn(trialSpawner, serverLevel.random) ? INACTIVE : this);
/*     */ 
/*     */       
/*     */       case 3:
/* 119 */         serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, SoundSource.BLOCKS);
/* 120 */         return data.isReadyToOpenShutter(serverLevel, 40.0F, trialSpawner.getTargetCooldownLength()) ? EJECTING_REWARD : 
/*     */           
/* 122 */           this;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 130 */         serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS);
/* 131 */         data.ejectingLootTable = Optional.empty();
/*     */ 
/*     */ 
/*     */         
/* 135 */         if (data.ejectingLootTable.isEmpty()) {
/* 136 */           data.ejectingLootTable = config.lootTablesToEject().getRandom(serverLevel.getRandom());
/*     */         }
/*     */         
/* 139 */         data.ejectingLootTable.ifPresent(lootTable -> trialSpawner.ejectReward(serverLevel, spawnerPos, lootTable));
/* 140 */         data.detectedPlayers.remove(data.detectedPlayers.iterator().next());
/* 141 */         return !data.isReadyToEjectItems(serverLevel, TIME_BETWEEN_EACH_EJECTION, trialSpawner.getTargetCooldownLength()) ? this : (data.detectedPlayers.isEmpty() ? COOLDOWN : this);
/*     */       case 5:
/*     */         break; }
/* 144 */      data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*     */ 
/*     */     
/* 147 */     data.totalMobsSpawned = 0;
/* 148 */     data.nextMobSpawnsAt = 0L;
/*     */ 
/*     */     
/* 151 */     trialSpawner.removeOminous(serverLevel, spawnerPos);
/* 152 */     data.reset();
/* 153 */     return !data.detectedPlayers.isEmpty() ? ACTIVE : (data.isCooldownFinished(serverLevel) ? WAITING_FOR_PLAYERS : 
/*     */       
/* 155 */       this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void spawnOminousOminousItemSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner) {
/* 161 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/* 162 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/*     */     
/* 164 */     ItemStack itemToDispense = (ItemStack)data.getDispensingItems(level, config, trialSpawnerPos).getRandom(level.random).orElse(ItemStack.EMPTY);
/* 165 */     if (itemToDispense.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     if (timeToSpawnItemSpawner(level, data)) {
/* 170 */       calculatePositionToSpawnSpawner(level, trialSpawnerPos, trialSpawner, data).ifPresent(pos -> {
/* 171 */             OminousItemSpawner itemSpawner = OminousItemSpawner.create(level, itemToDispense);
/* 172 */             itemSpawner.snapTo(pos);
/* 173 */             level.addFreshEntity(itemSpawner);
/* 174 */             float pitch = (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F;
/* 175 */             level.playSound(null, BlockPos.containing(pos), SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, SoundSource.BLOCKS, 1.0F, pitch);
/*     */             
/* 177 */             data.cooldownEndsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenItemSpawners();
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<Vec3> calculatePositionToSpawnSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner, TrialSpawnerStateData data) {
/* 184 */     Objects.requireNonNull(level);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     List<Player> nearbyPlayers = data.detectedPlayers.stream().map(level::getPlayerByUUID).filter(Objects::nonNull).filter(player -> (!player.isCreative() && !player.isSpectator() && player.isAlive() && player.distanceToSqr(trialSpawnerPos.getCenter()) <= Mth.square(trialSpawner.getRequiredPlayerRange()))).toList();
/*     */     
/* 192 */     if (nearbyPlayers.isEmpty()) {
/* 193 */       return Optional.empty();
/*     */     }
/*     */     
/* 196 */     Entity entity = selectEntityToSpawnItemAbove(nearbyPlayers, data.currentMobs, trialSpawner, trialSpawnerPos, level);
/*     */     
/* 198 */     if (entity == null) {
/* 199 */       return Optional.empty();
/*     */     }
/*     */     
/* 202 */     return calculatePositionAbove(entity, level);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> calculatePositionAbove(Entity entityToSpawnItemAbove, ServerLevel level) {
/* 206 */     Vec3 entityPos = entityToSpawnItemAbove.position();
/* 207 */     Vec3 trySpawnPos = entityPos.relative(Direction.UP, (entityToSpawnItemAbove.getBbHeight() + 2.0F + level.random.nextInt(4)));
/* 208 */     BlockHitResult hitResult = level.clip(new ClipContext(entityPos, trySpawnPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 209 */     Vec3 down = hitResult.getBlockPos().getCenter().relative(Direction.DOWN, 1.0D);
/* 210 */     BlockPos blockPosDown = BlockPos.containing(down);
/* 211 */     if (!level.getBlockState(blockPosDown).getCollisionShape(level, blockPosDown).isEmpty()) {
/* 212 */       return Optional.empty();
/*     */     }
/* 214 */     return Optional.of(down);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Entity selectEntityToSpawnItemAbove(List<Player> nearbyPlayers, Set<UUID> mobIds, TrialSpawner trialSpawner, BlockPos spawnerPos, ServerLevel level) {
/* 219 */     Objects.requireNonNull(level);
/*     */     
/* 221 */     Stream<Entity> nearbyMobs = mobIds.stream().map(level::getEntity).filter(Objects::nonNull).filter(target -> (target.isAlive() && target.distanceToSqr(spawnerPos.getCenter()) <= Mth.square(trialSpawner.getRequiredPlayerRange())));
/*     */     
/* 223 */     List<? extends Entity> eligibleEntities = level.random.nextBoolean() ? nearbyMobs.toList() : nearbyPlayers;
/*     */     
/* 225 */     if (eligibleEntities.isEmpty()) {
/* 226 */       return null;
/*     */     }
/*     */     
/* 229 */     if (eligibleEntities.size() == 1) {
/* 230 */       return (Entity)eligibleEntities.getFirst();
/*     */     }
/*     */     
/* 233 */     return (Entity)Util.getRandom(eligibleEntities, level.random);
/*     */   }
/*     */ 
/*     */   
/* 237 */   private boolean timeToSpawnItemSpawner(ServerLevel serverLevel, TrialSpawnerStateData data) { return (serverLevel.getGameTime() >= data.cooldownEndsAt); }
/*     */ 
/*     */ 
/*     */   
/* 241 */   public int lightLevel() { return this.lightLevel; }
/*     */ 
/*     */ 
/*     */   
/* 245 */   public double spinningMobSpeed() { return this.spinningMobSpeed; }
/*     */ 
/*     */ 
/*     */   
/* 249 */   public boolean hasSpinningMob() { return (this.spinningMobSpeed >= 0.0D); }
/*     */ 
/*     */ 
/*     */   
/* 253 */   public boolean isCapableOfSpawning() { return this.isCapableOfSpawning; }
/*     */ 
/*     */ 
/*     */   
/* 257 */   public void emitParticles(Level level, BlockPos blockPos, boolean isOminous) { this.particleEmission.emit(level, level.getRandom(), blockPos, isOminous); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   public String getSerializedName() { return this.name; }
/*     */   
/*     */   private static class LightLevel {
/*     */     private static final int UNLIT = 0;
/*     */     private static final int HALF_LIT = 4;
/*     */     private static final int LIT = 8; }
/*     */   
/*     */   private static class SpinningMob {
/*     */     private static final double NONE = -1.0D;
/*     */     private static final double SLOW = 200.0D;
/*     */     private static final double FAST = 1000.0D;
/*     */   }
/*     */   
/*     */   private static interface ParticleEmission {
/*     */     public static final ParticleEmission NONE = (level, random, pos, isOminous) -> {
/*     */       
/*     */       };
/*     */     public static final ParticleEmission SMALL_FLAMES = (level, random, pos, isOminous) -> {
/* 280 */         if (random.nextInt(2) == 0) {
/* 281 */           Vec3 vec = pos.getCenter().offsetRandom(random, 0.9F);
/* 282 */           addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec, level);
/*     */         } 
/*     */       };
/*     */     public static final ParticleEmission FLAMES_AND_SMOKE = (level, random, pos, isOminous) -> {
/* 286 */         Vec3 vec = pos.getCenter().offsetRandom(random, 1.0F);
/* 287 */         addParticle(ParticleTypes.SMOKE, vec, level);
/* 288 */         addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec, level);
/*     */       };
/*     */     public static final ParticleEmission SMOKE_INSIDE_AND_TOP_FACE = (level, random, pos, isOminous) -> {
/* 291 */         Vec3 vec = pos.getCenter().offsetRandom(random, 0.9F);
/* 292 */         if (random.nextInt(3) == 0) {
/* 293 */           addParticle(ParticleTypes.SMOKE, vec, level);
/*     */         }
/*     */         
/* 296 */         if (level.getGameTime() % 20L == 0L) {
/* 297 */           Vec3 topFaceVec = pos.getCenter().add(0.0D, 0.5D, 0.0D);
/* 298 */           int smokeCount = level.getRandom().nextInt(4) + 20;
/* 299 */           for (int i = 0; i < smokeCount; i++)
/* 300 */             addParticle(ParticleTypes.SMOKE, topFaceVec, level); 
/*     */         } 
/*     */       };
/*     */     
/*     */     void emit(Level param1Level, RandomSource param1RandomSource, BlockPos param1BlockPos, boolean param1Boolean);
/*     */     
/* 306 */     private static void addParticle(SimpleParticleType smoke, Vec3 vec, Level level) { level.addParticle(smoke, vec.x(), vec.y(), vec.z(), 0.0D, 0.0D, 0.0D); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */