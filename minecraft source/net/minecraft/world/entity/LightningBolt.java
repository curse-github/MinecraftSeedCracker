/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.item.HoneycombItem;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseFireBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.LightningRodBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class LightningBolt
/*     */   extends Entity
/*     */ {
/*     */   private static final int START_LIFE = 2;
/*     */   private static final double DAMAGE_RADIUS = 3.0D;
/*     */   private static final double DETECTION_RADIUS = 15.0D;
/*     */   private int life;
/*     */   public long seed;
/*     */   private int flashes;
/*     */   private boolean visualOnly;
/*     */   private ServerPlayer cause;
/*  45 */   private final Set<Entity> hitEntities = Sets.newHashSet();
/*     */   private int blocksSetOnFire;
/*     */   
/*     */   public LightningBolt(EntityType<? extends LightningBolt> type, Level level) {
/*  49 */     super(type, level);
/*     */     
/*  51 */     this.life = 2;
/*  52 */     this.seed = this.random.nextLong();
/*  53 */     this.flashes = this.random.nextInt(3) + 1;
/*     */   }
/*     */ 
/*     */   
/*  57 */   public void setVisualOnly(boolean visualOnly) { this.visualOnly = visualOnly; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public SoundSource getSoundSource() { return SoundSource.WEATHER; }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public ServerPlayer getCause() { return this.cause; }
/*     */ 
/*     */ 
/*     */   
/*  70 */   public void setCause(ServerPlayer cause) { this.cause = cause; }
/*     */ 
/*     */   
/*     */   private void powerLightningRod() {
/*  74 */     BlockPos strikePosition = getStrikePosition();
/*  75 */     BlockState stateBelow = level().getBlockState(strikePosition);
/*  76 */     Block block = stateBelow.getBlock(); if (block instanceof LightningRodBlock) { LightningRodBlock lightningRodBlock = (LightningRodBlock)block;
/*  77 */       lightningRodBlock.onLightningStrike(stateBelow, level(), strikePosition); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  83 */     super.tick();
/*     */     
/*  85 */     if (this.life == 2) {
/*  86 */       if (level().isClientSide()) {
/*  87 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
/*  88 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
/*     */       } else {
/*  90 */         Difficulty difficulty = level().getDifficulty();
/*  91 */         if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD) {
/*  92 */           spawnFire(4);
/*     */         }
/*     */         
/*  95 */         powerLightningRod();
/*  96 */         clearCopperOnLightningStrike(level(), getStrikePosition());
/*     */         
/*  98 */         gameEvent(GameEvent.LIGHTNING_STRIKE);
/*     */       } 
/*     */     }
/*     */     
/* 102 */     this.life--;
/* 103 */     if (this.life < 0) {
/* 104 */       if (this.flashes == 0) {
/* 105 */         if (level() instanceof ServerLevel) {
/* 106 */           List<Entity> viewers = level().getEntities(this, new AABB(getX() - 15.0D, getY() - 15.0D, getZ() - 15.0D, getX() + 15.0D, getY() + 6.0D + 15.0D, getZ() + 15.0D), entity -> 
/* 107 */               (entity.isAlive() && !this.hitEntities.contains(entity)));
/*     */ 
/*     */           
/* 110 */           for (ServerPlayer player : ((ServerLevel)level()).getPlayers(player -> (player.distanceTo(this) < 256.0F))) {
/* 111 */             CriteriaTriggers.LIGHTNING_STRIKE.trigger(player, this, viewers);
/*     */           }
/*     */         } 
/*     */         
/* 115 */         discard();
/* 116 */       } else if (this.life < -this.random.nextInt(10)) {
/* 117 */         this.flashes--;
/* 118 */         this.life = 1;
/* 119 */         this.seed = this.random.nextLong();
/* 120 */         spawnFire(0);
/*     */       } 
/*     */     }
/*     */     
/* 124 */     if (this.life >= 0) {
/* 125 */       if (!(level() instanceof ServerLevel)) {
/* 126 */         level().setSkyFlashTime(2);
/* 127 */       } else if (!this.visualOnly) {
/* 128 */         List<Entity> entities = level().getEntities(this, new AABB(getX() - 3.0D, getY() - 3.0D, getZ() - 3.0D, getX() + 3.0D, getY() + 6.0D + 3.0D, getZ() + 3.0D), Entity::isAlive);
/* 129 */         for (Entity entity : entities) {
/* 130 */           entity.thunderHit((ServerLevel)level(), this);
/*     */         }
/* 132 */         this.hitEntities.addAll(entities);
/* 133 */         if (this.cause != null) {
/* 134 */           CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, entities);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private BlockPos getStrikePosition() {
/* 141 */     Vec3 position = position();
/* 142 */     return BlockPos.containing(position.x, position.y - 1.0E-6D, position.z);
/*     */   }
/*     */   private void spawnFire(int additionalSources) {
/*     */     ServerLevel level;
/* 146 */     if (!this.visualOnly) { Level level1 = level(); if (level1 instanceof ServerLevel) { level = (ServerLevel)level1; }
/*     */       else { return; }
/*     */        }
/*     */     else { return; }
/* 150 */      BlockPos pos = blockPosition();
/* 151 */     if (!level.canSpreadFireAround(pos)) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     BlockState fire = BaseFireBlock.getState(level, pos);
/*     */     
/* 157 */     if (level.getBlockState(pos).isAir() && fire.canSurvive(level, pos)) {
/* 158 */       level.setBlockAndUpdate(pos, fire);
/* 159 */       this.blocksSetOnFire++;
/*     */     } 
/*     */     
/* 162 */     for (int i = 0; i < additionalSources; i++) {
/* 163 */       BlockPos nearbyPos = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
/* 164 */       fire = BaseFireBlock.getState(level, nearbyPos);
/* 165 */       if (level.getBlockState(nearbyPos).isAir() && fire.canSurvive(level, nearbyPos)) {
/* 166 */         level.setBlockAndUpdate(nearbyPos, fire);
/* 167 */         this.blocksSetOnFire++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void clearCopperOnLightningStrike(Level level, BlockPos struckPos) {
/* 173 */     BlockState struckState = level.getBlockState(struckPos);
/*     */     
/* 175 */     boolean isWaxed = (((BiMap)HoneycombItem.WAX_OFF_BY_BLOCK.get()).get(struckState.getBlock()) != null);
/* 176 */     boolean isWeatheringCopper = struckState.getBlock() instanceof WeatheringCopper;
/*     */     
/* 178 */     if (!isWeatheringCopper && !isWaxed) {
/*     */       return;
/*     */     }
/*     */     
/* 182 */     if (isWeatheringCopper) {
/* 183 */       level.setBlockAndUpdate(struckPos, WeatheringCopper.getFirst(level.getBlockState(struckPos)));
/*     */     }
/*     */     
/* 186 */     BlockPos.MutableBlockPos workPos = struckPos.mutable();
/* 187 */     int strikesCount = level.random.nextInt(3) + 3;
/* 188 */     for (int strike = 0; strike < strikesCount; strike++) {
/* 189 */       int stepCount = level.random.nextInt(8) + 1;
/* 190 */       randomWalkCleaningCopper(level, struckPos, workPos, stepCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void randomWalkCleaningCopper(Level level, BlockPos originalStrikePos, BlockPos.MutableBlockPos workPos, int stepCount) {
/* 195 */     workPos.set(originalStrikePos);
/* 196 */     for (int step = 0; step < stepCount; step++) {
/* 197 */       Optional<BlockPos> stepPos = randomStepCleaningCopper(level, workPos);
/* 198 */       if (stepPos.isEmpty()) {
/*     */         break;
/*     */       }
/* 201 */       workPos.set((Vec3i)stepPos.get());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<BlockPos> randomStepCleaningCopper(Level level, BlockPos pos) {
/* 207 */     for (Iterator iterator = BlockPos.randomInCube(level.random, 10, pos, 1).iterator(); iterator.hasNext(); ) { BlockPos candidate = (BlockPos)iterator.next();
/* 208 */       BlockState state = level.getBlockState(candidate);
/* 209 */       if (state.getBlock() instanceof WeatheringCopper) {
/* 210 */         WeatheringCopper.getPrevious(state).ifPresent(s -> level.setBlockAndUpdate(candidate, s));
/* 211 */         level.levelEvent(3002, candidate, -1);
/*     */         
/* 213 */         return Optional.of(candidate);
/*     */       }  }
/*     */ 
/*     */     
/* 217 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 223 */     double size = 64.0D * getViewScale();
/* 224 */     return (distance < size * size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */ 
/*     */   
/* 240 */   public int getBlocksSetOnFire() { return this.blocksSetOnFire; }
/*     */ 
/*     */ 
/*     */   
/* 244 */   public Stream<Entity> getHitEntities() { return this.hitEntities.stream().filter(Entity::isAlive); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 249 */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) { return false; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\LightningBolt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */