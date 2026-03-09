/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PathNavigation
/*     */ {
/*     */   private static final int MAX_TIME_RECOMPUTE = 20;
/*     */   private static final int STUCK_CHECK_INTERVAL = 100;
/*     */   private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;
/*     */   protected final Mob mob;
/*     */   protected final Level level;
/*     */   protected Path path;
/*     */   protected double speedModifier;
/*     */   protected int tick;
/*     */   protected int lastStuckCheck;
/*     */   protected Vec3 lastStuckCheckPos;
/*     */   protected Vec3i timeoutCachedNode;
/*     */   protected long timeoutTimer;
/*     */   protected long lastTimeoutCheck;
/*     */   protected double timeoutLimit;
/*     */   protected float maxDistanceToWaypoint;
/*     */   protected boolean hasDelayedRecomputation;
/*     */   protected long timeLastRecompute;
/*     */   protected NodeEvaluator nodeEvaluator;
/*     */   private BlockPos targetPos;
/*     */   private int reachRange;
/*     */   private float maxVisitedNodesMultiplier;
/*     */   private final PathFinder pathFinder;
/*     */   private boolean isStuck;
/*     */   private float requiredPathLength;
/*     */   
/*     */   public PathNavigation(Mob mob, Level level) {
/*  62 */     this.lastStuckCheckPos = Vec3.ZERO;
/*  63 */     this.timeoutCachedNode = Vec3i.ZERO;
/*     */ 
/*     */ 
/*     */     
/*  67 */     this.maxDistanceToWaypoint = 0.5F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.maxVisitedNodesMultiplier = 1.0F;
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
/*  88 */     this.requiredPathLength = 16.0F;
/*     */ 
/*     */     
/*  91 */     this.mob = mob;
/*  92 */     this.level = level;
/*  93 */     this.pathFinder = createPathFinder(Mth.floor(mob.getAttributeBaseValue(Attributes.FOLLOW_RANGE) * 16.0D));
/*  94 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/*  95 */       ServerDebugSubscribers subscribers = serverLevel.getServer().debugSubscribers();
/*  96 */       this.pathFinder.setCaptureDebug(() -> subscribers.hasAnySubscriberFor(DebugSubscriptions.ENTITY_PATHS)); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePathfinderMaxVisitedNodes() {
/* 103 */     int maxVisitedNodes = Mth.floor(getMaxPathLength() * 16.0F);
/* 104 */     this.pathFinder.setMaxVisitedNodes(maxVisitedNodes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequiredPathLength(float length) {
/* 112 */     this.requiredPathLength = length;
/*     */     
/* 114 */     updatePathfinderMaxVisitedNodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   private float getMaxPathLength() { return Math.max((float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE), this.requiredPathLength); }
/*     */ 
/*     */ 
/*     */   
/* 125 */   public void resetMaxVisitedNodesMultiplier() { this.maxVisitedNodesMultiplier = 1.0F; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public void setMaxVisitedNodesMultiplier(float maxVisitedNodesMultiplier) { this.maxVisitedNodesMultiplier = maxVisitedNodesMultiplier; }
/*     */ 
/*     */ 
/*     */   
/* 136 */   public BlockPos getTargetPos() { return this.targetPos; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   public void setSpeedModifier(double speedModifier) { this.speedModifier = speedModifier; }
/*     */ 
/*     */   
/*     */   public void recomputePath() {
/* 146 */     if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
/* 147 */       if (this.targetPos != null) {
/* 148 */         this.path = null;
/* 149 */         this.path = createPath(this.targetPos, this.reachRange);
/* 150 */         this.timeLastRecompute = this.level.getGameTime();
/* 151 */         this.hasDelayedRecomputation = false;
/*     */       } 
/*     */     } else {
/* 154 */       this.hasDelayedRecomputation = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 159 */   public final Path createPath(double x, double y, double z, int reachRange) { return createPath(BlockPos.containing(x, y, z), reachRange); }
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
/* 171 */   public Path createPath(Stream<BlockPos> positions, int reachRange) { return createPath((Set)positions.collect(Collectors.toSet()), 8, false, reachRange); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public Path createPath(Set<BlockPos> positions, int reachRange) { return createPath(positions, 8, false, reachRange); }
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
/* 187 */   public Path createPath(BlockPos pos, int reachRange) { return createPath(ImmutableSet.of(pos), 8, false, reachRange); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 193 */   public Path createPath(BlockPos pos, int reachRange, int maxPathLength) { return createPath(ImmutableSet.of(pos), 8, false, reachRange, maxPathLength); }
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
/* 205 */   public Path createPath(Entity target, int reachRange) { return createPath(ImmutableSet.of(target.blockPosition()), 16, true, reachRange); }
/*     */ 
/*     */ 
/*     */   
/* 209 */   protected Path createPath(Set<BlockPos> targets, int radiusOffset, boolean above, int reachRange) { return createPath(targets, radiusOffset, above, reachRange, getMaxPathLength()); }
/*     */ 
/*     */   
/*     */   protected Path createPath(Set<BlockPos> targets, int radiusOffset, boolean above, int reachRange, float maxPathLength) {
/* 213 */     if (targets.isEmpty()) {
/* 214 */       return null;
/*     */     }
/*     */     
/* 217 */     if (this.mob.getY() < this.level.getMinY()) {
/* 218 */       return null;
/*     */     }
/*     */     
/* 221 */     if (!canUpdatePath()) {
/* 222 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 226 */     if (this.path != null && !this.path.isDone() && targets.contains(this.targetPos)) {
/* 227 */       return this.path;
/*     */     }
/*     */     
/* 230 */     ProfilerFiller profiler = Profiler.get();
/* 231 */     profiler.push("pathfind");
/* 232 */     BlockPos fromPos = above ? this.mob.blockPosition().above() : this.mob.blockPosition();
/* 233 */     int radius = (int)(maxPathLength + radiusOffset);
/*     */ 
/*     */     
/* 236 */     PathNavigationRegion region = new PathNavigationRegion(this.level, fromPos.offset(-radius, -radius, -radius), fromPos.offset(radius, radius, radius));
/* 237 */     Path path = this.pathFinder.findPath(region, this.mob, targets, maxPathLength, reachRange, this.maxVisitedNodesMultiplier);
/* 238 */     profiler.pop();
/*     */     
/* 240 */     if (path != null && path.getTarget() != null) {
/*     */ 
/*     */ 
/*     */       
/* 244 */       this.targetPos = path.getTarget();
/* 245 */       this.reachRange = reachRange;
/* 246 */       resetStuckTimeout();
/*     */     } 
/*     */     
/* 249 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 258 */   public boolean moveTo(double x, double y, double z, double speedModifier) { return moveTo(createPath(x, y, z, 1), speedModifier); }
/*     */ 
/*     */ 
/*     */   
/* 262 */   public boolean moveTo(double x, double y, double z, int reachRange, double speedModifier) { return moveTo(createPath(x, y, z, reachRange), speedModifier); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(Entity target, double speedModifier) {
/* 271 */     Path newPath = createPath(target, 1);
/* 272 */     return (newPath != null && moveTo(newPath, speedModifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(Path newPath, double speedModifier) {
/* 280 */     if (newPath == null) {
/* 281 */       this.path = null;
/* 282 */       return false;
/*     */     } 
/* 284 */     if (!newPath.sameAs(this.path)) {
/* 285 */       this.path = newPath;
/*     */     }
/* 287 */     if (isDone()) {
/* 288 */       return false;
/*     */     }
/* 290 */     trimPath();
/* 291 */     if (this.path.getNodeCount() <= 0) {
/* 292 */       return false;
/*     */     }
/*     */     
/* 295 */     this.speedModifier = speedModifier;
/* 296 */     Vec3 mobPos = getTempMobPos();
/* 297 */     this.lastStuckCheck = this.tick;
/* 298 */     this.lastStuckCheckPos = mobPos;
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 303 */   public Path getPath() { return this.path; }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 307 */     this.tick++;
/*     */     
/* 309 */     if (this.hasDelayedRecomputation) {
/* 310 */       recomputePath();
/*     */     }
/*     */     
/* 313 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 317 */     if (canUpdatePath()) {
/* 318 */       followThePath();
/* 319 */     } else if (this.path != null && !this.path.isDone()) {
/* 320 */       Vec3 mobPos = getTempMobPos();
/* 321 */       Vec3 pos = this.path.getNextEntityPos(this.mob);
/* 322 */       if (mobPos.y > pos.y && !this.mob.onGround() && Mth.floor(mobPos.x) == Mth.floor(pos.x) && Mth.floor(mobPos.z) == Mth.floor(pos.z)) {
/* 323 */         this.path.advance();
/*     */       }
/*     */     } 
/*     */     
/* 327 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 332 */     Vec3 target = this.path.getNextEntityPos(this.mob);
/* 333 */     this.mob.getMoveControl().setWantedPosition(target.x, getGroundY(target), target.z, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getGroundY(Vec3 target) {
/* 338 */     BlockPos blockPos = BlockPos.containing(target);
/* 339 */     return this.level.getBlockState(blockPos.below()).isAir() ? target.y : WalkNodeEvaluator.getFloorLevel(this.level, blockPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void followThePath() {
/* 346 */     Vec3 mobPos = getTempMobPos();
/*     */     
/* 348 */     this.maxDistanceToWaypoint = (this.mob.getBbWidth() > 0.75F) ? (this.mob.getBbWidth() / 2.0F) : (0.75F - this.mob.getBbWidth() / 2.0F);
/* 349 */     BlockPos blockPos = this.path.getNextNodePos();
/* 350 */     double xDistance = Math.abs(this.mob.getX() - blockPos.getX() + 0.5D);
/* 351 */     double yDistance = Math.abs(this.mob.getY() - blockPos.getY());
/* 352 */     double zDistance = Math.abs(this.mob.getZ() - blockPos.getZ() + 0.5D);
/* 353 */     boolean isCloseEnoughToCurrentNode = (xDistance < this.maxDistanceToWaypoint && zDistance < this.maxDistanceToWaypoint && yDistance < 1.0D);
/*     */ 
/*     */ 
/*     */     
/* 357 */     if (isCloseEnoughToCurrentNode || (canCutCorner((this.path.getNextNode()).type) && shouldTargetNextNodeInDirection(mobPos))) {
/* 358 */       this.path.advance();
/*     */     }
/* 360 */     doStuckDetection(mobPos);
/*     */   }
/*     */   
/*     */   private boolean shouldTargetNextNodeInDirection(Vec3 mobPosition) {
/* 364 */     if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
/* 365 */       return false;
/*     */     }
/*     */     
/* 368 */     Vec3 currentNode = Vec3.atBottomCenterOf(this.path.getNextNodePos());
/* 369 */     if (!mobPosition.closerThan(currentNode, 2.0D))
/*     */     {
/*     */       
/* 372 */       return false;
/*     */     }
/*     */     
/* 375 */     if (canMoveDirectly(mobPosition, this.path.getNextEntityPos(this.mob))) {
/* 376 */       return true;
/*     */     }
/*     */     
/* 379 */     Vec3 nextNode = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
/*     */ 
/*     */     
/* 382 */     Vec3 mobToCurrent = currentNode.subtract(mobPosition);
/* 383 */     Vec3 mobToNext = nextNode.subtract(mobPosition);
/* 384 */     double mobToCurrentSqr = mobToCurrent.lengthSqr();
/* 385 */     double mobToNextSqr = mobToNext.lengthSqr();
/* 386 */     boolean closerToNextThanCurrent = (mobToNextSqr < mobToCurrentSqr);
/* 387 */     boolean withinCurrentBlock = (mobToCurrentSqr < 0.5D);
/* 388 */     if (closerToNextThanCurrent || withinCurrentBlock) {
/* 389 */       Vec3 mobDirection = mobToCurrent.normalize();
/* 390 */       Vec3 pathDirection = mobToNext.normalize();
/* 391 */       return (pathDirection.dot(mobDirection) < 0.0D);
/*     */     } 
/*     */     
/* 394 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStuckDetection(Vec3 mobPos) {
/* 399 */     if (this.tick - this.lastStuckCheck > 100) {
/*     */       
/* 401 */       float effectiveSpeed = (this.mob.getSpeed() >= 1.0F) ? this.mob.getSpeed() : (this.mob.getSpeed() * this.mob.getSpeed());
/* 402 */       float thresholdDistance = effectiveSpeed * 100.0F * 0.25F;
/* 403 */       if (mobPos.distanceToSqr(this.lastStuckCheckPos) < (thresholdDistance * thresholdDistance)) {
/* 404 */         this.isStuck = true;
/* 405 */         stop();
/*     */       } else {
/* 407 */         this.isStuck = false;
/*     */       } 
/* 409 */       this.lastStuckCheck = this.tick;
/* 410 */       this.lastStuckCheckPos = mobPos;
/*     */     } 
/*     */     
/* 413 */     if (this.path != null && !this.path.isDone()) {
/* 414 */       BlockPos blockPos = this.path.getNextNodePos();
/*     */       
/* 416 */       long time = this.level.getGameTime();
/* 417 */       if (blockPos.equals(this.timeoutCachedNode)) {
/* 418 */         this.timeoutTimer += time - this.lastTimeoutCheck;
/*     */       } else {
/* 420 */         this.timeoutCachedNode = blockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 426 */         double distToNode = mobPos.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
/* 427 */         this.timeoutLimit = (this.mob.getSpeed() > 0.0F) ? (distToNode / this.mob.getSpeed() * 20.0D) : 0.0D;
/*     */       } 
/*     */       
/* 430 */       if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 3.0D) {
/* 431 */         timeoutPath();
/*     */       }
/* 433 */       this.lastTimeoutCheck = time;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void timeoutPath() {
/* 438 */     resetStuckTimeout();
/* 439 */     stop();
/*     */   }
/*     */   
/*     */   private void resetStuckTimeout() {
/* 443 */     this.timeoutCachedNode = Vec3i.ZERO;
/* 444 */     this.timeoutTimer = 0L;
/* 445 */     this.timeoutLimit = 0.0D;
/* 446 */     this.isStuck = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 451 */   public boolean isDone() { return (this.path == null || this.path.isDone()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 458 */   public boolean isInProgress() { return !isDone(); }
/*     */ 
/*     */ 
/*     */   
/* 462 */   public void stop() { this.path = null; }
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
/*     */   protected void trimPath() {
/* 474 */     if (this.path == null) {
/*     */       return;
/*     */     }
/*     */     
/* 478 */     for (int i = 0; i < this.path.getNodeCount(); i++) {
/* 479 */       Node node = this.path.getNode(i);
/* 480 */       Node nextNode = (i + 1 < this.path.getNodeCount()) ? this.path.getNode(i + 1) : null;
/*     */       
/* 482 */       BlockState state = this.level.getBlockState(new BlockPos(node.x, node.y, node.z));
/*     */       
/* 484 */       if (state.is(BlockTags.CAULDRONS)) {
/* 485 */         this.path.replaceNode(i, node.cloneAndMove(node.x, node.y + 1, node.z));
/* 486 */         if (nextNode != null && node.y >= nextNode.y) {
/* 487 */           this.path.replaceNode(i + 1, node.cloneAndMove(nextNode.x, node.y + 1, nextNode.z));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 494 */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 504 */   public boolean canCutCorner(PathType pathType) { return (pathType != PathType.DANGER_FIRE && pathType != PathType.DANGER_OTHER && pathType != PathType.WALKABLE_DOOR); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean isClearForMovementBetween(Mob mob, Vec3 startPos, Vec3 stopPos, boolean blockedByFluids) {
/* 510 */     Vec3 to = new Vec3(stopPos.x, stopPos.y + mob.getBbHeight() * 0.5D, stopPos.z);
/* 511 */     return (mob.level().clip(new ClipContext(startPos, to, ClipContext.Block.COLLIDER, blockedByFluids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, mob)).getType() == HitResult.Type.MISS);
/*     */   }
/*     */   
/*     */   public boolean isStableDestination(BlockPos pos) {
/* 515 */     BlockPos below = pos.below();
/* 516 */     return this.level.getBlockState(below).isSolidRender();
/*     */   }
/*     */ 
/*     */   
/* 520 */   public NodeEvaluator getNodeEvaluator() { return this.nodeEvaluator; }
/*     */ 
/*     */ 
/*     */   
/* 524 */   public void setCanFloat(boolean canFloat) { this.nodeEvaluator.setCanFloat(canFloat); }
/*     */ 
/*     */ 
/*     */   
/* 528 */   public boolean canFloat() { return this.nodeEvaluator.canFloat(); }
/*     */ 
/*     */   
/*     */   public boolean shouldRecomputePath(BlockPos pos) {
/* 532 */     if (this.hasDelayedRecomputation) {
/* 533 */       return false;
/*     */     }
/*     */     
/* 536 */     if (this.path == null || this.path.isDone() || this.path.getNodeCount() == 0) {
/* 537 */       return false;
/*     */     }
/*     */     
/* 540 */     Node target = this.path.getEndNode();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 545 */     Vec3 middlePos = new Vec3((target.x + this.mob.getX()) / 2.0D, (target.y + this.mob.getY()) / 2.0D, (target.z + this.mob.getZ()) / 2.0D);
/*     */ 
/*     */     
/* 548 */     return pos.closerToCenterThan(middlePos, (this.path.getNodeCount() - this.path.getNextNodeIndex()));
/*     */   }
/*     */ 
/*     */   
/* 552 */   public float getMaxDistanceToWaypoint() { return this.maxDistanceToWaypoint; }
/*     */ 
/*     */ 
/*     */   
/* 556 */   public boolean isStuck() { return this.isStuck; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 562 */   public void setCanOpenDoors(boolean canOpenDoors) { this.nodeEvaluator.setCanOpenDoors(canOpenDoors); }
/*     */   
/*     */   protected abstract PathFinder createPathFinder(int paramInt);
/*     */   
/*     */   protected abstract Vec3 getTempMobPos();
/*     */   
/*     */   protected abstract boolean canUpdatePath();
/*     */   
/*     */   public abstract boolean canNavigateGround();
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\PathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */