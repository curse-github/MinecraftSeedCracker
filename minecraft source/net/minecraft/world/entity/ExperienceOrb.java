/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class ExperienceOrb
/*     */   extends Entity
/*     */ {
/*  33 */   protected static final EntityDataAccessor<Integer> DATA_VALUE = SynchedEntityData.defineId(ExperienceOrb.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   private static final int ENTITY_SCAN_PERIOD = 20;
/*     */   private static final int MAX_FOLLOW_DIST = 8;
/*     */   private static final int ORB_GROUPS_PER_AREA = 40;
/*     */   private static final double ORB_MERGE_DISTANCE = 0.5D;
/*     */   private static final short DEFAULT_HEALTH = 5;
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   private static final short DEFAULT_VALUE = 0;
/*     */   private static final int DEFAULT_COUNT = 1;
/*  44 */   private int age = 0;
/*  45 */   private int health = 5;
/*  46 */   private int count = 1;
/*     */   private Player followingPlayer;
/*  48 */   private final InterpolationHandler interpolation = new InterpolationHandler(this);
/*     */ 
/*     */   
/*  51 */   public ExperienceOrb(Level level, double x, double y, double z, int value) { this(level, new Vec3(x, y, z), Vec3.ZERO, value); }
/*     */ 
/*     */   
/*     */   public ExperienceOrb(Level level, Vec3 pos, Vec3 roughly, int value) {
/*  55 */     this(EntityType.EXPERIENCE_ORB, level);
/*  56 */     setPos(pos);
/*  57 */     if (!level.isClientSide()) {
/*  58 */       setYRot(this.random.nextFloat() * 360.0F);
/*     */ 
/*     */ 
/*     */       
/*  62 */       Vec3 randomMovement = new Vec3((this.random.nextDouble() * 0.2D - 0.1D) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * 0.2D - 0.1D) * 2.0D);
/*     */       
/*  64 */       if (roughly.lengthSqr() > 0.0D && roughly.dot(randomMovement) < 0.0D) {
/*  65 */         randomMovement = randomMovement.scale(-1.0D);
/*     */       }
/*     */       
/*  68 */       double size = getBoundingBox().getSize();
/*  69 */       setPos(pos.add(roughly.normalize().scale(size * 0.5D)));
/*  70 */       setDeltaMovement(randomMovement);
/*     */       
/*  72 */       if (!level.noCollision(getBoundingBox())) {
/*  73 */         unstuckIfPossible(size);
/*     */       }
/*     */     } 
/*  76 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*  80 */   public ExperienceOrb(EntityType<? extends ExperienceOrb> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   protected void unstuckIfPossible(double maxDistance) {
/*  84 */     Vec3 center = position().add(0.0D, getBbHeight() / 2.0D, 0.0D);
/*  85 */     VoxelShape allowedCenters = Shapes.create(AABB.ofSize(center, maxDistance, maxDistance, maxDistance));
/*  86 */     level().findFreePosition(this, allowedCenters, center, getBbWidth(), getBbHeight(), getBbWidth())
/*  87 */       .ifPresent(pos -> setPos(pos.add(0.0D, -getBbHeight() / 2.0D, 0.0D)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_VALUE, Integer.valueOf(0)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected double getDefaultGravity() { return 0.03D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 107 */     this.interpolation.interpolate();
/* 108 */     if (this.firstTick && level().isClientSide()) {
/* 109 */       this.firstTick = false;
/*     */       
/*     */       return;
/*     */     } 
/* 113 */     super.tick();
/*     */     
/* 115 */     boolean colliding = !level().noCollision(getBoundingBox());
/*     */     
/* 117 */     if (isEyeInFluid(FluidTags.WATER)) {
/* 118 */       setUnderwaterMovement();
/* 119 */     } else if (!colliding) {
/* 120 */       applyGravity();
/*     */     } 
/*     */     
/* 123 */     if (level().getFluidState(blockPosition()).is(FluidTags.LAVA)) {
/* 124 */       setDeltaMovement(((this.random
/* 125 */           .nextFloat() - this.random.nextFloat()) * 0.2F), 0.20000000298023224D, ((this.random
/*     */           
/* 127 */           .nextFloat() - this.random.nextFloat()) * 0.2F));
/*     */     }
/*     */ 
/*     */     
/* 131 */     if (this.tickCount % 20 == 1) {
/* 132 */       scanForMerges();
/*     */     }
/*     */     
/* 135 */     followNearbyPlayer();
/*     */     
/* 137 */     if (this.followingPlayer == null && !level().isClientSide() && colliding) {
/* 138 */       boolean nextColliding = !level().noCollision(getBoundingBox().move(getDeltaMovement()));
/* 139 */       if (nextColliding) {
/* 140 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/* 141 */         this.needsSync = true;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     double fallSpeed = (getDeltaMovement()).y;
/* 146 */     move(MoverType.SELF, getDeltaMovement());
/* 147 */     applyEffectsFromBlocks();
/*     */     
/* 149 */     float friction = 0.98F;
/* 150 */     if (onGround()) {
/* 151 */       friction = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
/*     */     }
/*     */     
/* 154 */     setDeltaMovement(getDeltaMovement().scale(friction));
/* 155 */     if (this.verticalCollisionBelow && fallSpeed < -getGravity()) {
/* 156 */       setDeltaMovement(new Vec3((getDeltaMovement()).x, -fallSpeed * 0.4D, (getDeltaMovement()).z));
/*     */     }
/*     */     
/* 159 */     this.age++;
/* 160 */     if (this.age >= 6000) {
/* 161 */       discard();
/*     */     }
/*     */   }
/*     */   
/*     */   private void followNearbyPlayer() {
/* 166 */     if (this.followingPlayer == null || this.followingPlayer
/* 167 */       .isSpectator() || this.followingPlayer
/* 168 */       .distanceToSqr(this) > 64.0D) {
/* 169 */       Player nearestPlayer = level().getNearestPlayer(this, 8.0D);
/* 170 */       if (nearestPlayer != null && !nearestPlayer.isSpectator() && !nearestPlayer.isDeadOrDying()) {
/* 171 */         this.followingPlayer = nearestPlayer;
/*     */       } else {
/* 173 */         this.followingPlayer = null;
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     if (this.followingPlayer != null) {
/*     */ 
/*     */ 
/*     */       
/* 181 */       Vec3 delta = new Vec3(this.followingPlayer.getX() - getX(), this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0D - getY(), this.followingPlayer.getZ() - getZ());
/*     */       
/* 183 */       double length = delta.lengthSqr();
/* 184 */       double power = 1.0D - Math.sqrt(length) / 8.0D;
/* 185 */       setDeltaMovement(getDeltaMovement().add(delta.normalize().scale(power * power * 0.1D)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 192 */   public BlockPos getBlockPosBelowThatAffectsMyMovement() { return getOnPos(0.999999F); }
/*     */ 
/*     */   
/*     */   private void scanForMerges() {
/* 196 */     if (level() instanceof ServerLevel) {
/* 197 */       List<ExperienceOrb> orbs = level().getEntities(EntityTypeTest.forClass(ExperienceOrb.class), getBoundingBox().inflate(0.5D), this::canMerge);
/* 198 */       for (ExperienceOrb orb : orbs) {
/* 199 */         merge(orb);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 205 */   public static void award(ServerLevel level, Vec3 pos, int amount) { awardWithDirection(level, pos, Vec3.ZERO, amount); }
/*     */ 
/*     */   
/*     */   public static void awardWithDirection(ServerLevel level, Vec3 pos, Vec3 roughDirection, int amount) {
/* 209 */     while (amount > 0) {
/* 210 */       int newCount = getExperienceValue(amount);
/* 211 */       amount -= newCount;
/* 212 */       if (!tryMergeToExisting(level, pos, newCount)) {
/* 213 */         level.addFreshEntity(new ExperienceOrb(level, pos, roughDirection, newCount));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryMergeToExisting(ServerLevel level, Vec3 pos, int value) {
/* 219 */     AABB box = AABB.ofSize(pos, 1.0D, 1.0D, 1.0D);
/* 220 */     int id = level.getRandom().nextInt(40);
/* 221 */     List<ExperienceOrb> orbs = level.getEntities(EntityTypeTest.forClass(ExperienceOrb.class), box, orb -> canMerge(orb, id, value));
/* 222 */     if (!orbs.isEmpty()) {
/* 223 */       ExperienceOrb orb = (ExperienceOrb)orbs.get(0);
/* 224 */       orb.count++;
/* 225 */       orb.age = 0;
/* 226 */       return true;
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canMerge(ExperienceOrb orb) {
/* 232 */     return (orb != this && 
/* 233 */       canMerge(orb, getId(), getValue()));
/*     */   }
/*     */   
/*     */   private static boolean canMerge(ExperienceOrb orb, int id, int value) {
/* 237 */     return (!orb.isRemoved() && (orb
/* 238 */       .getId() - id) % 40 == 0 && orb
/* 239 */       .getValue() == value);
/*     */   }
/*     */   
/*     */   private void merge(ExperienceOrb orb) {
/* 243 */     this.count += orb.count;
/* 244 */     this.age = Math.min(this.age, orb.age);
/* 245 */     orb.discard();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 250 */     Vec3 movement = getDeltaMovement();
/*     */     
/* 252 */     setDeltaMovement(movement.x * 0.9900000095367432D, 
/*     */         
/* 254 */         Math.min(movement.y + 5.000000237487257E-4D, 0.05999999865889549D), movement.z * 0.9900000095367432D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWaterSplashEffect() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 265 */   public final boolean hurtClient(DamageSource source) { return !isInvulnerableToBase(source); }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 270 */     if (isInvulnerableToBase(source)) {
/* 271 */       return false;
/*     */     }
/* 273 */     markHurt();
/* 274 */     this.health = (int)(this.health - damage);
/* 275 */     if (this.health <= 0) {
/* 276 */       discard();
/*     */     }
/* 278 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 283 */     output.putShort("Health", (short)this.health);
/* 284 */     output.putShort("Age", (short)this.age);
/* 285 */     output.putShort("Value", (short)getValue());
/* 286 */     output.putInt("Count", this.count);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 291 */     this.health = input.getShortOr("Health", (short)5);
/* 292 */     this.age = input.getShortOr("Age", (short)0);
/* 293 */     setValue(input.getShortOr("Value", (short)0));
/* 294 */     this.count = ((Integer)input.read("Count", ExtraCodecs.POSITIVE_INT).orElse(Integer.valueOf(1))).intValue();
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/*     */     ServerPlayer serverPlayer;
/* 299 */     if (player instanceof ServerPlayer) { serverPlayer = (ServerPlayer)player; }
/*     */     else
/*     */     { return; }
/*     */     
/* 303 */     if (player.takeXpDelay == 0) {
/* 304 */       player.takeXpDelay = 2;
/* 305 */       player.take(this, 1);
/* 306 */       int remaining = repairPlayerItems(serverPlayer, getValue());
/* 307 */       if (remaining > 0) {
/* 308 */         player.giveExperiencePoints(remaining);
/*     */       }
/* 310 */       this.count--;
/* 311 */       if (this.count == 0) {
/* 312 */         discard();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private int repairPlayerItems(ServerPlayer player, int amount) {
/* 318 */     Optional<EnchantedItemInUse> selected = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, player, ItemStack::isDamaged);
/* 319 */     if (selected.isPresent()) {
/* 320 */       ItemStack itemStack = ((EnchantedItemInUse)selected.get()).itemStack();
/* 321 */       int toRepairFromXpAmount = EnchantmentHelper.modifyDurabilityToRepairFromXp(player.level(), itemStack, amount);
/* 322 */       int repair = Math.min(toRepairFromXpAmount, itemStack.getDamageValue());
/* 323 */       itemStack.setDamageValue(itemStack.getDamageValue() - repair);
/*     */       
/* 325 */       if (repair > 0) {
/*     */         
/* 327 */         int remaining = amount - repair * amount / toRepairFromXpAmount;
/* 328 */         if (remaining > 0) {
/* 329 */           return repairPlayerItems(player, remaining);
/*     */         }
/*     */       } 
/* 332 */       return 0;
/*     */     } 
/* 334 */     return amount;
/*     */   }
/*     */ 
/*     */   
/* 338 */   public int getValue() { return ((Integer)this.entityData.get(DATA_VALUE)).intValue(); }
/*     */ 
/*     */ 
/*     */   
/* 342 */   private void setValue(int value) { this.entityData.set(DATA_VALUE, Integer.valueOf(value)); }
/*     */ 
/*     */   
/*     */   public int getIcon() {
/* 346 */     int value = getValue();
/* 347 */     if (value >= 2477)
/* 348 */       return 10; 
/* 349 */     if (value >= 1237)
/* 350 */       return 9; 
/* 351 */     if (value >= 617)
/* 352 */       return 8; 
/* 353 */     if (value >= 307)
/* 354 */       return 7; 
/* 355 */     if (value >= 149)
/* 356 */       return 6; 
/* 357 */     if (value >= 73)
/* 358 */       return 5; 
/* 359 */     if (value >= 37)
/* 360 */       return 4; 
/* 361 */     if (value >= 17)
/* 362 */       return 3; 
/* 363 */     if (value >= 7)
/* 364 */       return 2; 
/* 365 */     if (value >= 3) {
/* 366 */       return 1;
/*     */     }
/*     */     
/* 369 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getExperienceValue(int maxValue) {
/* 381 */     if (maxValue >= 2477)
/* 382 */       return 2477; 
/* 383 */     if (maxValue >= 1237)
/* 384 */       return 1237; 
/* 385 */     if (maxValue >= 617)
/* 386 */       return 617; 
/* 387 */     if (maxValue >= 307)
/* 388 */       return 307; 
/* 389 */     if (maxValue >= 149)
/* 390 */       return 149; 
/* 391 */     if (maxValue >= 73)
/* 392 */       return 73; 
/* 393 */     if (maxValue >= 37)
/* 394 */       return 37; 
/* 395 */     if (maxValue >= 17)
/* 396 */       return 17; 
/* 397 */     if (maxValue >= 7)
/* 398 */       return 7; 
/* 399 */     if (maxValue >= 3) {
/* 400 */       return 3;
/*     */     }
/*     */     
/* 403 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 408 */   public boolean isAttackable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 413 */   public SoundSource getSoundSource() { return SoundSource.AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 418 */   public InterpolationHandler getInterpolation() { return this.interpolation; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ExperienceOrb.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */