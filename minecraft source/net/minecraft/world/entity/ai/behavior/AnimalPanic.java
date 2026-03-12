/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class AnimalPanic<E extends PathfinderMob>
/*     */   extends Behavior<E>
/*     */ {
/*     */   private static final int PANIC_MIN_DURATION = 100;
/*     */   private static final int PANIC_MAX_DURATION = 120;
/*     */   private static final int PANIC_DISTANCE_HORIZONTAL = 5;
/*     */   private static final int PANIC_DISTANCE_VERTICAL = 4;
/*     */   private final float speedMultiplier;
/*     */   private final Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes;
/*     */   private final Function<E, Vec3> positionGetter;
/*     */   
/*  39 */   public AnimalPanic(float speedMultiplier) { this(speedMultiplier, mob -> DamageTypeTags.PANIC_CAUSES, mob -> LandRandomPos.getPos(mob, 5, 4)); }
/*     */ 
/*     */ 
/*     */   
/*  43 */   public AnimalPanic(float speedMultiplier, int flyHeight) { this(speedMultiplier, mob -> DamageTypeTags.PANIC_CAUSES, mob -> AirAndWaterRandomPos.getPos(mob, 5, 4, flyHeight, (mob.getViewVector(0.0F)).x, (mob.getViewVector(0.0F)).z, 1.5707963705062866D)); }
/*     */ 
/*     */ 
/*     */   
/*  47 */   public AnimalPanic(float speedMultiplier, Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes) { this(speedMultiplier, panicCausingDamageTypes, mob -> LandRandomPos.getPos(mob, 5, 4)); }
/*     */ 
/*     */   
/*     */   public AnimalPanic(float speedMultiplier, Function<PathfinderMob, TagKey<DamageType>> panicCausingDamageTypes, Function<E, Vec3> positionGetter) {
/*  51 */     super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.REGISTERED), 100, 120);
/*  52 */     this.speedMultiplier = speedMultiplier;
/*  53 */     this.panicCausingDamageTypes = panicCausingDamageTypes;
/*  54 */     this.positionGetter = positionGetter;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/*  59 */     return (((Boolean)body.getBrain().getMemory(MemoryModuleType.HURT_BY).map(d -> Boolean.valueOf(d.is((TagKey)this.panicCausingDamageTypes.apply(body)))).orElse(Boolean.valueOf(false))).booleanValue() || body
/*  60 */       .getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  65 */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {
/*  70 */     body.getBrain().setMemory(MemoryModuleType.IS_PANICKING, Boolean.valueOf(true));
/*  71 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  72 */     body.getNavigation().stop();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {
/*  77 */     Brain<?> brain = body.getBrain();
/*  78 */     brain.eraseMemory(MemoryModuleType.IS_PANICKING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/*  83 */     if (body.getNavigation().isDone()) {
/*  84 */       Vec3 panicToPos = getPanicPos(body, level);
/*  85 */       if (panicToPos != null) {
/*  86 */         body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(panicToPos, this.speedMultiplier, 0));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3 getPanicPos(E body, ServerLevel level) {
/*  92 */     if (body.isOnFire()) {
/*  93 */       Optional<Vec3> nearestWater = lookForWater(level, body).map(Vec3::atBottomCenterOf);
/*  94 */       if (nearestWater.isPresent()) {
/*  95 */         return (Vec3)nearestWater.get();
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return (Vec3)this.positionGetter.apply(body);
/*     */   }
/*     */   private Optional<BlockPos> lookForWater(BlockGetter level, Entity mob) {
/*     */     Predicate<BlockPos> posPredicate;
/* 103 */     BlockPos mobPosition = mob.blockPosition();
/* 104 */     if (!level.getBlockState(mobPosition).getCollisionShape(level, mobPosition).isEmpty()) {
/* 105 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 109 */     if (Mth.ceil(mob.getBbWidth()) == 2) {
/* 110 */       posPredicate = (from -> BlockPos.squareOutSouthEast(from).allMatch(()));
/*     */     } else {
/* 112 */       posPredicate = (pos -> level.getFluidState(pos).is(FluidTags.WATER));
/*     */     } 
/*     */     
/* 115 */     return BlockPos.findClosestMatch(mobPosition, 5, 1, posPredicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AnimalPanic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */