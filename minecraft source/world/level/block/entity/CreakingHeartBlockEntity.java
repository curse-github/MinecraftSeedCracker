/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.particles.TrailParticleOption;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.Mutable;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreakingHeartBlockEntity
/*     */   extends BlockEntity
/*     */ {
/*     */   private static final int PLAYER_DETECTION_RANGE = 32;
/*     */   public static final int CREAKING_ROAMING_RADIUS = 32;
/*     */   private static final int DISTANCE_CREAKING_TOO_FAR = 34;
/*     */   private static final int SPAWN_RANGE_XZ = 16;
/*     */   private static final int SPAWN_RANGE_Y = 8;
/*     */   private static final int ATTEMPTS_PER_SPAWN = 5;
/*     */   private static final int UPDATE_TICKS = 20;
/*     */   private static final int UPDATE_TICKS_VARIANCE = 5;
/*     */   private static final int HURT_CALL_TOTAL_TICKS = 100;
/*     */   private static final int NUMBER_OF_HURT_CALLS = 10;
/*     */   private static final int HURT_CALL_INTERVAL = 10;
/*     */   private static final int HURT_CALL_PARTICLE_TICKS = 50;
/*     */   private static final int MAX_DEPTH = 2;
/*     */   private static final int MAX_COUNT = 64;
/*     */   private static final int TICKS_GRACE_PERIOD = 30;
/*  63 */   private static final Optional<Creaking> NO_CREAKING = Optional.empty();
/*     */   
/*     */   private Either<Creaking, UUID> creakingInfo;
/*     */   
/*     */   private long ticksExisted;
/*     */   private int ticker;
/*     */   private int emitter;
/*     */   private Vec3 emitterTarget;
/*     */   private int outputSignal;
/*     */   
/*  73 */   public CreakingHeartBlockEntity(BlockPos worldPosition, BlockState blockState) { super(BlockEntityType.CREAKING_HEART, worldPosition, blockState); }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState state, CreakingHeartBlockEntity entity) {
/*     */     ServerLevel serverLevel;
/*  77 */     entity.ticksExisted++;
/*  78 */     if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*     */     
/*  82 */     int computedOutputSignal = entity.computeAnalogOutputSignal();
/*  83 */     if (entity.outputSignal != computedOutputSignal) {
/*  84 */       entity.outputSignal = computedOutputSignal;
/*  85 */       level.updateNeighbourForOutputSignal(pos, Blocks.CREAKING_HEART);
/*     */     } 
/*     */     
/*  88 */     if (entity.emitter > 0) {
/*  89 */       if (entity.emitter > 50) {
/*  90 */         entity.emitParticles(serverLevel, 1, true);
/*  91 */         entity.emitParticles(serverLevel, 1, false);
/*     */       } 
/*  93 */       if (entity.emitter % 10 == 0 && entity.emitterTarget != null) {
/*  94 */         entity.getCreakingProtector().ifPresent(creaking -> entity.emitterTarget = creaking.getBoundingBox().getCenter());
/*     */         
/*  96 */         Vec3 heartPosition = Vec3.atCenterOf(pos);
/*     */         
/*  98 */         float progress = 0.2F + 0.8F * (100 - entity.emitter) / 100.0F;
/*  99 */         Vec3 soundLocation = heartPosition.subtract(entity.emitterTarget).scale(progress).add(entity.emitterTarget);
/* 100 */         BlockPos soundPos = BlockPos.containing(soundLocation);
/* 101 */         float volume = entity.emitter / 2.0F / 100.0F + 0.5F;
/* 102 */         serverLevel.playSound(null, soundPos, SoundEvents.CREAKING_HEART_HURT, SoundSource.BLOCKS, volume, 1.0F);
/*     */       } 
/*     */       
/* 105 */       entity.emitter--;
/*     */     } 
/*     */     
/* 108 */     if (entity.ticker-- >= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 112 */     entity.ticker = (entity.level == null) ? 20 : (entity.level.random.nextInt(5) + 20);
/*     */     
/* 114 */     BlockState updatedState = updateCreakingState(level, state, pos, entity);
/* 115 */     if (updatedState != state) {
/* 116 */       level.setBlock(pos, updatedState, 3);
/* 117 */       if (updatedState.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 122 */     if (entity.creakingInfo != null) {
/* 123 */       Optional<Creaking> optionalCreaking = entity.getCreakingProtector();
/* 124 */       if (optionalCreaking.isPresent()) {
/* 125 */         Creaking creaking = (Creaking)optionalCreaking.get();
/* 126 */         if ((!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue() && !creaking.isPersistenceRequired()) || entity.distanceToCreaking() > 34.0D || creaking.playerIsStuckInYou()) {
/* 127 */           entity.removeProtector(null);
/*     */         }
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 133 */     if (updatedState.getValue(CreakingHeartBlock.STATE) != CreakingHeartState.AWAKE) {
/*     */       return;
/*     */     }
/*     */     
/* 137 */     if (!serverLevel.isSpawningMonsters()) {
/*     */       return;
/*     */     }
/*     */     
/* 141 */     Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32.0D, false);
/* 142 */     if (player != null) {
/* 143 */       Creaking creaking = spawnProtector(serverLevel, entity);
/* 144 */       if (creaking != null) {
/* 145 */         entity.setCreakingInfo(creaking);
/* 146 */         creaking.makeSound(SoundEvents.CREAKING_SPAWN);
/* 147 */         level.playSound(null, entity.getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState updateCreakingState(Level level, BlockState state, BlockPos pos, CreakingHeartBlockEntity entity) {
/* 153 */     if (!CreakingHeartBlock.hasRequiredLogs(state, level, pos) && entity.creakingInfo == null) {
/* 154 */       return (BlockState)state.setValue(CreakingHeartBlock.STATE, CreakingHeartState.UPROOTED);
/*     */     }
/* 156 */     CreakingHeartState heartState = ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue() ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT;
/* 157 */     return (BlockState)state.setValue(CreakingHeartBlock.STATE, heartState);
/*     */   }
/*     */ 
/*     */   
/* 161 */   private double distanceToCreaking() { return ((Double)getCreakingProtector().map(creaking -> Double.valueOf(Math.sqrt(creaking.distanceToSqr(Vec3.atBottomCenterOf(getBlockPos()))))).orElse(Double.valueOf(0.0D))).doubleValue(); }
/*     */ 
/*     */   
/*     */   private void clearCreakingInfo() {
/* 165 */     this.creakingInfo = null;
/* 166 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(Creaking creaking) {
/* 170 */     this.creakingInfo = Either.left(creaking);
/* 171 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(UUID uuid) {
/* 175 */     this.creakingInfo = Either.right(uuid);
/* 176 */     this.ticksExisted = 0L;
/* 177 */     setChanged();
/*     */   }
/*     */   
/*     */   private Optional<Creaking> getCreakingProtector() {
/* 181 */     if (this.creakingInfo == null) {
/* 182 */       return NO_CREAKING;
/*     */     }
/* 184 */     if (this.creakingInfo.left().isPresent()) {
/* 185 */       Creaking creaking = (Creaking)this.creakingInfo.left().get();
/* 186 */       if (!creaking.isRemoved()) {
/* 187 */         return Optional.of(creaking);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 192 */       setCreakingInfo(creaking.getUUID());
/*     */     } 
/* 194 */     Level level = this.level; if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (this.creakingInfo.right().isPresent()) {
/*     */ 
/*     */         
/* 197 */         UUID uuid = (UUID)this.creakingInfo.right().get();
/* 198 */         Entity entity = serverLevel.getEntity(uuid);
/* 199 */         if (entity instanceof Creaking) { Creaking resolvedCreaking = (Creaking)entity;
/* 200 */           setCreakingInfo(resolvedCreaking);
/* 201 */           return Optional.of(resolvedCreaking); }
/*     */         
/* 203 */         if (this.ticksExisted >= 30L) {
/* 204 */           clearCreakingInfo();
/*     */         }
/* 206 */         return NO_CREAKING;
/*     */       }  }
/* 208 */      return NO_CREAKING;
/*     */   }
/*     */   
/*     */   private static Creaking spawnProtector(ServerLevel level, CreakingHeartBlockEntity entity) {
/* 212 */     BlockPos pos = entity.getBlockPos();
/* 213 */     Optional<Creaking> spawnedMob = SpawnUtil.trySpawnMob(EntityType.CREAKING, EntitySpawnReason.SPAWNER, level, pos, 5, 16, 8, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER_NO_LEAVES, true);
/* 214 */     if (spawnedMob.isEmpty()) {
/* 215 */       return null;
/*     */     }
/* 217 */     Creaking spawnedCreaking = (Creaking)spawnedMob.get();
/* 218 */     level.gameEvent(spawnedCreaking, GameEvent.ENTITY_PLACE, spawnedCreaking.position());
/* 219 */     level.broadcastEntityEvent(spawnedCreaking, (byte)60);
/* 220 */     spawnedCreaking.setTransient(pos);
/* 221 */     return spawnedCreaking;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 226 */   public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 231 */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) { return saveCustomOnly(registries); }
/*     */   
/*     */   public void creakingHurt() {
/*     */     Creaking creaking;
/* 235 */     ServerLevel serverLevel = getCreakingProtector().orElse(null); if (serverLevel instanceof Creaking) { creaking = (Creaking)serverLevel; }
/*     */     else
/*     */     { return; }
/* 238 */      Level level = this.level; if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/* 241 */      if (this.emitter > 0) {
/*     */       return;
/*     */     }
/* 244 */     emitParticles(serverLevel, 20, false);
/* 245 */     if (getBlockState().getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
/* 246 */       int numberOfClumps = this.level.getRandom().nextIntBetweenInclusive(2, 3);
/* 247 */       for (int i = 0; i < numberOfClumps; i++) {
/* 248 */         spreadResin(serverLevel).ifPresent(blockPos -> {
/* 249 */               this.level.playSound(null, blockPos, SoundEvents.RESIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 250 */               this.level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(getBlockState()));
/*     */             });
/*     */       } 
/*     */     } 
/* 254 */     this.emitter = 100;
/* 255 */     this.emitterTarget = creaking.getBoundingBox().getCenter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<BlockPos> spreadResin(ServerLevel level) {
/* 262 */     MutableObject mutableObject = new MutableObject(null);
/* 263 */     BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (pos, acceptor) -> {
/* 264 */           for (Direction dir : Util.shuffledCopy(Direction.values(), level.random)) {
/* 265 */             BlockPos neighbourPos = pos.relative(dir);
/* 266 */             if (level.getBlockState(neighbourPos).is(BlockTags.PALE_OAK_LOGS)) {
/* 267 */               acceptor.accept(neighbourPos);
/*     */             }
/*     */           } 
/*     */         }pos -> {
/*     */           
/* 272 */           if (!level.getBlockState(pos).is(BlockTags.PALE_OAK_LOGS)) {
/* 273 */             return BlockPos.TraversalNodeStatus.ACCEPT;
/*     */           }
/* 275 */           for (Direction dir : Util.shuffledCopy(Direction.values(), level.random)) {
/* 276 */             BlockPos neightbourPos = pos.relative(dir);
/* 277 */             BlockState neighbourState = level.getBlockState(neightbourPos);
/* 278 */             Direction opposite = dir.getOpposite();
/* 279 */             if (neighbourState.isAir()) {
/* 280 */               neighbourState = Blocks.RESIN_CLUMP.defaultBlockState();
/* 281 */             } else if (neighbourState.is(Blocks.WATER) && neighbourState.getFluidState().isSource()) {
/* 282 */               neighbourState = (BlockState)Blocks.RESIN_CLUMP.defaultBlockState().setValue(MultifaceBlock.WATERLOGGED, Boolean.valueOf(true));
/*     */             } 
/* 284 */             if (neighbourState.is(Blocks.RESIN_CLUMP) && !MultifaceBlock.hasFace(neighbourState, opposite)) {
/* 285 */               level.setBlock(neightbourPos, (BlockState)neighbourState.setValue(MultifaceBlock.getFaceProperty(opposite), Boolean.valueOf(true)), 3);
/* 286 */               placedResin.setValue(neightbourPos);
/* 287 */               return BlockPos.TraversalNodeStatus.STOP;
/*     */             } 
/*     */           } 
/* 290 */           return BlockPos.TraversalNodeStatus.ACCEPT;
/*     */         });
/* 292 */     return Optional.ofNullable((BlockPos)mutableObject.get());
/*     */   }
/*     */   private void emitParticles(ServerLevel serverLevel, int count, boolean towardsCreaking) {
/*     */     Creaking creaking;
/* 296 */     Object object = getCreakingProtector().orElse(null); if (object instanceof Creaking) { creaking = (Creaking)object; }
/*     */     else
/*     */     { return; }
/* 299 */      int color = towardsCreaking ? 16545810 : 6250335;
/*     */     
/* 301 */     RandomSource random = serverLevel.random;
/*     */     double i;
/* 303 */     for (i = 0.0D; i < count; i++) {
/* 304 */       AABB box = creaking.getBoundingBox();
/* 305 */       Vec3 source = box.getMinPosition().add(random.nextDouble() * box.getXsize(), random.nextDouble() * box.getYsize(), random.nextDouble() * box.getZsize());
/* 306 */       Vec3 destination = Vec3.atLowerCornerOf(getBlockPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());
/* 307 */       if (towardsCreaking) {
/* 308 */         Vec3 foo = source;
/* 309 */         source = destination;
/* 310 */         destination = foo;
/*     */       } 
/*     */       
/* 313 */       TrailParticleOption particleOption = new TrailParticleOption(destination, color, random.nextInt(40) + 10);
/* 314 */       serverLevel.sendParticles(particleOption, true, true, source.x, source.y, source.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 320 */   public void preRemoveSideEffects(BlockPos pos, BlockState state) { removeProtector(null); }
/*     */ 
/*     */   
/*     */   public void removeProtector(DamageSource damageSource) {
/* 324 */     Object object = getCreakingProtector().orElse(null); if (object instanceof Creaking) { Creaking creaking = (Creaking)object;
/* 325 */       if (damageSource == null) {
/* 326 */         creaking.tearDown();
/*     */       } else {
/* 328 */         creaking.creakingDeathEffects(damageSource);
/* 329 */         creaking.setTearingDown();
/* 330 */         creaking.setHealth(0.0F);
/*     */       } 
/* 332 */       clearCreakingInfo(); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/* 337 */   public boolean isProtector(Creaking creaking) { return ((Boolean)getCreakingProtector().map(c -> Boolean.valueOf((c == creaking))).orElse(Boolean.valueOf(false))).booleanValue(); }
/*     */ 
/*     */ 
/*     */   
/* 341 */   public int getAnalogOutputSignal() { return this.outputSignal; }
/*     */ 
/*     */   
/*     */   public int computeAnalogOutputSignal() {
/* 345 */     if (this.creakingInfo == null || getCreakingProtector().isEmpty()) {
/* 346 */       return 0;
/*     */     }
/* 348 */     double distance = distanceToCreaking();
/* 349 */     double scaledDistance = Math.clamp(distance, 0.0D, 32.0D) / 32.0D;
/* 350 */     return 15 - (int)Math.floor(scaledDistance * 15.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 355 */     super.loadAdditional(input);
/* 356 */     input.read("creaking", UUIDUtil.CODEC).ifPresentOrElse(this::setCreakingInfo, this::clearCreakingInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 364 */     super.saveAdditional(output);
/* 365 */     if (this.creakingInfo != null)
/* 366 */       output.store("creaking", UUIDUtil.CODEC, (UUID)this.creakingInfo.map(Entity::getUUID, uuid -> uuid)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\CreakingHeartBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */