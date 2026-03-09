/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Projectile
/*     */   extends Entity
/*     */   implements TraceableEntity
/*     */ {
/*     */   private static final boolean DEFAULT_LEFT_OWNER = false;
/*     */   private static final boolean DEFAULT_HAS_BEEN_SHOT = false;
/*     */   protected EntityReference<Entity> owner;
/*     */   private boolean leftOwner = false;
/*     */   private boolean leftOwnerChecked;
/*     */   private boolean hasBeenShot = false;
/*     */   private Entity lastDeflectedBy;
/*     */   
/*  54 */   protected Projectile(EntityType<? extends Projectile> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   protected void setOwner(EntityReference<Entity> owner) { this.owner = owner; }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public void setOwner(Entity owner) { setOwner(EntityReference.of(owner)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public Entity getOwner() { return EntityReference.getEntity(this.owner, level()); }
/*     */ 
/*     */ 
/*     */   
/*  71 */   public Entity getEffectSource() { return (Entity)MoreObjects.firstNonNull(getOwner(), this); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  76 */     EntityReference.store(this.owner, output, "Owner");
/*  77 */     if (this.leftOwner) {
/*  78 */       output.putBoolean("LeftOwner", true);
/*     */     }
/*  80 */     output.putBoolean("HasBeenShot", this.hasBeenShot);
/*     */   }
/*     */ 
/*     */   
/*  84 */   protected boolean ownedBy(Entity entity) { return (this.owner != null && this.owner.matches(entity)); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  89 */     setOwner(EntityReference.read(input, "Owner"));
/*  90 */     this.leftOwner = input.getBooleanOr("LeftOwner", false);
/*  91 */     this.hasBeenShot = input.getBooleanOr("HasBeenShot", false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/*  96 */     super.restoreFrom(oldEntity);
/*  97 */     if (oldEntity instanceof Projectile) { Projectile projectile = (Projectile)oldEntity;
/*  98 */       this.owner = projectile.owner; }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 104 */     if (!this.hasBeenShot) {
/* 105 */       gameEvent(GameEvent.PROJECTILE_SHOOT, getOwner());
/* 106 */       this.hasBeenShot = true;
/*     */     } 
/*     */     
/* 109 */     checkLeftOwner();
/* 110 */     super.tick();
/* 111 */     this.leftOwnerChecked = false;
/*     */   }
/*     */   
/*     */   protected void checkLeftOwner() {
/* 115 */     if (!this.leftOwner && !this.leftOwnerChecked) {
/* 116 */       this.leftOwner = isOutsideOwnerCollisionRange();
/* 117 */       this.leftOwnerChecked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isOutsideOwnerCollisionRange() {
/* 122 */     Entity owner = getOwner();
/* 123 */     if (owner != null) {
/* 124 */       AABB aabb = getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D);
/* 125 */       return owner.getRootVehicle().getSelfAndPassengers()
/* 126 */         .filter(EntitySelector.CAN_BE_PICKED)
/* 127 */         .noneMatch(entity -> aabb.intersects(entity.getBoundingBox()));
/*     */     } 
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getMovementToShoot(double xd, double yd, double zd, float pow, float uncertainty) {
/* 133 */     return (new Vec3(xd, yd, zd)).normalize().add(this.random
/* 134 */         .triangle(0.0D, 0.0172275D * uncertainty), this.random
/* 135 */         .triangle(0.0D, 0.0172275D * uncertainty), this.random
/* 136 */         .triangle(0.0D, 0.0172275D * uncertainty))
/* 137 */       .scale(pow);
/*     */   }
/*     */   
/*     */   public void shoot(double xd, double yd, double zd, float pow, float uncertainty) {
/* 141 */     Vec3 movement = getMovementToShoot(xd, yd, zd, pow, uncertainty);
/* 142 */     setDeltaMovement(movement);
/* 143 */     this.needsSync = true;
/*     */     
/* 145 */     double sd = movement.horizontalDistance();
/*     */     
/* 147 */     setYRot((float)(Mth.atan2(movement.x, movement.z) * 57.2957763671875D));
/* 148 */     setXRot((float)(Mth.atan2(movement.y, sd) * 57.2957763671875D));
/* 149 */     this.yRotO = getYRot();
/* 150 */     this.xRotO = getXRot();
/*     */   }
/*     */   
/*     */   public void shootFromRotation(Entity source, float xRot, float yRot, float yOffset, float pow, float uncertainty) {
/* 154 */     float xd = -Mth.sin((yRot * 0.017453292F)) * Mth.cos((xRot * 0.017453292F));
/* 155 */     float yd = -Mth.sin(((xRot + yOffset) * 0.017453292F));
/* 156 */     float zd = Mth.cos((yRot * 0.017453292F)) * Mth.cos((xRot * 0.017453292F));
/* 157 */     shoot(xd, yd, zd, pow, uncertainty);
/*     */     
/* 159 */     Vec3 sourceMovement = source.getKnownMovement();
/* 160 */     setDeltaMovement(getDeltaMovement().add(sourceMovement.x, 
/*     */           
/* 162 */           source.onGround() ? 0.0D : sourceMovement.y, sourceMovement.z));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 169 */     double yd = dragDown ? -0.03D : 0.1D;
/* 170 */     setDeltaMovement(getDeltaMovement().add(0.0D, yd, 0.0D));
/* 171 */     sendBubbleColumnParticles(level(), pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInsideBubbleColumn(boolean dragDown) {
/* 176 */     double yd = dragDown ? -0.03D : 0.06D;
/* 177 */     setDeltaMovement(getDeltaMovement().add(0.0D, yd, 0.0D));
/* 178 */     resetFallDistance();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   public static <T extends Projectile> T spawnProjectileFromRotation(ProjectileFactory<T> creator, ServerLevel serverLevel, ItemStack itemStack, LivingEntity source, float yOffset, float pow, float uncertainty) { return (T)spawnProjectile(creator.create(serverLevel, source, itemStack), serverLevel, itemStack, projectile -> projectile.shootFromRotation(source, source.getXRot(), source.getYRot(), yOffset, pow, uncertainty)); }
/*     */ 
/*     */ 
/*     */   
/* 191 */   public static <T extends Projectile> T spawnProjectileUsingShoot(ProjectileFactory<T> creator, ServerLevel serverLevel, ItemStack itemStack, LivingEntity source, double targetX, double targetY, double targetZ, float pow, float uncertainty) { return (T)spawnProjectile(creator.create(serverLevel, source, itemStack), serverLevel, itemStack, projectile -> projectile.shoot(targetX, targetY, targetZ, pow, uncertainty)); }
/*     */ 
/*     */ 
/*     */   
/* 195 */   public static <T extends Projectile> T spawnProjectileUsingShoot(T projectile, ServerLevel serverLevel, ItemStack itemStack, double targetX, double targetY, double targetZ, float pow, float uncertainty) { return (T)spawnProjectile(projectile, serverLevel, itemStack, i -> projectile.shoot(targetX, targetY, targetZ, pow, uncertainty)); }
/*     */ 
/*     */ 
/*     */   
/* 199 */   public static <T extends Projectile> T spawnProjectile(T projectile, ServerLevel serverLevel, ItemStack itemStack) { return (T)spawnProjectile(projectile, serverLevel, itemStack, ignored -> {
/*     */         
/*     */         }); }
/*     */   public static <T extends Projectile> T spawnProjectile(T projectile, ServerLevel serverLevel, ItemStack itemStack, Consumer<T> shootFunction) {
/* 203 */     shootFunction.accept(projectile);
/* 204 */     serverLevel.addFreshEntity(projectile);
/*     */     
/* 206 */     projectile.applyOnProjectileSpawned(serverLevel, itemStack);
/*     */     
/* 208 */     return projectile;
/*     */   }
/*     */   
/*     */   public void applyOnProjectileSpawned(ServerLevel serverLevel, ItemStack pickupItemStack) {
/* 212 */     EnchantmentHelper.onProjectileSpawned(serverLevel, pickupItemStack, this, item -> { 
/* 213 */         }); Projectile projectile = this; if (projectile instanceof AbstractArrow) { AbstractArrow arrow = (AbstractArrow)projectile;
/* 214 */       ItemStack weapon = arrow.getWeaponItem();
/* 215 */       if (weapon != null && !weapon.isEmpty() && !pickupItemStack.getItem().equals(weapon.getItem())) {
/* 216 */         Objects.requireNonNull(arrow); EnchantmentHelper.onProjectileSpawned(serverLevel, weapon, this, arrow::onItemBreak);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   protected ProjectileDeflection hitTargetOrDeflectSelf(HitResult hitResult) {
/* 222 */     if (hitResult.getType() == HitResult.Type.ENTITY)
/* 223 */     { EntityHitResult entityHitResult = (EntityHitResult)hitResult;
/* 224 */       Entity entity = entityHitResult.getEntity();
/* 225 */       ProjectileDeflection deflection = entity.deflection(this);
/* 226 */       if (deflection != ProjectileDeflection.NONE) {
/* 227 */         if (entity != this.lastDeflectedBy && deflect(deflection, entity, this.owner, false)) {
/* 228 */           this.lastDeflectedBy = entity;
/*     */         }
/* 230 */         return deflection;
/*     */       }  }
/* 232 */     else if (shouldBounceOnWorldBorder() && hitResult instanceof BlockHitResult) { BlockHitResult blockHit = (BlockHitResult)hitResult; if (blockHit.isWorldBorderHit()) {
/* 233 */         ProjectileDeflection deflection = ProjectileDeflection.REVERSE;
/* 234 */         if (deflect(deflection, null, this.owner, false)) {
/* 235 */           setDeltaMovement(getDeltaMovement().scale(0.2D));
/* 236 */           return deflection;
/*     */         } 
/*     */       }  }
/*     */     
/* 240 */     onHit(hitResult);
/* 241 */     return ProjectileDeflection.NONE;
/*     */   }
/*     */ 
/*     */   
/* 245 */   protected boolean shouldBounceOnWorldBorder() { return false; }
/*     */ 
/*     */   
/*     */   public boolean deflect(ProjectileDeflection deflection, Entity deflectingEntity, EntityReference<Entity> newOwner, boolean byAttack) {
/* 249 */     deflection.deflect(this, deflectingEntity, this.random);
/* 250 */     if (!level().isClientSide()) {
/* 251 */       setOwner(newOwner);
/* 252 */       onDeflection(byAttack);
/*     */     } 
/* 254 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDeflection(boolean byAttack) {}
/*     */ 
/*     */   
/*     */   protected void onItemBreak(Item item) {}
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/* 264 */     HitResult.Type type = hitResult.getType();
/* 265 */     if (type == HitResult.Type.ENTITY) {
/* 266 */       EntityHitResult entityHitResult = (EntityHitResult)hitResult;
/* 267 */       Entity entityHit = entityHitResult.getEntity();
/* 268 */       if (entityHit.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entityHit instanceof Projectile) {
/* 269 */         Projectile projectile = (Projectile)entityHit;
/*     */         
/* 271 */         projectile.deflect(ProjectileDeflection.AIM_DEFLECT, getOwner(), this.owner, true);
/*     */       } 
/* 273 */       onHitEntity(entityHitResult);
/* 274 */       level().gameEvent(GameEvent.PROJECTILE_LAND, hitResult.getLocation(), GameEvent.Context.of(this, null));
/* 275 */     } else if (type == HitResult.Type.BLOCK) {
/* 276 */       BlockHitResult blockHit = (BlockHitResult)hitResult;
/* 277 */       onHitBlock(blockHit);
/* 278 */       BlockPos target = blockHit.getBlockPos();
/* 279 */       level().gameEvent(GameEvent.PROJECTILE_LAND, target, GameEvent.Context.of(this, level().getBlockState(target)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {}
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 287 */     BlockState state = level().getBlockState(hitResult.getBlockPos());
/* 288 */     state.onProjectileHit(level(), state, hitResult, this);
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 292 */     if (!entity.canBeHitByProjectile()) {
/* 293 */       return false;
/*     */     }
/* 295 */     Entity owner = getOwner();
/* 296 */     return (owner == null || this.leftOwner || !owner.isPassengerOfSameVehicle(entity));
/*     */   }
/*     */   
/*     */   protected void updateRotation() {
/* 300 */     Vec3 movement = getDeltaMovement();
/* 301 */     double sd = movement.horizontalDistance();
/*     */     
/* 303 */     setXRot(lerpRotation(this.xRotO, (float)(Mth.atan2(movement.y, sd) * 57.2957763671875D)));
/* 304 */     setYRot(lerpRotation(this.yRotO, (float)(Mth.atan2(movement.x, movement.z) * 57.2957763671875D)));
/*     */   }
/*     */   
/*     */   protected static float lerpRotation(float rotO, float rot) {
/* 308 */     while (rot - rotO < -180.0F) {
/* 309 */       rotO -= 360.0F;
/*     */     }
/* 311 */     while (rot - rotO >= 180.0F) {
/* 312 */       rotO += 360.0F;
/*     */     }
/* 314 */     return Mth.lerp(0.2F, rotO, rot);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 319 */     Entity owner = getOwner();
/* 320 */     return new ClientboundAddEntityPacket(this, serverEntity, (owner == null) ? 0 : owner.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 325 */     super.recreateFromPacket(packet);
/* 326 */     Entity owner = level().getEntity(packet.getData());
/* 327 */     if (owner != null) {
/* 328 */       setOwner(owner);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mayInteract(ServerLevel level, BlockPos pos) {
/* 334 */     Entity owner = getOwner();
/* 335 */     if (owner instanceof net.minecraft.world.entity.player.Player) {
/* 336 */       return owner.mayInteract(level, pos);
/*     */     }
/* 338 */     return (owner == null || ((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*     */   }
/*     */ 
/*     */   
/* 342 */   public boolean mayBreak(ServerLevel level) { return (getType().is(EntityTypeTags.IMPACT_PROJECTILES) && ((Boolean)level.getGameRules().get(GameRules.PROJECTILES_CAN_BREAK_BLOCKS)).booleanValue()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 347 */   public boolean isPickable() { return getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 352 */   public float getPickRadius() { return isPickable() ? 1.0F : 0.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity hurtEntity, DamageSource damageSource) {
/* 358 */     double dx = (getDeltaMovement()).x;
/* 359 */     double dz = (getDeltaMovement()).z;
/* 360 */     return DoubleDoubleImmutablePair.of(dx, dz);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 365 */   public int getDimensionChangingDelay() { return 2; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 370 */     if (!isInvulnerableToBase(source)) {
/* 371 */       markHurt();
/*     */     }
/*     */     
/* 374 */     return false;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ProjectileFactory<T extends Projectile> {
/*     */     T create(ServerLevel param1ServerLevel, LivingEntity param1LivingEntity, ItemStack param1ItemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\Projectile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */