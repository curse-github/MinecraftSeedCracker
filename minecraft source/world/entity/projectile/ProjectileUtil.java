/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*     */ import net.minecraft.world.item.ArrowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.AttackRange;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
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
/*     */ 
/*     */ public final class ProjectileUtil
/*     */ {
/*     */   public static final float DEFAULT_ENTITY_HIT_RESULT_MARGIN = 0.3F;
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity source, Predicate<Entity> matching) {
/*  37 */     Vec3 movement = source.getDeltaMovement();
/*  38 */     Level level = source.level();
/*     */     
/*  40 */     Vec3 from = source.position();
/*  41 */     return getHitResult(from, source, matching, movement, level, computeMargin(source), ClipContext.Block.COLLIDER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity attacker, AttackRange attackRange, Predicate<Entity> matching, ClipContext.Block blockClipType) {
/*  48 */     Vec3 look = attacker.getHeadLookAngle();
/*  49 */     Vec3 eyePosition = attacker.getEyePosition();
/*  50 */     Vec3 from = eyePosition.add(look.scale(attackRange.effectiveMinRange(attacker)));
/*  51 */     double movementComponent = attacker.getKnownMovement().dot(look);
/*  52 */     Vec3 to = eyePosition.add(look.scale(attackRange.effectiveMaxRange(attacker) + Math.max(0.0D, movementComponent)));
/*  53 */     return getHitEntitiesAlong(attacker, eyePosition, from, matching, to, attackRange.hitboxMargin(), blockClipType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HitResult getHitResultOnMoveVector(Entity source, Predicate<Entity> matching, ClipContext.Block clipType) {
/*  60 */     Vec3 movement = source.getDeltaMovement();
/*  61 */     Level level = source.level();
/*     */     
/*  63 */     Vec3 from = source.position();
/*  64 */     return getHitResult(from, source, matching, movement, level, computeMargin(source), clipType);
/*     */   }
/*     */   
/*     */   public static HitResult getHitResultOnViewVector(Entity source, Predicate<Entity> matching, double distance) {
/*  68 */     Vec3 viewVector = source.getViewVector(0.0F).scale(distance);
/*  69 */     Level level = source.level();
/*     */     
/*  71 */     Vec3 from = source.getEyePosition();
/*  72 */     return getHitResult(from, source, matching, viewVector, level, 0.0F, ClipContext.Block.COLLIDER);
/*     */   }
/*     */   
/*     */   private static HitResult getHitResult(Vec3 from, Entity source, Predicate<Entity> matching, Vec3 delta, Level level, float entityMargin, ClipContext.Block clipType) {
/*  76 */     Vec3 to = from.add(delta);
/*  77 */     EntityHitResult entityHitResult1 = level.clipIncludingBorder(new ClipContext(from, to, clipType, ClipContext.Fluid.NONE, source));
/*     */     
/*  79 */     if (entityHitResult1.getType() != HitResult.Type.MISS) {
/*  80 */       to = entityHitResult1.getLocation();
/*     */     }
/*  82 */     EntityHitResult entityHitResult2 = getEntityHitResult(level, source, from, to, source.getBoundingBox().expandTowards(delta).inflate(1.0D), matching, entityMargin);
/*     */     
/*  84 */     if (entityHitResult2 != null) {
/*  85 */       entityHitResult1 = entityHitResult2;
/*     */     }
/*     */     
/*  88 */     return entityHitResult1;
/*     */   }
/*     */   
/*     */   private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(Entity source, Vec3 origin, Vec3 from, Predicate<Entity> matching, Vec3 to, float entityMargin, ClipContext.Block clipType) {
/*  92 */     Level level = source.level();
/*     */     
/*  94 */     BlockHitResult hitResult = level.clipIncludingBorder(new ClipContext(origin, to, clipType, ClipContext.Fluid.NONE, source));
/*     */     
/*  96 */     if (hitResult.getType() != HitResult.Type.MISS) {
/*  97 */       to = hitResult.getLocation();
/*  98 */       if (origin.distanceToSqr(to) < origin.distanceToSqr(from))
/*     */       {
/* 100 */         return Either.left(hitResult);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 105 */     AABB searchArea = AABB.ofSize(from, entityMargin, entityMargin, entityMargin).expandTowards(to.subtract(from)).inflate(1.0D);
/* 106 */     Collection<EntityHitResult> entityHit = getManyEntityHitResult(level, source, from, to, searchArea, matching, entityMargin, clipType, true);
/*     */     
/* 108 */     if (!entityHit.isEmpty()) {
/* 109 */       return Either.right(entityHit);
/*     */     }
/*     */     
/* 112 */     return Either.left(hitResult);
/*     */   }
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Entity except, Vec3 from, Vec3 to, AABB box, Predicate<Entity> matching, double maxValue) {
/* 116 */     Level level = except.level();
/* 117 */     double nearest = maxValue;
/* 118 */     Entity hovered = null;
/* 119 */     Vec3 hoveredPos = null;
/*     */     
/* 121 */     for (Entity entity : level.getEntities(except, box, matching)) {
/* 122 */       AABB bb = entity.getBoundingBox().inflate(entity.getPickRadius());
/* 123 */       Optional<Vec3> clipPoint = bb.clip(from, to);
/* 124 */       if (bb.contains(from)) {
/* 125 */         if (nearest >= 0.0D) {
/* 126 */           hovered = entity;
/* 127 */           hoveredPos = (Vec3)clipPoint.orElse(from);
/* 128 */           nearest = 0.0D;
/*     */         }  continue;
/*     */       } 
/* 131 */       if (clipPoint.isPresent()) {
/* 132 */         Vec3 location = (Vec3)clipPoint.get();
/* 133 */         double dd = from.distanceToSqr(location);
/* 134 */         if (dd < nearest || nearest == 0.0D) {
/* 135 */           if (entity.getRootVehicle() == except.getRootVehicle()) {
/* 136 */             if (nearest == 0.0D) {
/* 137 */               hovered = entity;
/* 138 */               hoveredPos = location;
/*     */             }  continue;
/*     */           } 
/* 141 */           hovered = entity;
/* 142 */           hoveredPos = location;
/* 143 */           nearest = dd;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (hovered == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     return new EntityHitResult(hovered, hoveredPos);
/*     */   }
/*     */ 
/*     */   
/* 157 */   public static EntityHitResult getEntityHitResult(Level level, Projectile source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching) { return getEntityHitResult(level, source, from, to, targetSearchArea, matching, computeMargin(source)); }
/*     */ 
/*     */ 
/*     */   
/* 161 */   public static float computeMargin(Entity source) { return Math.max(0.0F, Math.min(0.3F, (source.tickCount - 2) / 20.0F)); }
/*     */ 
/*     */   
/*     */   public static EntityHitResult getEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, float entityMargin) {
/* 165 */     double nearest = Double.MAX_VALUE;
/* 166 */     Optional<Vec3> nearestLocation = Optional.empty();
/* 167 */     Entity hitEntity = null;
/*     */     
/* 169 */     for (Entity entity : level.getEntities(source, targetSearchArea, matching)) {
/* 170 */       AABB bb = entity.getBoundingBox().inflate(entityMargin);
/* 171 */       Optional<Vec3> location = bb.clip(from, to);
/* 172 */       if (location.isPresent()) {
/* 173 */         double dd = from.distanceToSqr((Vec3)location.get());
/* 174 */         if (dd < nearest) {
/* 175 */           hitEntity = entity;
/* 176 */           nearest = dd;
/* 177 */           nearestLocation = location;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     if (hitEntity == null) {
/* 183 */       return null;
/*     */     }
/* 185 */     return new EntityHitResult(hitEntity, (Vec3)nearestLocation.get());
/*     */   }
/*     */ 
/*     */   
/* 189 */   public static Collection<EntityHitResult> getManyEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, boolean includeFromEntity) { return getManyEntityHitResult(level, source, from, to, targetSearchArea, matching, computeMargin(source), ClipContext.Block.COLLIDER, includeFromEntity); }
/*     */ 
/*     */   
/*     */   public static Collection<EntityHitResult> getManyEntityHitResult(Level level, Entity source, Vec3 from, Vec3 to, AABB targetSearchArea, Predicate<Entity> matching, float entityMargin, ClipContext.Block clipType, boolean includeFromEntity) {
/* 193 */     List<EntityHitResult> collector = new ArrayList<EntityHitResult>();
/*     */     
/* 195 */     for (Entity entity : level.getEntities(source, targetSearchArea, matching)) {
/* 196 */       AABB entityBB = entity.getBoundingBox();
/*     */ 
/*     */       
/* 199 */       if (includeFromEntity && entityBB.contains(from)) {
/* 200 */         collector.add(new EntityHitResult(entity, from));
/*     */         continue;
/*     */       } 
/* 203 */       Optional<Vec3> exactHit = entityBB.clip(from, to);
/* 204 */       if (exactHit.isPresent()) {
/* 205 */         collector.add(new EntityHitResult(entity, (Vec3)exactHit.get()));
/*     */         continue;
/*     */       } 
/* 208 */       if (entityMargin <= 0.0D) {
/*     */         continue;
/*     */       }
/* 211 */       Optional<Vec3> outsideHit = entityBB.inflate(entityMargin).clip(from, to);
/* 212 */       if (outsideHit.isEmpty()) {
/*     */         continue;
/*     */       }
/* 215 */       Vec3 outsideHitPosition = (Vec3)outsideHit.get();
/* 216 */       Vec3 towardsTarget = entityBB.getCenter();
/* 217 */       BlockHitResult hitResult = level.clipIncludingBorder(new ClipContext(outsideHitPosition, towardsTarget, clipType, ClipContext.Fluid.NONE, source));
/*     */       
/* 219 */       if (hitResult.getType() != HitResult.Type.MISS) {
/* 220 */         towardsTarget = hitResult.getLocation();
/*     */       }
/* 222 */       Optional<Vec3> surfaceHit = entity.getBoundingBox().clip(outsideHitPosition, towardsTarget);
/* 223 */       if (surfaceHit.isPresent()) {
/* 224 */         collector.add(new EntityHitResult(entity, (Vec3)surfaceHit.get()));
/*     */       }
/*     */     } 
/*     */     
/* 228 */     return collector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rotateTowardsMovement(Entity projectile, float rotationSpeed) {
/* 235 */     Vec3 movement = projectile.getDeltaMovement();
/*     */     
/* 237 */     if (movement.lengthSqr() == 0.0D) {
/*     */       return;
/*     */     }
/*     */     
/* 241 */     double sd = movement.horizontalDistance();
/* 242 */     projectile.setYRot((float)(Mth.atan2(movement.z, movement.x) * 57.2957763671875D) + 90.0F);
/* 243 */     projectile.setXRot((float)(Mth.atan2(sd, movement.y) * 57.2957763671875D) - 90.0F);
/*     */     
/* 245 */     while (projectile.getXRot() - projectile.xRotO < -180.0F) {
/* 246 */       projectile.xRotO -= 360.0F;
/*     */     }
/* 248 */     while (projectile.getXRot() - projectile.xRotO >= 180.0F) {
/* 249 */       projectile.xRotO += 360.0F;
/*     */     }
/*     */     
/* 252 */     while (projectile.getYRot() - projectile.yRotO < -180.0F) {
/* 253 */       projectile.yRotO -= 360.0F;
/*     */     }
/* 255 */     while (projectile.getYRot() - projectile.yRotO >= 180.0F) {
/* 256 */       projectile.yRotO += 360.0F;
/*     */     }
/*     */     
/* 259 */     projectile.setXRot(Mth.lerp(rotationSpeed, projectile.xRotO, projectile.getXRot()));
/* 260 */     projectile.setYRot(Mth.lerp(rotationSpeed, projectile.yRotO, projectile.getYRot()));
/*     */   }
/*     */ 
/*     */   
/* 264 */   public static InteractionHand getWeaponHoldingHand(LivingEntity mob, Item weaponItem) { return mob.getMainHandItem().is(weaponItem) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND; }
/*     */ 
/*     */   
/*     */   public static AbstractArrow getMobArrow(LivingEntity mob, ItemStack projectile, float power, ItemStack firedFromWeapon) {
/* 268 */     ArrowItem arrowItem = (ArrowItem)((projectile.getItem() instanceof ArrowItem) ? projectile.getItem() : Items.ARROW);
/* 269 */     AbstractArrow arrow = arrowItem.createArrow(mob.level(), projectile, mob, firedFromWeapon);
/* 270 */     arrow.setBaseDamageFromMob(power);
/*     */     
/* 272 */     return arrow;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\ProjectileUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */