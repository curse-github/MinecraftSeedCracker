/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.function.TriConsumer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransportItemsBetweenContainers
/*     */   extends Behavior<PathfinderMob>
/*     */ {
/*     */   public static final int TARGET_INTERACTION_TIME = 60;
/*     */   private static final int VISITED_POSITIONS_MEMORY_TIME = 6000;
/*     */   private static final int TRANSPORTED_ITEM_MAX_STACK_SIZE = 16;
/*     */   private static final int MAX_VISITED_POSITIONS = 10;
/*     */   private static final int MAX_UNREACHABLE_POSITIONS = 50;
/*     */   private static final int PASSENGER_MOB_TARGET_SEARCH_DISTANCE = 1;
/*     */   private static final int IDLE_COOLDOWN = 140;
/*     */   private static final double CLOSE_ENOUGH_TO_START_QUEUING_DISTANCE = 3.0D;
/*     */   private static final double CLOSE_ENOUGH_TO_START_INTERACTING_WITH_TARGET_DISTANCE = 0.5D;
/*     */   private static final double CLOSE_ENOUGH_TO_START_INTERACTING_WITH_TARGET_PATH_END_DISTANCE = 1.0D;
/*     */   private static final double CLOSE_ENOUGH_TO_CONTINUE_INTERACTING_WITH_TARGET = 2.0D;
/*     */   private final float speedModifier;
/*     */   private final int horizontalSearchDistance;
/*     */   private final int verticalSearchDistance;
/*     */   private final Predicate<BlockState> sourceBlockType;
/*     */   private final Predicate<BlockState> destinationBlockType;
/*     */   private final Predicate<TransportItemTarget> shouldQueueForTarget;
/*     */   private final Consumer<PathfinderMob> onStartTravelling;
/*     */   private final Map<ContainerInteractionState, OnTargetReachedInteraction> onTargetInteractionActions;
/*  68 */   private TransportItemTarget target = null;
/*     */   
/*     */   private TransportItemState state;
/*     */   private ContainerInteractionState interactionState;
/*     */   private int ticksSinceReachingTarget;
/*     */   
/*     */   public TransportItemsBetweenContainers(float speedModifier, Predicate<BlockState> sourceBlockType, Predicate<BlockState> destinationBlockType, int horizontalSearchDistance, int verticalSearchDistance, Map<ContainerInteractionState, OnTargetReachedInteraction> onTargetInteractionActions, Consumer<PathfinderMob> onStartTravelling, Predicate<TransportItemTarget> shouldQueueForTarget) {
/*  75 */     super(ImmutableMap.of(MemoryModuleType.VISITED_BLOCK_POSITIONS, MemoryStatus.REGISTERED, MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS, MemoryStatus.REGISTERED, MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.speedModifier = speedModifier;
/*  81 */     this.sourceBlockType = sourceBlockType;
/*  82 */     this.destinationBlockType = destinationBlockType;
/*  83 */     this.horizontalSearchDistance = horizontalSearchDistance;
/*  84 */     this.verticalSearchDistance = verticalSearchDistance;
/*  85 */     this.onStartTravelling = onStartTravelling;
/*  86 */     this.shouldQueueForTarget = shouldQueueForTarget;
/*  87 */     this.onTargetInteractionActions = onTargetInteractionActions;
/*  88 */     this.state = TransportItemState.TRAVELLING;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/*  93 */     PathNavigation pathNavigation1 = body.getNavigation(); if (pathNavigation1 instanceof GroundPathNavigation) { GroundPathNavigation pathNavigation = (GroundPathNavigation)pathNavigation1;
/*  94 */       pathNavigation.setCanPathToTargetsBelowSurface(true); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) { return !body.isLeashed(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) { return (body.getBrain().getMemory(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS).isEmpty() && !body.isPanicking() && !body.isLeashed()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected boolean timedOut(long timestamp) { return false; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, PathfinderMob body, long timestamp) {
/* 115 */     boolean updatedInvalidTarget = updateInvalidTarget(level, body);
/* 116 */     if (this.target == null) {
/* 117 */       stop(level, body, timestamp); return;
/*     */     } 
/* 119 */     if (updatedInvalidTarget) {
/*     */       return;
/*     */     }
/*     */     
/* 123 */     if (this.state.equals(TransportItemState.QUEUING)) {
/* 124 */       onQueuingForTarget(this.target, level, body);
/*     */     }
/*     */     
/* 127 */     if (this.state.equals(TransportItemState.TRAVELLING)) {
/* 128 */       onTravelToTarget(this.target, level, body);
/*     */     }
/*     */     
/* 131 */     if (this.state.equals(TransportItemState.INTERACTING)) {
/* 132 */       onReachedTarget(this.target, level, body);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean updateInvalidTarget(ServerLevel level, PathfinderMob body) {
/* 137 */     if (!hasValidTarget(level, body)) {
/* 138 */       stopTargetingCurrentTarget(body);
/* 139 */       Optional<TransportItemTarget> targetBlockPosition = getTransportTarget(level, body);
/* 140 */       if (targetBlockPosition.isPresent()) {
/* 141 */         this.target = (TransportItemTarget)targetBlockPosition.get();
/* 142 */         onStartTravelling(body);
/* 143 */         setVisitedBlockPos(body, level, this.target.pos);
/* 144 */         return true;
/*     */       } 
/* 146 */       enterCooldownAfterNoMatchingTargetFound(body);
/* 147 */       return true;
/*     */     } 
/*     */     
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   private void onQueuingForTarget(TransportItemTarget target, Level level, PathfinderMob body) {
/* 154 */     if (!isAnotherMobInteractingWithTarget(target, level)) {
/* 155 */       resumeTravelling(body);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onTravelToTarget(TransportItemTarget target, Level level, PathfinderMob body) {
/* 160 */     if (isWithinTargetDistance(3.0D, target, level, body, getCenterPos(body)) && isAnotherMobInteractingWithTarget(target, level)) {
/* 161 */       startQueuing(body);
/* 162 */     } else if (isWithinTargetDistance(getInteractionRange(body), target, level, body, getCenterPos(body))) {
/* 163 */       startOnReachedTargetInteraction(target, body);
/*     */     } else {
/* 165 */       walkTowardsTarget(body);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 170 */   private Vec3 getCenterPos(PathfinderMob body) { return setMiddleYPosition(body, body.position()); }
/*     */ 
/*     */   
/*     */   protected void onReachedTarget(TransportItemTarget target, Level level, PathfinderMob body) {
/* 174 */     if (!isWithinTargetDistance(2.0D, target, level, body, getCenterPos(body))) {
/* 175 */       onStartTravelling(body);
/*     */     } else {
/* 177 */       this.ticksSinceReachingTarget++;
/*     */       
/* 179 */       onTargetInteraction(target, body);
/*     */       
/* 181 */       if (this.ticksSinceReachingTarget >= 60) {
/* 182 */         doReachedTargetInteraction(body, target.container, this::pickUpItems, (mob, container) -> stopTargetingCurrentTarget(body), this::putDownItem, (mob, container) -> stopTargetingCurrentTarget(body));
/* 183 */         onStartTravelling(body);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startQueuing(PathfinderMob body) {
/* 189 */     stopInPlace(body);
/* 190 */     setTransportingState(TransportItemState.QUEUING);
/*     */   }
/*     */   
/*     */   private void resumeTravelling(PathfinderMob body) {
/* 194 */     setTransportingState(TransportItemState.TRAVELLING);
/* 195 */     walkTowardsTarget(body);
/*     */   }
/*     */   
/*     */   private void walkTowardsTarget(PathfinderMob body) {
/* 199 */     if (this.target != null) {
/* 200 */       BehaviorUtils.setWalkAndLookTargetMemories(body, this.target.pos, this.speedModifier, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private void startOnReachedTargetInteraction(TransportItemTarget target, PathfinderMob body) {
/* 205 */     doReachedTargetInteraction(body, target.container, onReachedInteraction(ContainerInteractionState.PICKUP_ITEM), onReachedInteraction(ContainerInteractionState.PICKUP_NO_ITEM), onReachedInteraction(ContainerInteractionState.PLACE_ITEM), onReachedInteraction(ContainerInteractionState.PLACE_NO_ITEM));
/* 206 */     setTransportingState(TransportItemState.INTERACTING);
/*     */   }
/*     */   
/*     */   private void onStartTravelling(PathfinderMob body) {
/* 210 */     this.onStartTravelling.accept(body);
/* 211 */     setTransportingState(TransportItemState.TRAVELLING);
/* 212 */     this.interactionState = null;
/* 213 */     this.ticksSinceReachingTarget = 0;
/*     */   }
/*     */ 
/*     */   
/* 217 */   private BiConsumer<PathfinderMob, Container> onReachedInteraction(ContainerInteractionState state) { return (mob, container) -> setInteractionState(state); }
/*     */ 
/*     */ 
/*     */   
/* 221 */   private void setTransportingState(TransportItemState state) { this.state = state; }
/*     */ 
/*     */ 
/*     */   
/* 225 */   private void setInteractionState(ContainerInteractionState state) { this.interactionState = state; }
/*     */ 
/*     */   
/*     */   private void onTargetInteraction(TransportItemTarget target, PathfinderMob body) {
/* 229 */     body.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(target.pos));
/* 230 */     stopInPlace(body);
/* 231 */     if (this.interactionState != null) {
/* 232 */       Optional.ofNullable((OnTargetReachedInteraction)this.onTargetInteractionActions.get(this.interactionState)).ifPresent(action -> action.accept(body, target, Integer.valueOf(this.ticksSinceReachingTarget)));
/*     */     }
/*     */   }
/*     */   
/*     */   private void doReachedTargetInteraction(PathfinderMob body, Container container, BiConsumer<PathfinderMob, Container> onPickupSuccess, BiConsumer<PathfinderMob, Container> onPickupFailure, BiConsumer<PathfinderMob, Container> onPlaceSuccess, BiConsumer<PathfinderMob, Container> onPlaceFailure) {
/* 237 */     if (isPickingUpItems(body)) {
/* 238 */       if (matchesGettingItemsRequirement(container)) {
/* 239 */         onPickupSuccess.accept(body, container);
/*     */       } else {
/* 241 */         onPickupFailure.accept(body, container);
/*     */       }
/*     */     
/* 244 */     } else if (matchesLeavingItemsRequirement(body, container)) {
/* 245 */       onPlaceSuccess.accept(body, container);
/*     */     } else {
/* 247 */       onPlaceFailure.accept(body, container);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Optional<TransportItemTarget> getTransportTarget(ServerLevel level, PathfinderMob body) {
/* 253 */     AABB targetBlockSearchArea = getTargetSearchArea(body);
/* 254 */     Set<GlobalPos> visitedPositions = getVisitedPositions(body);
/* 255 */     Set<GlobalPos> unreachablePositions = getUnreachablePositions(body);
/* 256 */     List<ChunkPos> list = ChunkPos.rangeClosed(new ChunkPos(body.blockPosition()), Math.floorDiv(getHorizontalSearchDistance(body), 16) + 1).toList();
/* 257 */     TransportItemTarget target = null;
/* 258 */     double closestDistance = 3.4028234663852886E38D;
/*     */     
/* 260 */     for (ChunkPos chunkPos : list) {
/* 261 */       LevelChunk levelChunk = level.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
/* 262 */       if (levelChunk != null) {
/* 263 */         for (BlockEntity potentialTarget : levelChunk.getBlockEntities().values()) {
/* 264 */           if (potentialTarget instanceof ChestBlockEntity) { ChestBlockEntity chestBlockEntity = (ChestBlockEntity)potentialTarget;
/* 265 */             double distance = chestBlockEntity.getBlockPos().distToCenterSqr(body.position());
/* 266 */             if (distance < closestDistance) {
/* 267 */               TransportItemTarget targetValidToPick = isTargetValidToPick(body, level, chestBlockEntity, visitedPositions, unreachablePositions, targetBlockSearchArea);
/* 268 */               if (targetValidToPick != null) {
/* 269 */                 target = targetValidToPick;
/* 270 */                 closestDistance = distance;
/*     */               } 
/*     */             }  }
/*     */         
/*     */         } 
/*     */       }
/*     */     } 
/* 277 */     return (target == null) ? Optional.empty() : Optional.of(target);
/*     */   }
/*     */   
/*     */   private TransportItemTarget isTargetValidToPick(PathfinderMob body, Level level, BlockEntity blockEntity, Set<GlobalPos> visitedPositions, Set<GlobalPos> unreachablePositions, AABB targetBlockSearchArea) {
/* 281 */     BlockPos blockPos = blockEntity.getBlockPos();
/* 282 */     boolean isWithinSearchArea = targetBlockSearchArea.contains(blockPos.getX(), blockPos.getY(), blockPos.getZ());
/* 283 */     if (!isWithinSearchArea) {
/* 284 */       return null;
/*     */     }
/* 286 */     TransportItemTarget transportItemTarget = TransportItemTarget.tryCreatePossibleTarget(blockEntity, level);
/* 287 */     if (transportItemTarget == null) {
/* 288 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 292 */     boolean isValidTarget = (isWantedBlock(body, transportItemTarget.state) && !isPositionAlreadyVisited(visitedPositions, unreachablePositions, transportItemTarget, level) && !isContainerLocked(transportItemTarget));
/* 293 */     return isValidTarget ? transportItemTarget : null;
/*     */   }
/*     */   
/*     */   private boolean isContainerLocked(TransportItemTarget transportItemTarget) {
/* 297 */     BlockEntity blockEntity1 = transportItemTarget.blockEntity; if (blockEntity1 instanceof BaseContainerBlockEntity) { BaseContainerBlockEntity blockEntity = (BaseContainerBlockEntity)blockEntity1; if (blockEntity.isLocked()); }  return false;
/*     */   }
/*     */   
/*     */   private boolean hasValidTarget(Level level, PathfinderMob body) {
/* 301 */     boolean targetIsOfValidType = (this.target != null && isWantedBlock(body, this.target.state) && targetHasNotChanged(level, this.target));
/* 302 */     if (targetIsOfValidType && !isTargetBlocked(level, this.target)) {
/* 303 */       if (!this.state.equals(TransportItemState.TRAVELLING)) {
/* 304 */         return true;
/*     */       }
/*     */       
/* 307 */       if (hasValidTravellingPath(level, this.target, body)) {
/* 308 */         return true;
/*     */       }
/* 310 */       markVisitedBlockPosAsUnreachable(body, level, this.target.pos);
/*     */     } 
/* 312 */     return false;
/*     */   }
/*     */   
/*     */   private boolean hasValidTravellingPath(Level level, TransportItemTarget target, PathfinderMob body) {
/* 316 */     Path path = (body.getNavigation().getPath() == null) ? body.getNavigation().createPath(target.pos, 0) : body.getNavigation().getPath();
/* 317 */     Vec3 posFromWhichToReachTarget = getPositionToReachTargetFrom(path, body);
/* 318 */     boolean canReachTarget = isWithinTargetDistance(getInteractionRange(body), target, level, body, posFromWhichToReachTarget);
/* 319 */     boolean hasNotYetCreatedPathToTarget = (path == null && !canReachTarget);
/* 320 */     return (hasNotYetCreatedPathToTarget || targetIsReachableFromPosition(level, canReachTarget, posFromWhichToReachTarget, target, body));
/*     */   }
/*     */   
/*     */   private Vec3 getPositionToReachTargetFrom(Path path, PathfinderMob body) {
/* 324 */     boolean haveNoValidPath = (path == null || path.getEndNode() == null);
/* 325 */     Vec3 bottomCenter = haveNoValidPath ? body.position() : path.getEndNode().asBlockPos().getBottomCenter();
/* 326 */     return setMiddleYPosition(body, bottomCenter);
/*     */   }
/*     */ 
/*     */   
/* 330 */   private Vec3 setMiddleYPosition(PathfinderMob body, Vec3 pos) { return pos.add(0.0D, body.getBoundingBox().getYsize() / 2.0D, 0.0D); }
/*     */ 
/*     */ 
/*     */   
/* 334 */   private boolean isTargetBlocked(Level level, TransportItemTarget target) { return ChestBlock.isChestBlockedAt(level, target.pos); }
/*     */ 
/*     */ 
/*     */   
/* 338 */   private boolean targetHasNotChanged(Level level, TransportItemTarget target) { return target.blockEntity.equals(level.getBlockEntity(target.pos)); }
/*     */ 
/*     */   
/*     */   private Stream<TransportItemTarget> getConnectedTargets(TransportItemTarget target, Level level) {
/* 342 */     if (target.state.getValueOrElse(ChestBlock.TYPE, ChestType.SINGLE) != ChestType.SINGLE) {
/* 343 */       TransportItemTarget connectedTarget = TransportItemTarget.tryCreatePossibleTarget(ChestBlock.getConnectedBlockPos(target.pos, target.state), level);
/* 344 */       return (connectedTarget != null) ? Stream.of(new TransportItemTarget[] { target, connectedTarget }) : Stream.of(target);
/*     */     } 
/* 346 */     return Stream.of(target);
/*     */   }
/*     */   
/*     */   private AABB getTargetSearchArea(PathfinderMob mob) {
/* 350 */     int horizontalSearchDistance = getHorizontalSearchDistance(mob);
/* 351 */     return (new AABB(mob.blockPosition())).inflate(horizontalSearchDistance, getVerticalSearchDistance(mob), horizontalSearchDistance);
/*     */   }
/*     */ 
/*     */   
/* 355 */   private int getHorizontalSearchDistance(PathfinderMob mob) { return mob.isPassenger() ? 1 : this.horizontalSearchDistance; }
/*     */ 
/*     */ 
/*     */   
/* 359 */   private int getVerticalSearchDistance(PathfinderMob mob) { return mob.isPassenger() ? 1 : this.verticalSearchDistance; }
/*     */ 
/*     */ 
/*     */   
/* 363 */   private static Set<GlobalPos> getVisitedPositions(PathfinderMob mob) { return (Set)mob.getBrain().getMemory(MemoryModuleType.VISITED_BLOCK_POSITIONS).orElse(Set.of()); }
/*     */ 
/*     */ 
/*     */   
/* 367 */   private static Set<GlobalPos> getUnreachablePositions(PathfinderMob mob) { return (Set)mob.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS).orElse(Set.of()); }
/*     */ 
/*     */ 
/*     */   
/* 371 */   private boolean isPositionAlreadyVisited(Set<GlobalPos> visitedPositions, Set<GlobalPos> unreachablePositions, TransportItemTarget target, Level level) { return getConnectedTargets(target, level)
/* 372 */       .map(transportItemTarget -> new GlobalPos(level.dimension(), transportItemTarget.pos))
/* 373 */       .anyMatch(pos -> (visitedPositions.contains(pos) || unreachablePositions.contains(pos))); }
/*     */ 
/*     */ 
/*     */   
/* 377 */   private static boolean hasFinishedPath(PathfinderMob body) { return (body.getNavigation().getPath() != null && body.getNavigation().getPath().isDone()); }
/*     */ 
/*     */   
/*     */   protected void setVisitedBlockPos(PathfinderMob body, Level level, BlockPos target) {
/* 381 */     Set<GlobalPos> visitedPositions = new HashSet<GlobalPos>(getVisitedPositions(body));
/* 382 */     visitedPositions.add(new GlobalPos(level.dimension(), target));
/* 383 */     if (visitedPositions.size() > 10) {
/* 384 */       enterCooldownAfterNoMatchingTargetFound(body);
/*     */     } else {
/* 386 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.VISITED_BLOCK_POSITIONS, visitedPositions, 6000L);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void markVisitedBlockPosAsUnreachable(PathfinderMob body, Level level, BlockPos target) {
/* 391 */     Set<GlobalPos> visitedPositions = new HashSet<GlobalPos>(getVisitedPositions(body));
/* 392 */     visitedPositions.remove(new GlobalPos(level.dimension(), target));
/* 393 */     Set<GlobalPos> unreachablePositions = new HashSet<GlobalPos>(getUnreachablePositions(body));
/* 394 */     unreachablePositions.add(new GlobalPos(level.dimension(), target));
/* 395 */     if (unreachablePositions.size() > 50) {
/* 396 */       enterCooldownAfterNoMatchingTargetFound(body);
/*     */     } else {
/* 398 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.VISITED_BLOCK_POSITIONS, visitedPositions, 6000L);
/* 399 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS, unreachablePositions, 6000L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 404 */   private boolean isWantedBlock(PathfinderMob mob, BlockState block) { return isPickingUpItems(mob) ? this.sourceBlockType.test(block) : this.destinationBlockType.test(block); }
/*     */ 
/*     */ 
/*     */   
/* 408 */   private static double getInteractionRange(PathfinderMob body) { return hasFinishedPath(body) ? 1.0D : 0.5D; }
/*     */ 
/*     */   
/*     */   private boolean isWithinTargetDistance(double distance, TransportItemTarget target, Level level, PathfinderMob body, Vec3 fromPos) {
/* 412 */     AABB boundingBox = body.getBoundingBox();
/* 413 */     AABB movedBoundBox = AABB.ofSize(fromPos, boundingBox.getXsize(), boundingBox.getYsize(), boundingBox.getZsize());
/* 414 */     return target.state.getCollisionShape(level, target.pos).bounds().inflate(distance, 0.5D, distance).move(target.pos).intersects(movedBoundBox);
/*     */   }
/*     */ 
/*     */   
/* 418 */   private boolean targetIsReachableFromPosition(Level level, boolean canReachTarget, Vec3 pos, TransportItemTarget target, PathfinderMob body) { return (canReachTarget && canSeeAnyTargetSide(target, level, body, pos)); }
/*     */ 
/*     */   
/*     */   private boolean canSeeAnyTargetSide(TransportItemTarget target, Level level, PathfinderMob body, Vec3 eyePosition) {
/* 422 */     Vec3 center = target.pos.getCenter();
/* 423 */     return Direction.stream()
/* 424 */       .map(direction -> center.add(0.5D * direction.getStepX(), 0.5D * direction.getStepY(), 0.5D * direction.getStepZ()))
/* 425 */       .map(hitTarget -> level.clip(new ClipContext(eyePosition, hitTarget, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, body)))
/* 426 */       .anyMatch(hitResult -> (hitResult.getType() == HitResult.Type.BLOCK && hitResult.getBlockPos().equals(target.pos)));
/*     */   }
/*     */ 
/*     */   
/* 430 */   private boolean isAnotherMobInteractingWithTarget(TransportItemTarget target, Level level) { return getConnectedTargets(target, level).anyMatch(this.shouldQueueForTarget); }
/*     */ 
/*     */ 
/*     */   
/* 434 */   private static boolean isPickingUpItems(PathfinderMob body) { return body.getMainHandItem().isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 438 */   private static boolean matchesGettingItemsRequirement(Container container) { return !container.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/* 442 */   private static boolean matchesLeavingItemsRequirement(PathfinderMob body, Container container) { return (container.isEmpty() || hasItemMatchingHandItem(body, container)); }
/*     */ 
/*     */   
/*     */   private static boolean hasItemMatchingHandItem(PathfinderMob body, Container container) {
/* 446 */     ItemStack mainHandItem = body.getMainHandItem();
/* 447 */     for (ItemStack itemStack : container) {
/* 448 */       if (ItemStack.isSameItem(itemStack, mainHandItem)) {
/* 449 */         return true;
/*     */       }
/*     */     } 
/* 452 */     return false;
/*     */   }
/*     */   
/*     */   private void pickUpItems(PathfinderMob body, Container container) {
/* 456 */     body.setItemSlot(EquipmentSlot.MAINHAND, pickupItemFromContainer(container));
/* 457 */     body.setGuaranteedDrop(EquipmentSlot.MAINHAND);
/* 458 */     container.setChanged();
/* 459 */     clearMemoriesAfterMatchingTargetFound(body);
/*     */   }
/*     */   
/*     */   private void putDownItem(PathfinderMob body, Container container) {
/* 463 */     ItemStack itemsLeftAfterVisitingChest = addItemsToContainer(body, container);
/* 464 */     container.setChanged();
/* 465 */     body.setItemSlot(EquipmentSlot.MAINHAND, itemsLeftAfterVisitingChest);
/* 466 */     if (itemsLeftAfterVisitingChest.isEmpty()) {
/* 467 */       clearMemoriesAfterMatchingTargetFound(body);
/*     */     } else {
/* 469 */       stopTargetingCurrentTarget(body);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ItemStack pickupItemFromContainer(Container container) {
/* 474 */     int slot = 0;
/* 475 */     for (ItemStack itemStack : container) {
/* 476 */       if (!itemStack.isEmpty()) {
/* 477 */         int itemCount = Math.min(itemStack.getCount(), 16);
/* 478 */         return container.removeItem(slot, itemCount);
/*     */       } 
/* 480 */       slot++;
/*     */     } 
/* 482 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   private static ItemStack addItemsToContainer(PathfinderMob body, Container container) {
/* 486 */     int slot = 0;
/* 487 */     ItemStack itemStack = body.getMainHandItem();
/* 488 */     for (ItemStack containerItemStack : container) {
/* 489 */       if (containerItemStack.isEmpty()) {
/* 490 */         container.setItem(slot, itemStack);
/* 491 */         return ItemStack.EMPTY;
/* 492 */       }  if (ItemStack.isSameItemSameComponents(containerItemStack, itemStack) && containerItemStack.getCount() < containerItemStack.getMaxStackSize()) {
/* 493 */         int countThatCanBeAdded = containerItemStack.getMaxStackSize() - containerItemStack.getCount();
/* 494 */         int countToAdd = Math.min(countThatCanBeAdded, itemStack.getCount());
/* 495 */         containerItemStack.setCount(containerItemStack.getCount() + countToAdd);
/* 496 */         itemStack.setCount(itemStack.getCount() - countThatCanBeAdded);
/* 497 */         container.setItem(slot, containerItemStack);
/* 498 */         if (itemStack.isEmpty()) {
/* 499 */           return ItemStack.EMPTY;
/*     */         }
/*     */       } 
/* 502 */       slot++;
/*     */     } 
/* 504 */     return itemStack;
/*     */   }
/*     */   
/*     */   protected void stopTargetingCurrentTarget(PathfinderMob body) {
/* 508 */     this.ticksSinceReachingTarget = 0;
/* 509 */     this.target = null;
/* 510 */     body.getNavigation().stop();
/* 511 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*     */   }
/*     */   
/*     */   protected void clearMemoriesAfterMatchingTargetFound(PathfinderMob body) {
/* 515 */     stopTargetingCurrentTarget(body);
/* 516 */     body.getBrain().eraseMemory(MemoryModuleType.VISITED_BLOCK_POSITIONS);
/* 517 */     body.getBrain().eraseMemory(MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS);
/*     */   }
/*     */   
/*     */   private void enterCooldownAfterNoMatchingTargetFound(PathfinderMob body) {
/* 521 */     stopTargetingCurrentTarget(body);
/* 522 */     body.getBrain().setMemory(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, Integer.valueOf(140));
/* 523 */     body.getBrain().eraseMemory(MemoryModuleType.VISITED_BLOCK_POSITIONS);
/* 524 */     body.getBrain().eraseMemory(MemoryModuleType.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 529 */     onStartTravelling(body);
/* 530 */     PathNavigation pathNavigation1 = body.getNavigation(); if (pathNavigation1 instanceof GroundPathNavigation) { GroundPathNavigation pathNavigation = (GroundPathNavigation)pathNavigation1;
/* 531 */       pathNavigation.setCanPathToTargetsBelowSurface(false); }
/*     */   
/*     */   }
/*     */   
/*     */   private void stopInPlace(PathfinderMob mob) {
/* 536 */     mob.getNavigation().stop();
/* 537 */     mob.setXxa(0.0F);
/* 538 */     mob.setYya(0.0F);
/* 539 */     mob.setSpeed(0.0F);
/* 540 */     mob.setDeltaMovement(0.0D, (mob.getDeltaMovement()).y, 0.0D);
/*     */   }
/*     */   
/*     */   public enum TransportItemState {
/* 544 */     TRAVELLING,
/* 545 */     QUEUING,
/* 546 */     INTERACTING;
/*     */   }
/*     */   
/*     */   public enum ContainerInteractionState {
/* 550 */     PICKUP_ITEM,
/* 551 */     PICKUP_NO_ITEM,
/* 552 */     PLACE_ITEM,
/* 553 */     PLACE_NO_ITEM; }
/*     */   public static final class TransportItemTarget extends Record { private final BlockPos pos; private final Container container; private final BlockEntity blockEntity; private final BlockState state;
/*     */     
/* 556 */     public TransportItemTarget(BlockPos pos, Container container, BlockEntity blockEntity, BlockState state) { this.pos = pos; this.container = container; this.blockEntity = blockEntity; this.state = state; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 556 */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget; } public BlockPos pos() { return this.pos; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #556	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/behavior/TransportItemsBetweenContainers$TransportItemTarget;
/* 556 */       //   0	8	1	o	Ljava/lang/Object; } public Container container() { return this.container; } public BlockEntity blockEntity() { return this.blockEntity; } public BlockState state() { return this.state; }
/*     */     public static TransportItemTarget tryCreatePossibleTarget(BlockEntity blockEntity, Level level) {
/* 558 */       BlockPos blockPos = blockEntity.getBlockPos();
/* 559 */       BlockState blockState = blockEntity.getBlockState();
/* 560 */       Container container = getBlockEntityContainer(blockEntity, blockState, level, blockPos);
/* 561 */       if (container != null) {
/* 562 */         return new TransportItemTarget(blockPos, container, blockEntity, blockState);
/*     */       }
/* 564 */       return null;
/*     */     }
/*     */     
/*     */     public static TransportItemTarget tryCreatePossibleTarget(BlockPos blockPos, Level level) {
/* 568 */       BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 569 */       return (blockEntity == null) ? null : tryCreatePossibleTarget(blockEntity, level);
/*     */     }
/*     */     
/*     */     private static Container getBlockEntityContainer(BlockEntity blockEntity, BlockState blockState, Level level, BlockPos blockPos) {
/* 573 */       Block block = blockState.getBlock(); if (block instanceof ChestBlock) { ChestBlock chestBlock = (ChestBlock)block;
/* 574 */         return ChestBlock.getContainer(chestBlock, blockState, level, blockPos, false); }
/* 575 */        if (blockEntity instanceof Container) return (Container)blockEntity;
/*     */ 
/*     */       
/* 578 */       return null;
/*     */     } }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnTargetReachedInteraction extends TriConsumer<PathfinderMob, TransportItemTarget, Integer> {}
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TransportItemsBetweenContainers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */