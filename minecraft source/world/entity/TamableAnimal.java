/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ public abstract class TamableAnimal
/*     */   extends Animal
/*     */   implements OwnableEntity
/*     */ {
/*     */   public static final int TELEPORT_WHEN_DISTANCE_IS_SQ = 144;
/*     */   private static final int MIN_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 2;
/*     */   private static final int MAX_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 3;
/*     */   private static final int MAX_VERTICAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 1;
/*     */   private static final boolean DEFAULT_ORDERED_TO_SIT = false;
/*  38 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.BYTE);
/*  39 */   protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*     */   
/*     */   private boolean orderedToSit = false;
/*     */ 
/*     */   
/*  44 */   protected TamableAnimal(EntityType<? extends TamableAnimal> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  49 */     super.defineSynchedData(entityData);
/*  50 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  51 */     entityData.define(DATA_OWNERUUID_ID, Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  56 */     super.addAdditionalSaveData(output);
/*  57 */     EntityReference<LivingEntity> owner = getOwnerReference();
/*  58 */     EntityReference.store(owner, output, "Owner");
/*  59 */     output.putBoolean("Sitting", this.orderedToSit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  64 */     super.readAdditionalSaveData(input);
/*  65 */     EntityReference<LivingEntity> owner = EntityReference.readWithOldOwnerConversion(input, "Owner", level());
/*  66 */     if (owner != null) {
/*     */       try {
/*  68 */         this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner));
/*  69 */         setTame(true, false);
/*  70 */       } catch (Throwable ignored) {
/*  71 */         setTame(false, true);
/*     */       } 
/*     */     } else {
/*  74 */       this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
/*  75 */       setTame(false, true);
/*     */     } 
/*  77 */     this.orderedToSit = input.getBooleanOr("Sitting", false);
/*  78 */     setInSittingPose(this.orderedToSit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public boolean canBeLeashed() { return true; }
/*     */ 
/*     */   
/*     */   protected void spawnTamingParticles(boolean success) {
/*  87 */     SimpleParticleType simpleParticleType = ParticleTypes.HEART;
/*  88 */     if (!success) {
/*  89 */       simpleParticleType = ParticleTypes.SMOKE;
/*     */     }
/*  91 */     for (int i = 0; i < 7; i++) {
/*  92 */       double xa = this.random.nextGaussian() * 0.02D;
/*  93 */       double ya = this.random.nextGaussian() * 0.02D;
/*  94 */       double za = this.random.nextGaussian() * 0.02D;
/*  95 */       level().addParticle(simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 101 */     if (id == 7) {
/* 102 */       spawnTamingParticles(true);
/* 103 */     } else if (id == 6) {
/* 104 */       spawnTamingParticles(false);
/*     */     } else {
/* 106 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 111 */   public boolean isTame() { return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x4) != 0); }
/*     */ 
/*     */   
/*     */   public void setTame(boolean isTame, boolean includeSideEffects) {
/* 115 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 116 */     if (isTame) {
/* 117 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | 0x4)));
/*     */     } else {
/* 119 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFB)));
/*     */     } 
/* 121 */     if (includeSideEffects) {
/* 122 */       applyTamingSideEffects();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyTamingSideEffects() {}
/*     */ 
/*     */   
/* 130 */   public boolean isInSittingPose() { return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & true) != 0); }
/*     */ 
/*     */   
/*     */   public void setInSittingPose(boolean value) {
/* 134 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 135 */     if (value) {
/* 136 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | true)));
/*     */     } else {
/* 138 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 144 */   public EntityReference<LivingEntity> getOwnerReference() { return (EntityReference)((Optional)this.entityData.get(DATA_OWNERUUID_ID)).orElse(null); }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public void setOwner(LivingEntity owner) { this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner).map(EntityReference::of)); }
/*     */ 
/*     */ 
/*     */   
/* 152 */   public void setOwnerReference(EntityReference<LivingEntity> owner) { this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner)); }
/*     */ 
/*     */   
/*     */   public void tame(Player player) {
/* 156 */     setTame(true, true);
/* 157 */     setOwner(player);
/* 158 */     if (player instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)player;
/* 159 */       CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 165 */     if (isOwnedBy(target)) {
/* 166 */       return false;
/*     */     }
/* 168 */     return super.canAttack(target);
/*     */   }
/*     */ 
/*     */   
/* 172 */   public boolean isOwnedBy(LivingEntity entity) { return (entity == getOwner()); }
/*     */ 
/*     */ 
/*     */   
/* 176 */   public boolean wantsToAttack(LivingEntity target, LivingEntity owner) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PlayerTeam getTeam() {
/* 181 */     PlayerTeam ownTeam = super.getTeam();
/* 182 */     if (ownTeam != null) {
/* 183 */       return ownTeam;
/*     */     }
/* 185 */     if (isTame()) {
/* 186 */       LivingEntity owner = getRootOwner();
/* 187 */       if (owner != null) {
/* 188 */         return owner.getTeam();
/*     */       }
/*     */     } 
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/* 196 */     if (isTame()) {
/* 197 */       LivingEntity owner = getRootOwner();
/* 198 */       if (other == owner) {
/* 199 */         return true;
/*     */       }
/* 201 */       if (owner != null) {
/* 202 */         return owner.considersEntityAsAlly(other);
/*     */       }
/*     */     } 
/* 205 */     return super.considersEntityAsAlly(other);
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource source) {
/* 210 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (((Boolean)serverLevel.getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)).booleanValue()) {
/* 211 */         LivingEntity livingEntity = getOwner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 212 */           serverPlayer.sendSystemMessage(getCombatTracker().getDeathMessage()); }
/*     */       
/*     */       }  }
/* 215 */      super.die(source);
/*     */   }
/*     */ 
/*     */   
/* 219 */   public boolean isOrderedToSit() { return this.orderedToSit; }
/*     */ 
/*     */ 
/*     */   
/* 223 */   public void setOrderedToSit(boolean orderedToSit) { this.orderedToSit = orderedToSit; }
/*     */ 
/*     */   
/*     */   public void tryToTeleportToOwner() {
/* 227 */     LivingEntity owner = getOwner();
/* 228 */     if (owner != null) {
/* 229 */       teleportToAroundBlockPos(owner.blockPosition());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldTryTeleportToOwner() {
/* 234 */     LivingEntity owner = getOwner();
/* 235 */     return (owner != null && distanceToSqr(getOwner()) >= 144.0D);
/*     */   }
/*     */   
/*     */   private void teleportToAroundBlockPos(BlockPos targetPos) {
/* 239 */     for (int attempt = 0; attempt < 10; attempt++) {
/* 240 */       int xd = this.random.nextIntBetweenInclusive(-3, 3);
/* 241 */       int zd = this.random.nextIntBetweenInclusive(-3, 3);
/* 242 */       if (Math.abs(xd) >= 2 || Math.abs(zd) >= 2) {
/*     */ 
/*     */ 
/*     */         
/* 246 */         int yd = this.random.nextIntBetweenInclusive(-1, 1);
/* 247 */         if (maybeTeleportTo(targetPos.getX() + xd, targetPos.getY() + yd, targetPos.getZ() + zd))
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean maybeTeleportTo(int x, int y, int z) {
/* 254 */     if (!canTeleportTo(new BlockPos(x, y, z))) {
/* 255 */       return false;
/*     */     }
/* 257 */     snapTo(x + 0.5D, y, z + 0.5D, getYRot(), getXRot());
/* 258 */     this.navigation.stop();
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canTeleportTo(BlockPos pos) {
/* 263 */     PathType pathType = WalkNodeEvaluator.getPathTypeStatic(this, pos);
/* 264 */     if (pathType != PathType.WALKABLE) {
/* 265 */       return false;
/*     */     }
/*     */     
/* 268 */     BlockState blockStateBelow = level().getBlockState(pos.below());
/* 269 */     if (!canFlyToOwner() && blockStateBelow.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)
/*     */     {
/* 271 */       return false;
/*     */     }
/*     */     
/* 274 */     BlockPos delta = pos.subtract(blockPosition());
/*     */     
/* 276 */     return level().noCollision(this, getBoundingBox().move(delta));
/*     */   }
/*     */ 
/*     */   
/* 280 */   public final boolean unableToMoveToOwner() { return (isOrderedToSit() || isPassenger() || mayBeLeashed() || (getOwner() != null && getOwner().isSpectator())); }
/*     */ 
/*     */ 
/*     */   
/* 284 */   protected boolean canFlyToOwner() { return false; }
/*     */   
/*     */   public class TamableAnimalPanicGoal
/*     */     extends PanicGoal
/*     */   {
/* 289 */     public TamableAnimalPanicGoal(double speedModifier, TagKey<DamageType> panicCausingDamageTypes) { super(TamableAnimal.this, speedModifier, panicCausingDamageTypes); }
/*     */ 
/*     */ 
/*     */     
/* 293 */     public TamableAnimalPanicGoal(double speedModifier) { super(TamableAnimal.this, speedModifier); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void tick() {
/* 298 */       if (!TamableAnimal.this.unableToMoveToOwner() && TamableAnimal.this.shouldTryTeleportToOwner()) {
/* 299 */         TamableAnimal.this.tryToTeleportToOwner();
/*     */       }
/* 301 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\TamableAnimal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */