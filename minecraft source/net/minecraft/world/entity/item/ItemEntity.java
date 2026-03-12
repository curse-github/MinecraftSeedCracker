/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemEntity
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*  43 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private static final float FLOAT_HEIGHT = 0.1F;
/*     */   
/*     */   public static final float EYE_HEIGHT = 0.2125F;
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   
/*     */   private static final int INFINITE_PICKUP_DELAY = 32767;
/*     */   
/*     */   private static final int INFINITE_LIFETIME = -32768;
/*     */   
/*     */   private static final int DEFAULT_HEALTH = 5;
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   private static final short DEFAULT_PICKUP_DELAY = 0;
/*  58 */   private int age = 0;
/*  59 */   private int pickupDelay = 0;
/*  60 */   private int health = 5;
/*     */   private EntityReference<Entity> thrower;
/*     */   private UUID target;
/*     */   public final float bobOffs;
/*     */   
/*     */   public ItemEntity(EntityType<? extends ItemEntity> type, Level level) {
/*  66 */     super(type, level);
/*  67 */     this.bobOffs = this.random.nextFloat() * 3.1415927F * 2.0F;
/*  68 */     setYRot(this.random.nextFloat() * 360.0F);
/*     */   }
/*     */ 
/*     */   
/*  72 */   public ItemEntity(Level level, double x, double y, double z, ItemStack itemStack) { this(level, x, y, z, itemStack, level.random.nextDouble() * 0.2D - 0.1D, 0.2D, level.random.nextDouble() * 0.2D - 0.1D); }
/*     */ 
/*     */   
/*     */   public ItemEntity(Level level, double x, double y, double z, ItemStack itemStack, double deltaX, double deltaY, double deltaZ) {
/*  76 */     this(EntityType.ITEM, level);
/*  77 */     setPos(x, y, z);
/*  78 */     setDeltaMovement(deltaX, deltaY, deltaZ);
/*  79 */     setItem(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  84 */   public boolean dampensVibrations() { return getItem().is(ItemTags.DAMPENS_VIBRATIONS); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public Entity getOwner() { return EntityReference.getEntity(this.thrower, level()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/*  94 */     super.restoreFrom(oldEntity);
/*  95 */     if (oldEntity instanceof ItemEntity) { ItemEntity item = (ItemEntity)oldEntity;
/*  96 */       this.thrower = item.thrower; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 102 */   protected Entity.MovementEmission getMovementEmission() { return Entity.MovementEmission.NONE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   protected void defineSynchedData(SynchedEntityData.Builder entityData) { entityData.define(DATA_ITEM, ItemStack.EMPTY); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   protected double getDefaultGravity() { return 0.04D; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 117 */     if (getItem().isEmpty()) {
/* 118 */       discard();
/*     */       return;
/*     */     } 
/* 121 */     super.tick();
/* 122 */     if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
/* 123 */       this.pickupDelay--;
/*     */     }
/* 125 */     this.xo = getX();
/* 126 */     this.yo = getY();
/* 127 */     this.zo = getZ();
/*     */     
/* 129 */     Vec3 oldMovement = getDeltaMovement();
/*     */ 
/*     */     
/* 132 */     if (isInWater() && getFluidHeight(FluidTags.WATER) > 0.10000000149011612D) {
/* 133 */       setUnderwaterMovement();
/* 134 */     } else if (isInLava() && getFluidHeight(FluidTags.LAVA) > 0.10000000149011612D) {
/* 135 */       setUnderLavaMovement();
/*     */     } else {
/* 137 */       applyGravity();
/*     */     } 
/*     */     
/* 140 */     if (level().isClientSide()) {
/* 141 */       this.noPhysics = false;
/*     */     } else {
/* 143 */       this.noPhysics = !level().noCollision(this, getBoundingBox().deflate(1.0E-7D));
/* 144 */       if (this.noPhysics) {
/* 145 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/*     */       }
/*     */     } 
/* 148 */     if (!onGround() || getDeltaMovement().horizontalDistanceSqr() > 9.999999747378752E-6D || (this.tickCount + getId()) % 4 == 0) {
/* 149 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 151 */       applyEffectsFromBlocks();
/*     */       
/* 153 */       float friction = 0.98F;
/* 154 */       if (onGround()) {
/* 155 */         friction = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.98F;
/*     */       }
/*     */       
/* 158 */       setDeltaMovement(getDeltaMovement().multiply(friction, 0.98D, friction));
/*     */ 
/*     */       
/* 161 */       if (onGround()) {
/* 162 */         Vec3 movement = getDeltaMovement();
/* 163 */         if (movement.y < 0.0D) {
/* 164 */           setDeltaMovement(movement.multiply(1.0D, -0.5D, 1.0D));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     boolean moved = (Mth.floor(this.xo) != Mth.floor(getX()) || Mth.floor(this.yo) != Mth.floor(getY()) || Mth.floor(this.zo) != Mth.floor(getZ()));
/* 170 */     int rate = moved ? 2 : 40;
/*     */     
/* 172 */     if (this.tickCount % rate == 0 && 
/* 173 */       !level().isClientSide() && isMergable()) {
/* 174 */       mergeWithNeighbours();
/*     */     }
/*     */ 
/*     */     
/* 178 */     if (this.age != -32768) {
/* 179 */       this.age++;
/*     */     }
/*     */ 
/*     */     
/* 183 */     this.needsSync |= updateInWaterStateAndDoFluidPushing();
/*     */     
/* 185 */     if (!level().isClientSide()) {
/*     */ 
/*     */ 
/*     */       
/* 189 */       double value = getDeltaMovement().subtract(oldMovement).lengthSqr();
/* 190 */       if (value > 0.01D) {
/* 191 */         this.needsSync = true;
/*     */       }
/*     */     } 
/*     */     
/* 195 */     if (!level().isClientSide() && this.age >= 6000) {
/* 196 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   public BlockPos getBlockPosBelowThatAffectsMyMovement() { return getOnPos(0.999999F); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   private void setUnderwaterMovement() { setFluidMovement(0.9900000095367432D); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 213 */   private void setUnderLavaMovement() { setFluidMovement(0.949999988079071D); }
/*     */ 
/*     */   
/*     */   private void setFluidMovement(double multiplier) {
/* 217 */     Vec3 movement = getDeltaMovement();
/* 218 */     setDeltaMovement(movement.x * multiplier, movement.y + (
/*     */         
/* 220 */         (movement.y < 0.05999999865889549D) ? 5.0E-4F : 0.0F), movement.z * multiplier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void mergeWithNeighbours() {
/* 226 */     if (!isMergable()) {
/*     */       return;
/*     */     }
/* 229 */     List<ItemEntity> items = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.5D, 0.0D, 0.5D), other -> (other != this && other.isMergable()));
/* 230 */     for (ItemEntity entity : items) {
/* 231 */       if (entity.isMergable()) {
/* 232 */         tryToMerge(entity);
/* 233 */         if (isRemoved()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMergable() {
/* 241 */     ItemStack item = getItem();
/* 242 */     return (isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && item.getCount() < item.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private void tryToMerge(ItemEntity other) {
/* 246 */     ItemStack thisItemStack = getItem();
/* 247 */     ItemStack otherItemStack = other.getItem();
/*     */     
/* 249 */     if (!Objects.equals(this.target, other.target) || !areMergable(thisItemStack, otherItemStack)) {
/*     */       return;
/*     */     }
/*     */     
/* 253 */     if (otherItemStack.getCount() < thisItemStack.getCount()) {
/* 254 */       merge(this, thisItemStack, other, otherItemStack);
/*     */     } else {
/* 256 */       merge(other, otherItemStack, this, thisItemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean areMergable(ItemStack thisItemStack, ItemStack otherItemStack) {
/* 261 */     if (otherItemStack.getCount() + thisItemStack.getCount() > otherItemStack.getMaxStackSize()) {
/* 262 */       return false;
/*     */     }
/* 264 */     return ItemStack.isSameItemSameComponents(thisItemStack, otherItemStack);
/*     */   }
/*     */   
/*     */   public static ItemStack merge(ItemStack toStack, ItemStack fromStack, int maxCount) {
/* 268 */     int delta = Math.min(Math.min(toStack.getMaxStackSize(), maxCount) - toStack.getCount(), fromStack.getCount());
/* 269 */     ItemStack newToStack = toStack.copyWithCount(toStack.getCount() + delta);
/* 270 */     fromStack.shrink(delta);
/* 271 */     return newToStack;
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity toItem, ItemStack toStack, ItemStack fromStack) {
/* 275 */     ItemStack newToStack = merge(toStack, fromStack, 64);
/* 276 */     toItem.setItem(newToStack);
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity toItem, ItemStack toStack, ItemEntity fromItem, ItemStack fromStack) {
/* 280 */     merge(toItem, toStack, fromStack);
/* 281 */     toItem.pickupDelay = Math.max(toItem.pickupDelay, fromItem.pickupDelay);
/* 282 */     toItem.age = Math.min(toItem.age, fromItem.age);
/*     */     
/* 284 */     if (fromStack.isEmpty()) {
/* 285 */       fromItem.discard();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 291 */   public boolean fireImmune() { return (!getItem().canBeHurtBy(damageSources().inFire()) || super.fireImmune()); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldPlayLavaHurtSound() {
/* 296 */     if (this.health <= 0) {
/* 297 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 302 */     return (this.tickCount % 10 == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtClient(DamageSource source) {
/* 307 */     if (isInvulnerableToBase(source)) {
/* 308 */       return false;
/*     */     }
/* 310 */     return getItem().canBeHurtBy(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 315 */     if (isInvulnerableToBase(source)) {
/* 316 */       return false;
/*     */     }
/* 318 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && source.getEntity() instanceof net.minecraft.world.entity.Mob) {
/* 319 */       return false;
/*     */     }
/* 321 */     if (!getItem().canBeHurtBy(source)) {
/* 322 */       return false;
/*     */     }
/* 324 */     markHurt();
/* 325 */     this.health = (int)(this.health - damage);
/* 326 */     gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
/* 327 */     if (this.health <= 0) {
/* 328 */       getItem().onDestroyed(this);
/* 329 */       discard();
/*     */     } 
/* 331 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/* 336 */     if (explosion.shouldAffectBlocklikeEntities()) {
/* 337 */       return super.ignoreExplosion(explosion);
/*     */     }
/* 339 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 344 */     output.putShort("Health", (short)this.health);
/* 345 */     output.putShort("Age", (short)this.age);
/* 346 */     output.putShort("PickupDelay", (short)this.pickupDelay);
/* 347 */     EntityReference.store(this.thrower, output, "Thrower");
/* 348 */     output.storeNullable("Owner", UUIDUtil.CODEC, this.target);
/* 349 */     if (!getItem().isEmpty()) {
/* 350 */       output.store("Item", ItemStack.CODEC, getItem());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 356 */     this.health = input.getShortOr("Health", (short)5);
/* 357 */     this.age = input.getShortOr("Age", (short)0);
/* 358 */     this.pickupDelay = input.getShortOr("PickupDelay", (short)0);
/* 359 */     this.target = (UUID)input.read("Owner", UUIDUtil.CODEC).orElse(null);
/* 360 */     this.thrower = EntityReference.read(input, "Thrower");
/* 361 */     setItem((ItemStack)input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 362 */     if (getItem().isEmpty()) {
/* 363 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player player) {
/* 369 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 373 */     ItemStack itemStack = getItem();
/* 374 */     Item item = itemStack.getItem();
/* 375 */     int orgCount = itemStack.getCount();
/* 376 */     if (this.pickupDelay == 0 && (this.target == null || this.target.equals(player.getUUID())) && player.getInventory().add(itemStack)) {
/* 377 */       player.take(this, orgCount);
/* 378 */       if (itemStack.isEmpty()) {
/* 379 */         discard();
/*     */ 
/*     */         
/* 382 */         itemStack.setCount(orgCount);
/*     */       } 
/* 384 */       player.awardStat(Stats.ITEM_PICKED_UP.get(item), orgCount);
/* 385 */       player.onItemPickup(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getName() {
/* 391 */     Component name = getCustomName();
/* 392 */     if (name != null) {
/* 393 */       return name;
/*     */     }
/*     */     
/* 396 */     return getItem().getItemName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 401 */   public boolean isAttackable() { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 406 */     Entity entity = super.teleport(transition);
/*     */     
/* 408 */     if (!level().isClientSide() && entity instanceof ItemEntity) { ItemEntity item = (ItemEntity)entity;
/* 409 */       item.mergeWithNeighbours(); }
/*     */     
/* 411 */     return entity;
/*     */   }
/*     */ 
/*     */   
/* 415 */   public ItemStack getItem() { return (ItemStack)getEntityData().get(DATA_ITEM); }
/*     */ 
/*     */ 
/*     */   
/* 419 */   public void setItem(ItemStack itemStack) { getEntityData().set(DATA_ITEM, itemStack); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 424 */     super.onSyncedDataUpdated(accessor);
/* 425 */     if (DATA_ITEM.equals(accessor)) {
/* 426 */       getItem().setEntityRepresentation(this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 434 */   public void setTarget(UUID target) { this.target = target; }
/*     */ 
/*     */ 
/*     */   
/* 438 */   public void setThrower(Entity thrower) { this.thrower = EntityReference.of(thrower); }
/*     */ 
/*     */ 
/*     */   
/* 442 */   public int getAge() { return this.age; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 447 */   public void setDefaultPickUpDelay() { this.pickupDelay = 10; }
/*     */ 
/*     */ 
/*     */   
/* 451 */   public void setNoPickUpDelay() { this.pickupDelay = 0; }
/*     */ 
/*     */ 
/*     */   
/* 455 */   public void setNeverPickUp() { this.pickupDelay = 32767; }
/*     */ 
/*     */ 
/*     */   
/* 459 */   public void setPickUpDelay(int ticks) { this.pickupDelay = ticks; }
/*     */ 
/*     */ 
/*     */   
/* 463 */   public boolean hasPickUpDelay() { return (this.pickupDelay > 0); }
/*     */ 
/*     */ 
/*     */   
/* 467 */   public void setUnlimitedLifetime() { this.age = -32768; }
/*     */ 
/*     */ 
/*     */   
/* 471 */   public void setExtendedLifetime() { this.age = -6000; }
/*     */ 
/*     */   
/*     */   public void makeFakeItem() {
/* 475 */     setNeverPickUp();
/* 476 */     this.age = 5999;
/*     */   }
/*     */ 
/*     */   
/* 480 */   public static float getSpin(float ageInTicks, float bobOffset) { return ageInTicks / 20.0F + bobOffset; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 485 */   public SoundSource getSoundSource() { return SoundSource.AMBIENT; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 490 */   public float getVisualRotationYInDegrees() { return 180.0F - getSpin(getAge() + 0.5F, this.bobOffs) / 6.2831855F * 360.0F; }
/*     */ 
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 495 */     if (slot == 0) {
/* 496 */       return SlotAccess.of(this::getItem, this::setItem);
/*     */     }
/* 498 */     return super.getSlot(slot);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\item\ItemEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */