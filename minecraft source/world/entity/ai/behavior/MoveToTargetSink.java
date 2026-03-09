/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MoveToTargetSink
/*     */   extends Behavior<Mob>
/*     */ {
/*     */   private static final int MAX_COOLDOWN_BEFORE_RETRYING = 40;
/*     */   private int remainingCooldown;
/*     */   private Path path;
/*     */   private BlockPos lastTargetPos;
/*     */   private float speedModifier;
/*     */   
/*  38 */   public MoveToTargetSink() { this(150, 250); }
/*     */ 
/*     */   
/*     */   public MoveToTargetSink(int minTimeout, int maxTimeout) {
/*  42 */     super(
/*  43 */         ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT), minTimeout, maxTimeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  54 */     if (this.remainingCooldown > 0) {
/*  55 */       this.remainingCooldown--;
/*  56 */       return false;
/*     */     } 
/*     */     
/*  59 */     Brain<?> brain = body.getBrain();
/*  60 */     WalkTarget walkTarget = (WalkTarget)brain.getMemory(MemoryModuleType.WALK_TARGET).get();
/*     */     
/*  62 */     boolean reachedTarget = reachedTarget(body, walkTarget);
/*  63 */     if (!reachedTarget && tryComputePath(body, walkTarget, level.getGameTime())) {
/*  64 */       this.lastTargetPos = walkTarget.getTarget().currentBlockPosition();
/*  65 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  70 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/*  71 */     if (reachedTarget) {
/*  72 */       brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     }
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  79 */     if (this.path == null || this.lastTargetPos == null) {
/*  80 */       return false;
/*     */     }
/*     */     
/*  83 */     Optional<WalkTarget> walkTarget = body.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
/*  84 */     boolean isSpectator = ((Boolean)walkTarget.map(MoveToTargetSink::isWalkTargetSpectator).orElse(Boolean.valueOf(false))).booleanValue();
/*     */     
/*  86 */     PathNavigation navigation = body.getNavigation();
/*  87 */     return (!navigation.isDone() && walkTarget.isPresent() && !reachedTarget(body, (WalkTarget)walkTarget.get()) && !isSpectator);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/*  92 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !reachedTarget(body, (WalkTarget)body.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get()) && body.getNavigation().isStuck())
/*     */     {
/*  94 */       this.remainingCooldown = level.getRandom().nextInt(40);
/*     */     }
/*     */     
/*  97 */     body.getNavigation().stop();
/*  98 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  99 */     body.getBrain().eraseMemory(MemoryModuleType.PATH);
/* 100 */     this.path = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 105 */     body.getBrain().setMemory(MemoryModuleType.PATH, this.path);
/* 106 */     body.getNavigation().moveTo(this.path, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 111 */     Path newPath = body.getNavigation().getPath();
/* 112 */     Brain<?> brain = body.getBrain();
/* 113 */     if (this.path != newPath) {
/* 114 */       this.path = newPath;
/* 115 */       brain.setMemory(MemoryModuleType.PATH, newPath);
/*     */     } 
/*     */     
/* 118 */     if (newPath == null || this.lastTargetPos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     WalkTarget walkTarget = (WalkTarget)brain.getMemory(MemoryModuleType.WALK_TARGET).get();
/*     */     
/* 124 */     if (walkTarget.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4.0D && 
/* 125 */       tryComputePath(body, walkTarget, level.getGameTime())) {
/* 126 */       this.lastTargetPos = walkTarget.getTarget().currentBlockPosition();
/* 127 */       start(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean tryComputePath(Mob body, WalkTarget walkTarget, long timestamp) {
/* 134 */     BlockPos targetPos = walkTarget.getTarget().currentBlockPosition();
/* 135 */     this.path = body.getNavigation().createPath(targetPos, 0);
/* 136 */     this.speedModifier = walkTarget.getSpeedModifier();
/*     */     
/* 138 */     Brain<?> brain = body.getBrain();
/* 139 */     if (reachedTarget(body, walkTarget)) {
/* 140 */       brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     } else {
/* 142 */       boolean canReach = (this.path != null && this.path.canReach());
/* 143 */       if (canReach) {
/* 144 */         brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 145 */       } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
/* 146 */         brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, Long.valueOf(timestamp));
/*     */       } 
/*     */       
/* 149 */       if (this.path != null) {
/* 150 */         return true;
/*     */       }
/*     */       
/* 153 */       Vec3 partialStep = DefaultRandomPos.getPosTowards((PathfinderMob)body, 10, 7, Vec3.atBottomCenterOf(targetPos), 1.5707963705062866D);
/* 154 */       if (partialStep != null) {
/* 155 */         this.path = body.getNavigation().createPath(partialStep.x, partialStep.y, partialStep.z, 0);
/* 156 */         return (this.path != null);
/*     */       } 
/*     */     } 
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 163 */   private boolean reachedTarget(Mob body, WalkTarget walkTarget) { return (walkTarget.getTarget().currentBlockPosition().distManhattan(body.blockPosition()) <= walkTarget.getCloseEnoughDist()); }
/*     */ 
/*     */   
/*     */   private static boolean isWalkTargetSpectator(WalkTarget walkTarget) {
/* 167 */     PositionTracker target = walkTarget.getTarget();
/*     */     
/* 169 */     if (target instanceof EntityTracker) { EntityTracker entityTracker = (EntityTracker)target;
/* 170 */       return entityTracker.getEntity().isSpectator(); }
/*     */     
/* 172 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\MoveToTargetSink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */