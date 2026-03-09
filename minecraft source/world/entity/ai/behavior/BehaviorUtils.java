/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BehaviorUtils
/*     */ {
/*     */   public static void lockGazeAndWalkToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
/*  37 */     lookAtEachOther(entity1, entity2);
/*  38 */     setWalkAndLookTargetMemoriesToEachOther(entity1, entity2, speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   public static boolean entityIsVisible(Brain<?> brain, LivingEntity targetEntity) {
/*  42 */     Optional<NearestVisibleLivingEntities> visibleEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*  43 */     return (visibleEntities.isPresent() && ((NearestVisibleLivingEntities)visibleEntities.get()).contains(targetEntity));
/*     */   }
/*     */ 
/*     */   
/*  47 */   public static boolean targetIsValid(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memory, EntityType<?> targetType) { return targetIsValid(brain, memory, entity -> (entity.getType() == targetType)); }
/*     */ 
/*     */ 
/*     */   
/*  51 */   private static boolean targetIsValid(Brain<?> brain, MemoryModuleType<? extends LivingEntity> memory, Predicate<LivingEntity> targetPredicate) { return brain.getMemory(memory)
/*  52 */       .filter(targetPredicate)
/*  53 */       .filter(LivingEntity::isAlive)
/*  54 */       .filter(entity -> entityIsVisible(brain, entity))
/*  55 */       .isPresent(); }
/*     */ 
/*     */   
/*     */   private static void lookAtEachOther(LivingEntity entity1, LivingEntity entity2) {
/*  59 */     lookAtEntity(entity1, entity2);
/*  60 */     lookAtEntity(entity2, entity1);
/*     */   }
/*     */ 
/*     */   
/*  64 */   public static void lookAtEntity(LivingEntity looker, LivingEntity targetEntity) { looker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(targetEntity, true)); }
/*     */ 
/*     */   
/*     */   private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
/*  68 */     setWalkAndLookTargetMemories(entity1, entity2, speedModifier, closeEnoughDistance);
/*  69 */     setWalkAndLookTargetMemories(entity2, entity1, speedModifier, closeEnoughDistance);
/*     */   }
/*     */ 
/*     */   
/*  73 */   public static void setWalkAndLookTargetMemories(LivingEntity walker, Entity targetEntity, float speedModifier, int closeEnoughDistance) { setWalkAndLookTargetMemories(walker, new EntityTracker(targetEntity, true), speedModifier, closeEnoughDistance); }
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static void setWalkAndLookTargetMemories(LivingEntity walker, BlockPos targetPos, float speedModifier, int closeEnoughDistance) { setWalkAndLookTargetMemories(walker, new BlockPosTracker(targetPos), speedModifier, closeEnoughDistance); }
/*     */ 
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity walker, PositionTracker target, float speedModifier, int closeEnoughDistance) {
/*  81 */     WalkTarget walkTarget = new WalkTarget(target, speedModifier, closeEnoughDistance);
/*  82 */     walker.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, target);
/*  83 */     walker.getBrain().setMemory(MemoryModuleType.WALK_TARGET, walkTarget);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity thrower, ItemStack item, Vec3 targetPos) {
/*  87 */     Vec3 throwVelocity = new Vec3(0.30000001192092896D, 0.30000001192092896D, 0.30000001192092896D);
/*  88 */     throwItem(thrower, item, targetPos, throwVelocity, 0.3F);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void throwItem(LivingEntity thrower, ItemStack item, Vec3 targetPos, Vec3 throwVelocity, float handYDistanceFromEye) {
/*  93 */     double yHandPos = thrower.getEyeY() - handYDistanceFromEye;
/*  94 */     ItemEntity itemEntity = new ItemEntity(thrower.level(), thrower.getX(), yHandPos, thrower.getZ(), item);
/*  95 */     itemEntity.setThrower(thrower);
/*     */     
/*  97 */     Vec3 throwVector = targetPos.subtract(thrower.position());
/*  98 */     throwVector = throwVector.normalize().multiply(throwVelocity.x, throwVelocity.y, throwVelocity.z);
/*     */     
/* 100 */     itemEntity.setDeltaMovement(throwVector);
/* 101 */     itemEntity.setDefaultPickUpDelay();
/* 102 */     thrower.level().addFreshEntity(itemEntity);
/*     */   }
/*     */   
/*     */   public static SectionPos findSectionClosestToVillage(ServerLevel level, SectionPos center, int radius) {
/* 106 */     int distToVillage = level.sectionsToVillage(center);
/*     */ 
/*     */ 
/*     */     
/* 110 */     Objects.requireNonNull(level); return (SectionPos)SectionPos.cube(center, radius).filter(s -> (level.sectionsToVillage(s) < distToVillage)).min(Comparator.comparingInt(level::sectionsToVillage))
/* 111 */       .orElse(center);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWithinAttackRange(Mob body, LivingEntity target, int projectileAttackRangeMargin) {
/* 118 */     Item item = body.getMainHandItem().getItem(); if (item instanceof ProjectileWeaponItem) { ProjectileWeaponItem weapon = (ProjectileWeaponItem)item; if (body.canUseNonMeleeWeapon(body.getMainHandItem())) {
/* 119 */         int maxAllowedDistance = weapon.getDefaultProjectileRange() - projectileAttackRangeMargin;
/* 120 */         return body.closerThan(target, maxAllowedDistance);
/*     */       }  }
/* 122 */      return body.isWithinMeleeAttackRange(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(LivingEntity body, LivingEntity otherTarget, double howMuchFurtherAway) {
/* 130 */     Optional<LivingEntity> currentTarget = body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 131 */     if (currentTarget.isEmpty()) {
/* 132 */       return false;
/*     */     }
/* 134 */     double distSqrToCurrentTarget = body.distanceToSqr(((LivingEntity)currentTarget.get()).position());
/* 135 */     double distSqrToOtherTarget = body.distanceToSqr(otherTarget.position());
/* 136 */     return (distSqrToOtherTarget > distSqrToCurrentTarget + howMuchFurtherAway * howMuchFurtherAway);
/*     */   }
/*     */   
/*     */   public static boolean canSee(LivingEntity body, LivingEntity target) {
/* 140 */     Brain<?> brain = body.getBrain();
/* 141 */     if (!brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)) {
/* 142 */       return false;
/*     */     }
/* 144 */     return ((NearestVisibleLivingEntities)brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).get()).contains(target);
/*     */   }
/*     */   
/*     */   public static LivingEntity getNearestTarget(LivingEntity body, Optional<LivingEntity> target1, LivingEntity target2) {
/* 148 */     if (target1.isEmpty()) {
/* 149 */       return target2;
/*     */     }
/* 151 */     return getTargetNearestMe(body, (LivingEntity)target1.get(), target2);
/*     */   }
/*     */   
/*     */   public static LivingEntity getTargetNearestMe(LivingEntity body, LivingEntity target1, LivingEntity target2) {
/* 155 */     Vec3 pos1 = target1.position();
/* 156 */     Vec3 pos2 = target2.position();
/* 157 */     return (body.distanceToSqr(pos1) < body.distanceToSqr(pos2)) ? target1 : target2;
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(LivingEntity body, MemoryModuleType<UUID> memoryType) {
/* 161 */     Optional<UUID> uuidMemory = body.getBrain().getMemory(memoryType);
/*     */     
/* 163 */     return uuidMemory.map(uuid -> body.level().getEntity(uuid)).map(entity -> { LivingEntity livingEntity = (LivingEntity)entity; return (entity instanceof LivingEntity) ? livingEntity : null;
/*     */         });
/*     */   }
/*     */   public static Vec3 getRandomSwimmablePos(PathfinderMob body, int maxHorizontalDistance, int maxVerticalDistance) {
/* 167 */     Vec3 targetPos = DefaultRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance);
/* 168 */     int count = 0;
/* 169 */     while (targetPos != null && !body.level().getBlockState(BlockPos.containing(targetPos)).isPathfindable(PathComputationType.WATER) && count++ < 10) {
/* 170 */       targetPos = DefaultRandomPos.getPos(body, maxHorizontalDistance, maxVerticalDistance);
/*     */     }
/* 172 */     return targetPos;
/*     */   }
/*     */ 
/*     */   
/* 176 */   public static boolean isBreeding(LivingEntity body) { return body.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BehaviorUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */