/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArmorStand
/*     */   extends LivingEntity
/*     */ {
/*     */   public static final int WOBBLE_TIME = 5;
/*     */   private static final boolean ENABLE_ARMS = true;
/*  58 */   public static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*  59 */   public static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*  60 */   public static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
/*  61 */   public static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
/*  62 */   public static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
/*  63 */   public static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);
/*     */   
/*  65 */   private static final EntityDimensions MARKER_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);
/*  66 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ARMOR_STAND.getDimensions().scale(0.5F).withEyeHeight(0.9875F);
/*     */   
/*     */   private static final double FEET_OFFSET = 0.1D;
/*     */   
/*     */   private static final double CHEST_OFFSET = 0.9D;
/*     */   
/*     */   private static final double LEGS_OFFSET = 0.4D;
/*     */   
/*     */   private static final double HEAD_OFFSET = 1.6D;
/*     */   public static final int DISABLE_TAKING_OFFSET = 8;
/*     */   public static final int DISABLE_PUTTING_OFFSET = 16;
/*     */   public static final int CLIENT_FLAG_SMALL = 1;
/*     */   public static final int CLIENT_FLAG_SHOW_ARMS = 4;
/*     */   public static final int CLIENT_FLAG_NO_BASEPLATE = 8;
/*     */   public static final int CLIENT_FLAG_MARKER = 16;
/*  81 */   public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.BYTE);
/*  82 */   public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  83 */   public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  84 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  85 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  86 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  87 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   private static final Predicate<Entity> RIDABLE_MINECARTS = entity -> {
/*  89 */       if (entity instanceof AbstractMinecart) { AbstractMinecart minecart = (AbstractMinecart)entity; if (minecart.isRideable()); }  return false;
/*     */     };
/*     */   
/*     */   private static final boolean DEFAULT_INVISIBLE = false;
/*     */   private static final int DEFAULT_DISABLED_SLOTS = 0;
/*     */   private static final boolean DEFAULT_SMALL = false;
/*     */   private static final boolean DEFAULT_SHOW_ARMS = false;
/*     */   private static final boolean DEFAULT_NO_BASE_PLATE = false;
/*     */   private static final boolean DEFAULT_MARKER = false;
/*     */   private boolean invisible = false;
/*     */   public long lastHit;
/* 100 */   private int disabledSlots = 0;
/*     */ 
/*     */   
/* 103 */   public ArmorStand(EntityType<? extends ArmorStand> type, Level level) { super(type, level); }
/*     */ 
/*     */   
/*     */   public ArmorStand(Level level, double x, double y, double z) {
/* 107 */     this(EntityType.ARMOR_STAND, level);
/* 108 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 112 */     return createLivingAttributes()
/* 113 */       .add(Attributes.STEP_HEIGHT, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/* 118 */     double oldX = getX();
/* 119 */     double oldY = getY();
/* 120 */     double oldZ = getZ();
/* 121 */     super.refreshDimensions();
/* 122 */     setPos(oldX, oldY, oldZ);
/*     */   }
/*     */ 
/*     */   
/* 126 */   private boolean hasPhysics() { return (!isMarker() && !isNoGravity()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   public boolean isEffectiveAi() { return (super.isEffectiveAi() && hasPhysics()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 136 */     super.defineSynchedData(entityData);
/* 137 */     entityData.define(DATA_CLIENT_FLAGS, Byte.valueOf((byte)0));
/* 138 */     entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
/* 139 */     entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
/* 140 */     entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
/* 141 */     entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
/* 142 */     entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
/* 143 */     entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 148 */   public boolean canUseSlot(EquipmentSlot slot) { return (slot != EquipmentSlot.BODY && slot != EquipmentSlot.SADDLE && !isDisabled(slot)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 153 */     super.addAdditionalSaveData(output);
/*     */     
/* 155 */     output.putBoolean("Invisible", isInvisible());
/* 156 */     output.putBoolean("Small", isSmall());
/*     */     
/* 158 */     output.putBoolean("ShowArms", showArms());
/*     */     
/* 160 */     output.putInt("DisabledSlots", this.disabledSlots);
/* 161 */     output.putBoolean("NoBasePlate", !showBasePlate());
/* 162 */     if (isMarker()) {
/* 163 */       output.putBoolean("Marker", isMarker());
/*     */     }
/* 165 */     output.store("Pose", ArmorStandPose.CODEC, getArmorStandPose());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 170 */     super.readAdditionalSaveData(input);
/*     */     
/* 172 */     setInvisible(input.getBooleanOr("Invisible", false));
/*     */     
/* 174 */     setSmall(input.getBooleanOr("Small", false));
/*     */     
/* 176 */     setShowArms(input.getBooleanOr("ShowArms", false));
/*     */     
/* 178 */     this.disabledSlots = input.getIntOr("DisabledSlots", 0);
/* 179 */     setNoBasePlate(input.getBooleanOr("NoBasePlate", false));
/* 180 */     setMarker(input.getBooleanOr("Marker", false));
/* 181 */     this.noPhysics = !hasPhysics();
/* 182 */     input.read("Pose", ArmorStandPose.CODEC).ifPresent(this::setArmorStandPose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   public boolean isPushable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity entity) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushEntities() {
/* 199 */     List<Entity> entities = level().getEntities(this, getBoundingBox(), RIDABLE_MINECARTS);
/* 200 */     for (Entity entity : entities) {
/* 201 */       if (distanceToSqr(entity) <= 0.2D) {
/* 202 */         entity.push(this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interactAt(Player player, Vec3 location, InteractionHand hand) {
/* 209 */     ItemStack itemStack = player.getItemInHand(hand);
/* 210 */     if (isMarker() || itemStack.is(Items.NAME_TAG)) {
/* 211 */       return InteractionResult.PASS;
/*     */     }
/* 213 */     if (player.isSpectator()) {
/* 214 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 217 */     if (player.level().isClientSide()) {
/* 218 */       return InteractionResult.SUCCESS_SERVER;
/*     */     }
/*     */     
/* 221 */     EquipmentSlot itemInHandSlot = getEquipmentSlotForItem(itemStack);
/* 222 */     if (itemStack.isEmpty()) {
/* 223 */       EquipmentSlot clickedSlot = getClickedSlot(location);
/* 224 */       EquipmentSlot targetSlot = isDisabled(clickedSlot) ? itemInHandSlot : clickedSlot;
/* 225 */       if (hasItemInSlot(targetSlot) && swapItem(player, targetSlot, itemStack, hand)) {
/* 226 */         return InteractionResult.SUCCESS_SERVER;
/*     */       }
/*     */     } else {
/* 229 */       if (isDisabled(itemInHandSlot)) {
/* 230 */         return InteractionResult.FAIL;
/*     */       }
/* 232 */       if (itemInHandSlot.getType() == EquipmentSlot.Type.HAND && !showArms()) {
/* 233 */         return InteractionResult.FAIL;
/*     */       }
/* 235 */       if (swapItem(player, itemInHandSlot, itemStack, hand)) {
/* 236 */         return InteractionResult.SUCCESS_SERVER;
/*     */       }
/*     */     } 
/* 239 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private EquipmentSlot getClickedSlot(Vec3 location) {
/* 243 */     EquipmentSlot slotClicked = EquipmentSlot.MAINHAND;
/* 244 */     boolean small = isSmall();
/*     */     
/* 246 */     double clickYPosition = location.y / (getScale() * getAgeScale());
/* 247 */     EquipmentSlot feet = EquipmentSlot.FEET;
/* 248 */     if (clickYPosition >= 0.1D && clickYPosition < 0.1D + (small ? 0.8D : 0.45D) && hasItemInSlot(feet)) {
/* 249 */       slotClicked = EquipmentSlot.FEET;
/* 250 */     } else if (clickYPosition >= 0.9D + (small ? 0.3D : 0.0D) && clickYPosition < 0.9D + (small ? 1.0D : 0.7D) && hasItemInSlot(EquipmentSlot.CHEST)) {
/* 251 */       slotClicked = EquipmentSlot.CHEST;
/* 252 */     } else if (clickYPosition >= 0.4D && clickYPosition < 0.4D + (small ? 1.0D : 0.8D) && hasItemInSlot(EquipmentSlot.LEGS)) {
/* 253 */       slotClicked = EquipmentSlot.LEGS;
/* 254 */     } else if (clickYPosition >= 1.6D && hasItemInSlot(EquipmentSlot.HEAD)) {
/* 255 */       slotClicked = EquipmentSlot.HEAD;
/* 256 */     } else if (!hasItemInSlot(EquipmentSlot.MAINHAND) && hasItemInSlot(EquipmentSlot.OFFHAND)) {
/* 257 */       slotClicked = EquipmentSlot.OFFHAND;
/*     */     } 
/*     */     
/* 260 */     return slotClicked;
/*     */   }
/*     */ 
/*     */   
/* 264 */   private boolean isDisabled(EquipmentSlot slot) { return ((this.disabledSlots & 1 << slot.getFilterBit(0)) != 0 || (slot.getType() == EquipmentSlot.Type.HAND && !showArms())); }
/*     */ 
/*     */   
/*     */   private boolean swapItem(Player player, EquipmentSlot slot, ItemStack playerItemStack, InteractionHand hand) {
/* 268 */     ItemStack itemStack = getItemBySlot(slot);
/*     */     
/* 270 */     if (!itemStack.isEmpty() && (this.disabledSlots & 1 << slot.getFilterBit(8)) != 0) {
/* 271 */       return false;
/*     */     }
/*     */     
/* 274 */     if (itemStack.isEmpty() && (this.disabledSlots & 1 << slot.getFilterBit(16)) != 0) {
/* 275 */       return false;
/*     */     }
/*     */     
/* 278 */     if (player.hasInfiniteMaterials() && itemStack.isEmpty() && !playerItemStack.isEmpty()) {
/* 279 */       setItemSlot(slot, playerItemStack.copyWithCount(1));
/* 280 */       return true;
/*     */     } 
/*     */     
/* 283 */     if (!playerItemStack.isEmpty() && playerItemStack.getCount() > 1) {
/* 284 */       if (!itemStack.isEmpty()) {
/* 285 */         return false;
/*     */       }
/* 287 */       setItemSlot(slot, playerItemStack.split(1));
/* 288 */       return true;
/*     */     } 
/*     */     
/* 291 */     setItemSlot(slot, playerItemStack);
/* 292 */     player.setItemInHand(hand, itemStack);
/* 293 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 298 */     if (isRemoved()) {
/* 299 */       return false;
/*     */     }
/* 301 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && source.getEntity() instanceof net.minecraft.world.entity.Mob) {
/* 302 */       return false;
/*     */     }
/* 304 */     if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 305 */       kill(level);
/* 306 */       return false;
/*     */     } 
/* 308 */     if (isInvulnerableTo(level, source) || this.invisible || isMarker()) {
/* 309 */       return false;
/*     */     }
/* 311 */     if (source.is(DamageTypeTags.IS_EXPLOSION)) {
/* 312 */       brokenByAnything(level, source);
/* 313 */       kill(level);
/* 314 */       return false;
/*     */     } 
/* 316 */     if (source.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
/* 317 */       if (isOnFire()) {
/* 318 */         causeDamage(level, source, 0.15F);
/*     */       } else {
/* 320 */         igniteForSeconds(5.0F);
/*     */       } 
/* 322 */       return false;
/*     */     } 
/* 324 */     if (source.is(DamageTypeTags.BURNS_ARMOR_STANDS) && getHealth() > 0.5F) {
/* 325 */       causeDamage(level, source, 4.0F);
/* 326 */       return false;
/*     */     } 
/*     */     
/* 329 */     boolean allowIncrementalBreaking = source.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
/* 330 */     boolean shouldKill = source.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
/* 331 */     if (!allowIncrementalBreaking && !shouldKill) {
/* 332 */       return false;
/*     */     }
/* 334 */     Entity entity = source.getEntity(); if (entity instanceof Player) { Player player = (Player)entity; if (!(player.getAbilities()).mayBuild)
/* 335 */         return false;  }
/*     */     
/* 337 */     if (source.isCreativePlayer()) {
/* 338 */       playBrokenSound();
/* 339 */       showBreakingParticles();
/* 340 */       kill(level);
/* 341 */       return true;
/*     */     } 
/*     */     
/* 344 */     long time = level.getGameTime();
/* 345 */     if (time - this.lastHit <= 5L || shouldKill) {
/* 346 */       brokenByPlayer(level, source);
/* 347 */       showBreakingParticles();
/* 348 */       kill(level);
/*     */     } else {
/* 350 */       level.broadcastEntityEvent(this, (byte)32);
/* 351 */       gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
/* 352 */       this.lastHit = time;
/*     */     } 
/* 354 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 359 */     if (id == 32) {
/* 360 */       if (level().isClientSide()) {
/* 361 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ARMOR_STAND_HIT, getSoundSource(), 0.3F, 1.0F, false);
/* 362 */         this.lastHit = level().getGameTime();
/*     */       } 
/*     */     } else {
/* 365 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 371 */     double size = getBoundingBox().getSize() * 4.0D;
/* 372 */     if (Double.isNaN(size) || size == 0.0D) {
/* 373 */       size = 4.0D;
/*     */     }
/* 375 */     size *= 64.0D;
/* 376 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   private void showBreakingParticles() {
/* 380 */     if (level() instanceof ServerLevel) {
/* 381 */       ((ServerLevel)level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), getX(), getY(0.6666666666666666D), getZ(), 10, (getBbWidth() / 4.0F), (getBbHeight() / 4.0F), (getBbWidth() / 4.0F), 0.05D);
/*     */     }
/*     */   }
/*     */   
/*     */   private void causeDamage(ServerLevel level, DamageSource source, float dmg) {
/* 386 */     float health = getHealth();
/* 387 */     health -= dmg;
/* 388 */     if (health <= 0.5F) {
/* 389 */       brokenByAnything(level, source);
/* 390 */       kill(level);
/*     */     } else {
/* 392 */       setHealth(health);
/* 393 */       gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void brokenByPlayer(ServerLevel level, DamageSource source) {
/* 398 */     ItemStack result = new ItemStack(Items.ARMOR_STAND);
/* 399 */     result.set(DataComponents.CUSTOM_NAME, getCustomName());
/* 400 */     Block.popResource(level(), blockPosition(), result);
/* 401 */     brokenByAnything(level, source);
/*     */   }
/*     */   
/*     */   private void brokenByAnything(ServerLevel level, DamageSource source) {
/* 405 */     playBrokenSound();
/* 406 */     dropAllDeathLoot(level, source);
/*     */     
/* 408 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 409 */       ItemStack itemStack = this.equipment.set(slot, ItemStack.EMPTY);
/* 410 */       if (!itemStack.isEmpty()) {
/* 411 */         Block.popResource(level(), blockPosition().above(), itemStack);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 417 */   private void playBrokenSound() { level().playSound(null, getX(), getY(), getZ(), SoundEvents.ARMOR_STAND_BREAK, getSoundSource(), 1.0F, 1.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tickHeadTurn(float yBodyRotT) {
/* 422 */     this.yBodyRotO = this.yRotO;
/* 423 */     this.yBodyRot = getYRot();
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 input) {
/* 428 */     if (!hasPhysics()) {
/*     */       return;
/*     */     }
/* 431 */     super.travel(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float yBodyRot) {
/* 436 */     this.yBodyRotO = this.yRotO = yBodyRot;
/* 437 */     this.yHeadRotO = this.yHeadRot = yBodyRot;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYHeadRot(float yHeadRot) {
/* 442 */     this.yBodyRotO = this.yRotO = yHeadRot;
/* 443 */     this.yHeadRotO = this.yHeadRot = yHeadRot;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 448 */   protected void updateInvisibilityStatus() { setInvisible(this.invisible); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInvisible(boolean invisible) {
/* 453 */     this.invisible = invisible;
/* 454 */     super.setInvisible(invisible);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 459 */   public boolean isBaby() { return isSmall(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 465 */     remove(Entity.RemovalReason.KILLED);
/* 466 */     gameEvent(GameEvent.ENTITY_DIE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/* 471 */     if (explosion.shouldAffectBlocklikeEntities()) {
/* 472 */       return isInvisible();
/*     */     }
/* 474 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 479 */     if (isMarker()) {
/* 480 */       return PushReaction.IGNORE;
/*     */     }
/* 482 */     return super.getPistonPushReaction();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 487 */   public boolean isIgnoringBlockTriggers() { return isMarker(); }
/*     */ 
/*     */ 
/*     */   
/* 491 */   private void setSmall(boolean value) { this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 1, value))); }
/*     */ 
/*     */ 
/*     */   
/* 495 */   public boolean isSmall() { return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & true) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 499 */   public void setShowArms(boolean value) { this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 4, value))); }
/*     */ 
/*     */ 
/*     */   
/* 503 */   public boolean showArms() { return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x4) != 0); }
/*     */ 
/*     */ 
/*     */   
/* 507 */   public void setNoBasePlate(boolean value) { this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 8, value))); }
/*     */ 
/*     */ 
/*     */   
/* 511 */   public boolean showBasePlate() { return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x8) == 0); }
/*     */ 
/*     */ 
/*     */   
/* 515 */   private void setMarker(boolean value) { this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 16, value))); }
/*     */ 
/*     */ 
/*     */   
/* 519 */   public boolean isMarker() { return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x10) != 0); }
/*     */ 
/*     */   
/*     */   private byte setBit(byte data, int bit, boolean value) {
/* 523 */     if (value) {
/* 524 */       data = (byte)(data | bit);
/*     */     } else {
/* 526 */       data = (byte)(data & (bit ^ 0xFFFFFFFF));
/*     */     } 
/* 528 */     return data;
/*     */   }
/*     */ 
/*     */   
/* 532 */   public void setHeadPose(Rotations headPose) { this.entityData.set(DATA_HEAD_POSE, headPose); }
/*     */ 
/*     */ 
/*     */   
/* 536 */   public void setBodyPose(Rotations bodyPose) { this.entityData.set(DATA_BODY_POSE, bodyPose); }
/*     */ 
/*     */ 
/*     */   
/* 540 */   public void setLeftArmPose(Rotations leftArmPose) { this.entityData.set(DATA_LEFT_ARM_POSE, leftArmPose); }
/*     */ 
/*     */ 
/*     */   
/* 544 */   public void setRightArmPose(Rotations rightArmPose) { this.entityData.set(DATA_RIGHT_ARM_POSE, rightArmPose); }
/*     */ 
/*     */ 
/*     */   
/* 548 */   public void setLeftLegPose(Rotations leftLegPose) { this.entityData.set(DATA_LEFT_LEG_POSE, leftLegPose); }
/*     */ 
/*     */ 
/*     */   
/* 552 */   public void setRightLegPose(Rotations rightLegPose) { this.entityData.set(DATA_RIGHT_LEG_POSE, rightLegPose); }
/*     */ 
/*     */ 
/*     */   
/* 556 */   public Rotations getHeadPose() { return (Rotations)this.entityData.get(DATA_HEAD_POSE); }
/*     */ 
/*     */ 
/*     */   
/* 560 */   public Rotations getBodyPose() { return (Rotations)this.entityData.get(DATA_BODY_POSE); }
/*     */ 
/*     */ 
/*     */   
/* 564 */   public Rotations getLeftArmPose() { return (Rotations)this.entityData.get(DATA_LEFT_ARM_POSE); }
/*     */ 
/*     */ 
/*     */   
/* 568 */   public Rotations getRightArmPose() { return (Rotations)this.entityData.get(DATA_RIGHT_ARM_POSE); }
/*     */ 
/*     */ 
/*     */   
/* 572 */   public Rotations getLeftLegPose() { return (Rotations)this.entityData.get(DATA_LEFT_LEG_POSE); }
/*     */ 
/*     */ 
/*     */   
/* 576 */   public Rotations getRightLegPose() { return (Rotations)this.entityData.get(DATA_RIGHT_LEG_POSE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 581 */   public boolean isPickable() { return (super.isPickable() && !isMarker()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 586 */   public boolean skipAttackInteraction(Entity source) { if (source instanceof Player) { Player playerSource = (Player)source; if (!level().mayInteract(playerSource, blockPosition())); }  return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 591 */   public HumanoidArm getMainArm() { return HumanoidArm.RIGHT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 596 */   public LivingEntity.Fallsounds getFallSounds() { return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 601 */   protected SoundEvent getHurtSound(DamageSource source) { return SoundEvents.ARMOR_STAND_HIT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 606 */   protected SoundEvent getDeathSound() { return SoundEvents.ARMOR_STAND_BREAK; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {}
/*     */ 
/*     */ 
/*     */   
/* 615 */   public boolean isAffectedByPotions() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 620 */     if (DATA_CLIENT_FLAGS.equals(accessor)) {
/* 621 */       refreshDimensions();
/* 622 */       this.blocksBuilding = !isMarker();
/*     */     } 
/* 624 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 629 */   public boolean attackable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 634 */   public EntityDimensions getDefaultDimensions(Pose pose) { return getDimensionsMarker(isMarker()); }
/*     */ 
/*     */   
/*     */   private EntityDimensions getDimensionsMarker(boolean isMarker) {
/* 638 */     if (isMarker) {
/* 639 */       return MARKER_DIMENSIONS;
/*     */     }
/* 641 */     return isBaby() ? BABY_DIMENSIONS : getType().getDimensions();
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLightProbePosition(float partialTickTime) {
/* 646 */     if (isMarker()) {
/* 647 */       AABB box = getDimensionsMarker(false).makeBoundingBox(position());
/*     */       
/* 649 */       BlockPos probePos = blockPosition();
/* 650 */       int brightestLight = Integer.MIN_VALUE;
/* 651 */       for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
/* 652 */         int blockBrightness = Math.max(level().getBrightness(LightLayer.BLOCK, pos), level().getBrightness(LightLayer.SKY, pos));
/* 653 */         if (blockBrightness == 15) {
/* 654 */           return Vec3.atCenterOf(pos);
/*     */         }
/*     */         
/* 657 */         if (blockBrightness > brightestLight) {
/* 658 */           brightestLight = blockBrightness;
/* 659 */           probePos = pos.immutable();
/*     */         } 
/*     */       } 
/*     */       
/* 663 */       return Vec3.atCenterOf(probePos);
/*     */     } 
/*     */     
/* 666 */     return super.getLightProbePosition(partialTickTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 671 */   public ItemStack getPickResult() { return new ItemStack(Items.ARMOR_STAND); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 676 */   public boolean canBeSeenByAnyone() { return (!isInvisible() && !isMarker()); }
/*     */ 
/*     */   
/*     */   public void setArmorStandPose(ArmorStandPose pose) {
/* 680 */     setHeadPose(pose.head());
/* 681 */     setBodyPose(pose.body());
/* 682 */     setLeftArmPose(pose.leftArm());
/* 683 */     setRightArmPose(pose.rightArm());
/* 684 */     setLeftLegPose(pose.leftLeg());
/* 685 */     setRightLegPose(pose.rightLeg());
/*     */   }
/*     */   
/*     */   public ArmorStandPose getArmorStandPose() {
/* 689 */     return new ArmorStandPose(
/* 690 */         getHeadPose(), 
/* 691 */         getBodyPose(), 
/* 692 */         getLeftArmPose(), 
/* 693 */         getRightArmPose(), 
/* 694 */         getLeftLegPose(), 
/* 695 */         getRightLegPose());
/*     */   }
/*     */   public static final class ArmorStandPose extends Record { private final Rotations head; private final Rotations body; private final Rotations leftArm; private final Rotations rightArm; private final Rotations leftLeg; private final Rotations rightLeg;
/*     */     
/* 699 */     public ArmorStandPose(Rotations head, Rotations body, Rotations leftArm, Rotations rightArm, Rotations leftLeg, Rotations rightLeg) { this.head = head; this.body = body; this.leftArm = leftArm; this.rightArm = rightArm; this.leftLeg = leftLeg; this.rightLeg = rightLeg; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 699 */       //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose; } public Rotations head() { return this.head; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #699	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/* 699 */       //   0	8	1	o	Ljava/lang/Object; } public Rotations body() { return this.body; } public Rotations leftArm() { return this.leftArm; } public Rotations rightArm() { return this.rightArm; } public Rotations leftLeg() { return this.leftLeg; } public Rotations rightLeg() { return this.rightLeg; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 707 */     public static final ArmorStandPose DEFAULT = new ArmorStandPose(ArmorStand.DEFAULT_HEAD_POSE, ArmorStand.DEFAULT_BODY_POSE, ArmorStand.DEFAULT_LEFT_ARM_POSE, ArmorStand.DEFAULT_RIGHT_ARM_POSE, ArmorStand.DEFAULT_LEFT_LEG_POSE, ArmorStand.DEFAULT_RIGHT_LEG_POSE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 716 */     public static final Codec<ArmorStandPose> CODEC = RecordCodecBuilder.create(i -> i.group(Rotations.CODEC
/* 717 */           .optionalFieldOf("Head", ArmorStand.DEFAULT_HEAD_POSE).forGetter(ArmorStandPose::head), Rotations.CODEC
/* 718 */           .optionalFieldOf("Body", ArmorStand.DEFAULT_BODY_POSE).forGetter(ArmorStandPose::body), Rotations.CODEC
/* 719 */           .optionalFieldOf("LeftArm", ArmorStand.DEFAULT_LEFT_ARM_POSE).forGetter(ArmorStandPose::leftArm), Rotations.CODEC
/* 720 */           .optionalFieldOf("RightArm", ArmorStand.DEFAULT_RIGHT_ARM_POSE).forGetter(ArmorStandPose::rightArm), Rotations.CODEC
/* 721 */           .optionalFieldOf("LeftLeg", ArmorStand.DEFAULT_LEFT_LEG_POSE).forGetter(ArmorStandPose::leftLeg), Rotations.CODEC
/* 722 */           .optionalFieldOf("RightLeg", ArmorStand.DEFAULT_RIGHT_LEG_POSE).forGetter(ArmorStandPose::rightLeg))
/* 723 */         .apply(i, ArmorStandPose::new)); }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\decoration\ArmorStand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */