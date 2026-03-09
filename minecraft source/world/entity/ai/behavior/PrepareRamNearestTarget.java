/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class PrepareRamNearestTarget<E extends PathfinderMob>
/*     */   extends Behavior<E>
/*     */ {
/*     */   public static final int TIME_OUT_DURATION = 160;
/*     */   private final ToIntFunction<E> getCooldownOnFail;
/*     */   private final int minRamDistance;
/*     */   private final int maxRamDistance;
/*     */   private final float walkSpeed;
/*     */   private final TargetingConditions ramTargeting;
/*     */   private final int ramPrepareTime;
/*     */   private final Function<E, SoundEvent> getPrepareRamSound;
/*  42 */   private Optional<Long> reachedRamPositionTimestamp = Optional.empty();
/*  43 */   private Optional<RamCandidate> ramCandidate = Optional.empty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrepareRamNearestTarget(ToIntFunction<E> getCooldownOnFail, int minRamDistance, int maxRamDistance, float walkSpeed, TargetingConditions ramTargeting, int ramPrepareTime, Function<E, SoundEvent> getPrepareRamSound) {
/*  54 */     super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_ABSENT), 160);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  61 */     this.getCooldownOnFail = getCooldownOnFail;
/*  62 */     this.minRamDistance = minRamDistance;
/*  63 */     this.maxRamDistance = maxRamDistance;
/*  64 */     this.walkSpeed = walkSpeed;
/*  65 */     this.ramTargeting = ramTargeting;
/*  66 */     this.ramPrepareTime = ramPrepareTime;
/*  67 */     this.getPrepareRamSound = getPrepareRamSound;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/*  72 */     Brain<?> brain = body.getBrain();
/*  73 */     brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
/*  74 */       .flatMap(livingEntities -> livingEntities.findClosest(()))
/*  75 */       .ifPresent(livingEntity -> chooseRamPosition(body, livingEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {
/*  80 */     Brain<?> brain = body.getBrain();
/*  81 */     if (!brain.hasMemoryValue(MemoryModuleType.RAM_TARGET)) {
/*  82 */       level.broadcastEntityEvent(body, (byte)59);
/*  83 */       brain.setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, Integer.valueOf(this.getCooldownOnFail.applyAsInt(body)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/*  89 */     return (this.ramCandidate.isPresent() && ((RamCandidate)this.ramCandidate
/*  90 */       .get()).getTarget().isAlive());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {
/*  95 */     if (this.ramCandidate.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  99 */     body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(((RamCandidate)this.ramCandidate.get()).getStartPosition(), this.walkSpeed, 0));
/* 100 */     body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(((RamCandidate)this.ramCandidate.get()).getTarget(), true));
/*     */     
/* 102 */     boolean didTargetMove = !((RamCandidate)this.ramCandidate.get()).getTarget().blockPosition().equals(((RamCandidate)this.ramCandidate.get()).getTargetPosition());
/* 103 */     if (didTargetMove) {
/* 104 */       level.broadcastEntityEvent(body, (byte)59);
/* 105 */       body.getNavigation().stop();
/* 106 */       chooseRamPosition(body, ((RamCandidate)this.ramCandidate.get()).target);
/*     */     } else {
/* 108 */       BlockPos startRamPos = body.blockPosition();
/* 109 */       if (startRamPos.equals(((RamCandidate)this.ramCandidate.get()).getStartPosition())) {
/* 110 */         level.broadcastEntityEvent(body, (byte)58);
/* 111 */         if (this.reachedRamPositionTimestamp.isEmpty()) {
/* 112 */           this.reachedRamPositionTimestamp = Optional.of(Long.valueOf(timestamp));
/*     */         }
/* 114 */         if (timestamp - ((Long)this.reachedRamPositionTimestamp.get()).longValue() >= this.ramPrepareTime) {
/* 115 */           body.getBrain().setMemory(MemoryModuleType.RAM_TARGET, getEdgeOfBlock(startRamPos, ((RamCandidate)this.ramCandidate.get()).getTargetPosition()));
/* 116 */           level.playSound(null, body, (SoundEvent)this.getPrepareRamSound.apply(body), SoundSource.NEUTRAL, 1.0F, body.getVoicePitch());
/* 117 */           this.ramCandidate = Optional.empty();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Vec3 getEdgeOfBlock(BlockPos startRamPos, BlockPos targetPos) {
/* 124 */     double offsetDistance = 0.5D;
/* 125 */     double xOffset = 0.5D * Mth.sign((targetPos.getX() - startRamPos.getX()));
/* 126 */     double zOffset = 0.5D * Mth.sign((targetPos.getZ() - startRamPos.getZ()));
/*     */     
/* 128 */     return Vec3.atBottomCenterOf(targetPos).add(xOffset, 0.0D, zOffset);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> calculateRammingStartPosition(PathfinderMob body, LivingEntity ramableTarget) {
/* 132 */     BlockPos targetPos = ramableTarget.blockPosition();
/* 133 */     if (!isWalkableBlock(body, targetPos)) {
/* 134 */       return Optional.empty();
/*     */     }
/*     */     
/* 137 */     List<BlockPos> possibleRamPositions = Lists.newArrayList();
/*     */     
/* 139 */     BlockPos.MutableBlockPos walkablePosFurthestAwayFromTarget = targetPos.mutable();
/* 140 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 141 */       walkablePosFurthestAwayFromTarget.set(targetPos);
/* 142 */       for (int distance = 0; distance < this.maxRamDistance; distance++) {
/* 143 */         if (!isWalkableBlock(body, walkablePosFurthestAwayFromTarget.move(direction))) {
/* 144 */           walkablePosFurthestAwayFromTarget.move(direction.getOpposite());
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 149 */       if (walkablePosFurthestAwayFromTarget.distManhattan(targetPos) >= this.minRamDistance) {
/* 150 */         possibleRamPositions.add(walkablePosFurthestAwayFromTarget.immutable());
/*     */       }
/*     */     } 
/*     */     
/* 154 */     PathNavigation navigation = body.getNavigation();
/*     */ 
/*     */     
/* 157 */     Objects.requireNonNull(body.blockPosition()); return possibleRamPositions.stream().sorted(Comparator.comparingDouble(body.blockPosition()::distSqr))
/* 158 */       .filter(pos -> {
/* 159 */           Path path = navigation.createPath(pos, 0);
/* 160 */           return (path != null && path.canReach());
/*     */         
/* 162 */         }).findFirst();
/*     */   }
/*     */   
/*     */   private boolean isWalkableBlock(PathfinderMob body, BlockPos targetPos) {
/* 166 */     return (body.getNavigation().isStableDestination(targetPos) && body
/* 167 */       .getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(body, targetPos)) == 0.0F);
/*     */   }
/*     */   
/*     */   private void chooseRamPosition(PathfinderMob body, LivingEntity ramableTarget) {
/* 171 */     this.reachedRamPositionTimestamp = Optional.empty();
/* 172 */     this
/* 173 */       .ramCandidate = calculateRammingStartPosition(body, ramableTarget).map(pos -> new RamCandidate(pos, ramableTarget.blockPosition(), ramableTarget));
/*     */   }
/*     */   
/*     */   public static class RamCandidate {
/*     */     private final BlockPos startPosition;
/*     */     private final BlockPos targetPosition;
/*     */     private final LivingEntity target;
/*     */     
/*     */     public RamCandidate(BlockPos startPosition, BlockPos targetPosition, LivingEntity target) {
/* 182 */       this.startPosition = startPosition;
/* 183 */       this.targetPosition = targetPosition;
/* 184 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/* 188 */     public BlockPos getStartPosition() { return this.startPosition; }
/*     */ 
/*     */ 
/*     */     
/* 192 */     public BlockPos getTargetPosition() { return this.targetPosition; }
/*     */ 
/*     */ 
/*     */     
/* 196 */     public LivingEntity getTarget() { return this.target; }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PrepareRamNearestTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */