/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.FenceGateBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WalkNodeEvaluator
/*     */   extends NodeEvaluator
/*     */ {
/*     */   public static final double SPACE_BETWEEN_WALL_POSTS = 0.5D;
/*     */   private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125D;
/*  38 */   private final Long2ObjectMap<PathType> pathTypesByPosCacheByMob = new Long2ObjectOpenHashMap();
/*  39 */   private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();
/*  40 */   private final Node[] reusableNeighbors = new Node[Direction.Plane.HORIZONTAL.length()];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  47 */     super.prepare(level, entity);
/*  48 */     entity.onPathfindingStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  53 */     this.mob.onPathfindingDone();
/*     */     
/*  55 */     this.pathTypesByPosCacheByMob.clear();
/*  56 */     this.collisionCache.clear();
/*     */     
/*  58 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  64 */     BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
/*  65 */     int startY = this.mob.getBlockY();
/*  66 */     BlockState blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*     */     
/*  68 */     if (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  69 */       while (this.mob.canStandOnFluid(blockState.getFluidState())) {
/*  70 */         startY++;
/*  71 */         blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*     */       } 
/*  73 */       startY--;
/*  74 */     } else if (canFloat() && this.mob.isInWater()) {
/*  75 */       while (blockState.is(Blocks.WATER) || blockState.getFluidState() == Fluids.WATER.getSource(false)) {
/*  76 */         startY++;
/*  77 */         blockState = this.currentContext.getBlockState(reusablePos.set(this.mob.getX(), startY, this.mob.getZ()));
/*     */       } 
/*  79 */       startY--;
/*     */     }
/*  81 */     else if (this.mob.onGround()) {
/*  82 */       startY = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } else {
/*  84 */       reusablePos.set(this.mob.getX(), this.mob.getY() + 1.0D, this.mob.getZ());
/*  85 */       while (reusablePos.getY() > this.currentContext.level().getMinY()) {
/*  86 */         startY = reusablePos.getY();
/*  87 */         reusablePos.setY(reusablePos.getY() - 1);
/*  88 */         BlockState belowBlockState = this.currentContext.getBlockState(reusablePos);
/*  89 */         if (!belowBlockState.isAir() && !belowBlockState.isPathfindable(PathComputationType.LAND)) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  96 */     BlockPos startPos = this.mob.blockPosition();
/*  97 */     if (!canStartAt(reusablePos.set(startPos.getX(), startY, startPos.getZ()))) {
/*  98 */       AABB mobBB = this.mob.getBoundingBox();
/*     */       
/* 100 */       if (canStartAt(reusablePos.set(mobBB.minX, startY, mobBB.minZ)) || 
/* 101 */         canStartAt(reusablePos.set(mobBB.minX, startY, mobBB.maxZ)) || 
/* 102 */         canStartAt(reusablePos.set(mobBB.maxX, startY, mobBB.minZ)) || 
/* 103 */         canStartAt(reusablePos.set(mobBB.maxX, startY, mobBB.maxZ)))
/*     */       {
/* 105 */         return getStartNode(reusablePos);
/*     */       }
/*     */     } 
/* 108 */     return getStartNode(new BlockPos(startPos.getX(), startY, startPos.getZ()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node getStartNode(BlockPos pos) {
/* 114 */     Node node = getNode(pos);
/* 115 */     node.type = getCachedPathType(node.x, node.y, node.z);
/* 116 */     node.costMalus = this.mob.getPathfindingMalus(node.type);
/* 117 */     return node;
/*     */   }
/*     */   
/*     */   protected boolean canStartAt(BlockPos pos) {
/* 121 */     PathType blockPathType = getCachedPathType(pos.getX(), pos.getY(), pos.getZ());
/* 122 */     return (blockPathType != PathType.OPEN && this.mob.getPathfindingMalus(blockPathType) >= 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public Target getTarget(double x, double y, double z) { return getTargetNodeAt(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] neighbors, Node pos) {
/* 132 */     int p = 0;
/* 133 */     int jumpSize = 0;
/* 134 */     PathType blockPathTypeAbove = getCachedPathType(pos.x, pos.y + 1, pos.z);
/* 135 */     PathType blockPathTypeCurrent = getCachedPathType(pos.x, pos.y, pos.z);
/*     */     
/* 137 */     if (this.mob.getPathfindingMalus(blockPathTypeAbove) >= 0.0F && blockPathTypeCurrent != PathType.STICKY_HONEY) {
/* 138 */       jumpSize = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
/*     */     }
/*     */     
/* 141 */     double posHeight = getFloorLevel(new BlockPos(pos.x, pos.y, pos.z));
/* 142 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 143 */       Node node = findAcceptedNode(pos.x + direction.getStepX(), pos.y, pos.z + direction.getStepZ(), jumpSize, posHeight, direction, blockPathTypeCurrent);
/* 144 */       this.reusableNeighbors[direction.get2DDataValue()] = node;
/* 145 */       if (isNeighborValid(node, pos)) {
/* 146 */         neighbors[p++] = node;
/*     */       }
/*     */     } 
/*     */     
/* 150 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 151 */       Direction secondDirection = direction.getClockWise();
/* 152 */       if (isDiagonalValid(pos, this.reusableNeighbors[direction.get2DDataValue()], this.reusableNeighbors[secondDirection.get2DDataValue()])) {
/* 153 */         Node diagonalNode = findAcceptedNode(pos.x + direction.getStepX() + secondDirection.getStepX(), pos.y, pos.z + direction.getStepZ() + secondDirection.getStepZ(), jumpSize, posHeight, direction, blockPathTypeCurrent);
/* 154 */         if (isDiagonalValid(diagonalNode)) {
/* 155 */           neighbors[p++] = diagonalNode;
/*     */         }
/*     */       } 
/*     */     } 
/* 159 */     return p;
/*     */   }
/*     */ 
/*     */   
/* 163 */   protected boolean isNeighborValid(Node neighbor, Node current) { return (neighbor != null && !neighbor.closed && (neighbor.costMalus >= 0.0F || current.costMalus < 0.0F)); }
/*     */ 
/*     */   
/*     */   protected boolean isDiagonalValid(Node pos, Node ew, Node ns) {
/* 167 */     if (ns == null || ew == null || ns.y > pos.y || ew.y > pos.y) {
/* 168 */       return false;
/*     */     }
/* 170 */     if (ew.type == PathType.WALKABLE_DOOR || ns.type == PathType.WALKABLE_DOOR)
/*     */     {
/* 172 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 176 */     boolean canPassBetweenPosts = (ns.type == PathType.FENCE && ew.type == PathType.FENCE && this.mob.getBbWidth() < 0.5D);
/* 177 */     return ((ns.y < pos.y || ns.costMalus >= 0.0F || canPassBetweenPosts) && (ew.y < pos.y || ew.costMalus >= 0.0F || canPassBetweenPosts));
/*     */   }
/*     */   
/*     */   protected boolean isDiagonalValid(Node diagonal) {
/* 181 */     if (diagonal == null || diagonal.closed) {
/* 182 */       return false;
/*     */     }
/* 184 */     if (diagonal.type == PathType.WALKABLE_DOOR)
/*     */     {
/* 186 */       return false;
/*     */     }
/* 188 */     return (diagonal.costMalus >= 0.0F);
/*     */   }
/*     */ 
/*     */   
/* 192 */   private static boolean doesBlockHavePartialCollision(PathType type) { return (type == PathType.FENCE || type == PathType.DOOR_WOOD_CLOSED || type == PathType.DOOR_IRON_CLOSED); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReachWithoutCollision(Node posTo) {
/* 198 */     AABB bb = this.mob.getBoundingBox();
/*     */ 
/*     */ 
/*     */     
/* 202 */     Vec3 delta = new Vec3(posTo.x - this.mob.getX() + bb.getXsize() / 2.0D, posTo.y - this.mob.getY() + bb.getYsize() / 2.0D, posTo.z - this.mob.getZ() + bb.getZsize() / 2.0D);
/*     */     
/* 204 */     int steps = Mth.ceil(delta.length() / bb.getSize());
/* 205 */     delta = delta.scale((1.0F / steps));
/* 206 */     for (int i = 1; i <= steps; i++) {
/* 207 */       bb = bb.move(delta);
/* 208 */       if (hasCollisions(bb)) {
/* 209 */         return false;
/*     */       }
/*     */     } 
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   protected double getFloorLevel(BlockPos pos) {
/* 216 */     CollisionGetter collisionGetter = this.currentContext.level();
/* 217 */     if ((canFloat() || isAmphibious()) && collisionGetter.getFluidState(pos).is(FluidTags.WATER)) {
/* 218 */       return pos.getY() + 0.5D;
/*     */     }
/* 220 */     return getFloorLevel(collisionGetter, pos);
/*     */   }
/*     */   
/*     */   public static double getFloorLevel(BlockGetter level, BlockPos pos) {
/* 224 */     BlockPos target = pos.below();
/* 225 */     VoxelShape shape = level.getBlockState(target).getCollisionShape(level, target);
/* 226 */     return target.getY() + (shape.isEmpty() ? 0.0D : shape.max(Direction.Axis.Y));
/*     */   }
/*     */ 
/*     */   
/* 230 */   protected boolean isAmphibious() { return false; }
/*     */ 
/*     */   
/*     */   protected Node findAcceptedNode(int x, int y, int z, int jumpSize, double nodeHeight, Direction travelDirection, PathType blockPathTypeCurrent) {
/* 234 */     Node best = null;
/* 235 */     BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
/*     */     
/* 237 */     double maxYTarget = getFloorLevel(reusablePos.set(x, y, z));
/*     */     
/* 239 */     if (maxYTarget - nodeHeight > getMobJumpHeight()) {
/* 240 */       return null;
/*     */     }
/*     */     
/* 243 */     PathType pathType = getCachedPathType(x, y, z);
/* 244 */     float pathCost = this.mob.getPathfindingMalus(pathType);
/*     */     
/* 246 */     if (pathCost >= 0.0F) {
/* 247 */       best = getNodeAndUpdateCostToMax(x, y, z, pathType, pathCost);
/*     */     }
/*     */ 
/*     */     
/* 251 */     if (doesBlockHavePartialCollision(blockPathTypeCurrent) && best != null && best.costMalus >= 0.0F && !canReachWithoutCollision(best)) {
/* 252 */       best = null;
/*     */     }
/*     */     
/* 255 */     if (pathType == PathType.WALKABLE || (isAmphibious() && pathType == PathType.WATER)) {
/* 256 */       return best;
/*     */     }
/*     */     
/* 259 */     if ((best == null || best.costMalus < 0.0F) && jumpSize > 0 && (pathType != PathType.FENCE || canWalkOverFences()) && pathType != PathType.UNPASSABLE_RAIL && pathType != PathType.TRAPDOOR && pathType != PathType.POWDER_SNOW) {
/* 260 */       best = tryJumpOn(x, y, z, jumpSize, nodeHeight, travelDirection, blockPathTypeCurrent, reusablePos);
/* 261 */     } else if (!isAmphibious() && pathType == PathType.WATER && !canFloat()) {
/* 262 */       best = tryFindFirstNonWaterBelow(x, y, z, best);
/* 263 */     } else if (pathType == PathType.OPEN) {
/* 264 */       best = tryFindFirstGroundNodeBelow(x, y, z);
/* 265 */     } else if (doesBlockHavePartialCollision(pathType) && best == null) {
/* 266 */       best = getClosedNode(x, y, z, pathType);
/*     */     } 
/* 268 */     return best;
/*     */   }
/*     */ 
/*     */   
/* 272 */   private double getMobJumpHeight() { return Math.max(1.125D, this.mob.maxUpStep()); }
/*     */ 
/*     */   
/*     */   private Node getNodeAndUpdateCostToMax(int x, int y, int z, PathType pathType, float cost) {
/* 276 */     Node node = getNode(x, y, z);
/* 277 */     node.type = pathType;
/* 278 */     node.costMalus = Math.max(node.costMalus, cost);
/* 279 */     return node;
/*     */   }
/*     */   
/*     */   private Node getBlockedNode(int x, int y, int z) {
/* 283 */     Node node = getNode(x, y, z);
/* 284 */     node.type = PathType.BLOCKED;
/* 285 */     node.costMalus = -1.0F;
/* 286 */     return node;
/*     */   }
/*     */   
/*     */   private Node getClosedNode(int x, int y, int z, PathType pathType) {
/* 290 */     Node node = getNode(x, y, z);
/* 291 */     node.closed = true;
/* 292 */     node.type = pathType;
/* 293 */     node.costMalus = pathType.getMalus();
/* 294 */     return node;
/*     */   }
/*     */   
/*     */   private Node tryJumpOn(int x, int y, int z, int jumpSize, double nodeHeight, Direction travelDirection, PathType blockPathTypeCurrent, BlockPos.MutableBlockPos reusablePos) {
/* 298 */     Node nodeAbove = findAcceptedNode(x, y + 1, z, jumpSize - 1, nodeHeight, travelDirection, blockPathTypeCurrent);
/*     */     
/* 300 */     if (nodeAbove == null) {
/* 301 */       return null;
/*     */     }
/*     */     
/* 304 */     if (this.mob.getBbWidth() >= 1.0F) {
/* 305 */       return nodeAbove;
/*     */     }
/*     */     
/* 308 */     if (nodeAbove.type != PathType.OPEN && nodeAbove.type != PathType.WALKABLE) {
/* 309 */       return nodeAbove;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 315 */     double centerX = (x - travelDirection.getStepX()) + 0.5D;
/* 316 */     double centerZ = (z - travelDirection.getStepZ()) + 0.5D;
/* 317 */     double halfWidth = this.mob.getBbWidth() / 2.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 323 */     AABB grow = new AABB(centerX - halfWidth, getFloorLevel(reusablePos.set(centerX, (y + 1), centerZ)) + 0.001D, centerZ - halfWidth, centerX + halfWidth, this.mob.getBbHeight() + getFloorLevel(reusablePos.set(nodeAbove.x, nodeAbove.y, nodeAbove.z)) - 0.002D, centerZ + halfWidth);
/*     */ 
/*     */     
/* 326 */     return hasCollisions(grow) ? null : nodeAbove;
/*     */   }
/*     */ 
/*     */   
/*     */   private Node tryFindFirstNonWaterBelow(int x, int y, int z, Node best) {
/* 331 */     for (; --y > this.mob.level().getMinY(); y--) {
/* 332 */       PathType pathTypeLocal = getCachedPathType(x, y, z);
/*     */       
/* 334 */       if (pathTypeLocal == PathType.WATER) {
/* 335 */         best = getNodeAndUpdateCostToMax(x, y, z, pathTypeLocal, this.mob.getPathfindingMalus(pathTypeLocal));
/*     */       } else {
/* 337 */         return best;
/*     */       } 
/*     */     } 
/* 340 */     return best;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Node tryFindFirstGroundNodeBelow(int x, int y, int z) {
/* 346 */     for (int currentY = y - 1; currentY >= this.mob.level().getMinY(); currentY--) {
/* 347 */       if (y - currentY > this.mob.getMaxFallDistance()) {
/* 348 */         return getBlockedNode(x, currentY, z);
/*     */       }
/*     */       
/* 351 */       PathType pathType = getCachedPathType(x, currentY, z);
/* 352 */       float pathCost = this.mob.getPathfindingMalus(pathType);
/*     */       
/* 354 */       if (pathType != PathType.OPEN) {
/* 355 */         if (pathCost >= 0.0F) {
/* 356 */           return getNodeAndUpdateCostToMax(x, currentY, z, pathType, pathCost);
/*     */         }
/* 358 */         return getBlockedNode(x, currentY, z);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 363 */     return getBlockedNode(x, y, z);
/*     */   }
/*     */ 
/*     */   
/* 367 */   private boolean hasCollisions(AABB aabb) { return this.collisionCache.computeIfAbsent(aabb, bb -> !this.currentContext.level().noCollision(this.mob, aabb)); }
/*     */ 
/*     */ 
/*     */   
/* 371 */   protected PathType getCachedPathType(int x, int y, int z) { return (PathType)this.pathTypesByPosCacheByMob.computeIfAbsent(BlockPos.asLong(x, y, z), k -> getPathTypeOfMob(this.currentContext, x, y, z, this.mob)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
/* 377 */     Set<PathType> blockTypes = getPathTypeWithinMobBB(context, x, y, z);
/*     */     
/* 379 */     if (blockTypes.contains(PathType.FENCE)) {
/* 380 */       return PathType.FENCE;
/*     */     }
/*     */     
/* 383 */     if (blockTypes.contains(PathType.UNPASSABLE_RAIL)) {
/* 384 */       return PathType.UNPASSABLE_RAIL;
/*     */     }
/*     */     
/* 387 */     PathType blockType = PathType.BLOCKED;
/* 388 */     for (PathType type : blockTypes) {
/*     */       
/* 390 */       if (mob.getPathfindingMalus(type) < 0.0F) {
/* 391 */         return type;
/*     */       }
/*     */ 
/*     */       
/* 395 */       if (mob.getPathfindingMalus(type) >= mob.getPathfindingMalus(blockType)) {
/* 396 */         blockType = type;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 401 */     if (this.entityWidth <= 1 && blockType != PathType.OPEN && mob.getPathfindingMalus(blockType) == 0.0F && getPathType(context, x, y, z) == PathType.OPEN) {
/* 402 */       return PathType.OPEN;
/*     */     }
/*     */     
/* 405 */     return blockType;
/*     */   }
/*     */   
/*     */   public Set<PathType> getPathTypeWithinMobBB(PathfindingContext context, int x, int y, int z) {
/* 409 */     EnumSet<PathType> blockTypes = EnumSet.noneOf(PathType.class);
/* 410 */     for (int dx = 0; dx < this.entityWidth; dx++) {
/* 411 */       for (int dy = 0; dy < this.entityHeight; dy++) {
/* 412 */         for (int dz = 0; dz < this.entityDepth; dz++) {
/* 413 */           int xx = dx + x;
/* 414 */           int yy = dy + y;
/* 415 */           int zz = dz + z;
/*     */           
/* 417 */           PathType blockType = getPathType(context, xx, yy, zz);
/*     */           
/* 419 */           BlockPos mobPosition = this.mob.blockPosition();
/* 420 */           boolean canPassDoors = canPassDoors();
/* 421 */           if (blockType == PathType.DOOR_WOOD_CLOSED && canOpenDoors() && canPassDoors) {
/* 422 */             blockType = PathType.WALKABLE_DOOR;
/*     */           }
/* 424 */           if (blockType == PathType.DOOR_OPEN && !canPassDoors) {
/* 425 */             blockType = PathType.BLOCKED;
/*     */           }
/* 427 */           if (blockType == PathType.RAIL && getPathType(context, mobPosition.getX(), mobPosition.getY(), mobPosition.getZ()) != PathType.RAIL && getPathType(context, mobPosition.getX(), mobPosition.getY() - true, mobPosition.getZ()) != PathType.RAIL) {
/* 428 */             blockType = PathType.UNPASSABLE_RAIL;
/*     */           }
/*     */           
/* 431 */           blockTypes.add(blockType);
/*     */         } 
/*     */       } 
/*     */     } 
/* 435 */     return blockTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 440 */   public PathType getPathType(PathfindingContext context, int x, int y, int z) { return getPathTypeStatic(context, new BlockPos.MutableBlockPos(x, y, z)); }
/*     */ 
/*     */ 
/*     */   
/* 444 */   public static PathType getPathTypeStatic(Mob mob, BlockPos pos) { return getPathTypeStatic(new PathfindingContext(mob.level(), mob), pos.mutable()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathType getPathTypeStatic(PathfindingContext context, BlockPos.MutableBlockPos pos) {
/* 455 */     int x = pos.getX();
/* 456 */     int y = pos.getY();
/* 457 */     int z = pos.getZ();
/*     */     
/* 459 */     PathType blockPathType = context.getPathTypeFromState(x, y, z);
/* 460 */     if (blockPathType != PathType.OPEN || y < context.level().getMinY() + 1) {
/* 461 */       return blockPathType;
/*     */     }
/*     */     
/* 464 */     switch (context.getPathTypeFromState(x, y - 1, z)) { case OPEN: case WATER: case LAVA: case WALKABLE: case DAMAGE_FIRE: case DAMAGE_OTHER: case STICKY_HONEY: case POWDER_SNOW: case DAMAGE_CAUTIOUS: case TRAPDOOR:  }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 474 */       checkNeighbourBlocks(context, x, y, z, PathType.WALKABLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static PathType checkNeighbourBlocks(PathfindingContext context, int x, int y, int z, PathType blockPathType) {
/* 479 */     for (int dx = -1; dx <= 1; dx++) {
/* 480 */       for (int dy = -1; dy <= 1; dy++) {
/* 481 */         for (int dz = -1; dz <= 1; dz++) {
/* 482 */           if (dx != 0 || dz != 0) {
/* 483 */             PathType pathType = context.getPathTypeFromState(x + dx, y + dy, z + dz);
/*     */             
/* 485 */             if (pathType == PathType.DAMAGE_OTHER)
/* 486 */               return PathType.DANGER_OTHER; 
/* 487 */             if (pathType == PathType.DAMAGE_FIRE || pathType == PathType.LAVA)
/* 488 */               return PathType.DANGER_FIRE; 
/* 489 */             if (pathType == PathType.WATER)
/* 490 */               return PathType.WATER_BORDER; 
/* 491 */             if (pathType == PathType.DAMAGE_CAUTIOUS) {
/* 492 */               return PathType.DAMAGE_CAUTIOUS;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 498 */     return blockPathType;
/*     */   }
/*     */   
/*     */   protected static PathType getPathTypeFromState(BlockGetter level, BlockPos pos) {
/* 502 */     BlockState blockState = level.getBlockState(pos);
/* 503 */     Block block = blockState.getBlock();
/*     */     
/* 505 */     if (blockState.isAir()) {
/* 506 */       return PathType.OPEN;
/*     */     }
/*     */     
/* 509 */     if (blockState.is(BlockTags.TRAPDOORS) || blockState.is(Blocks.LILY_PAD) || blockState.is(Blocks.BIG_DRIPLEAF)) {
/* 510 */       return PathType.TRAPDOOR;
/*     */     }
/*     */     
/* 513 */     if (blockState.is(Blocks.POWDER_SNOW)) {
/* 514 */       return PathType.POWDER_SNOW;
/*     */     }
/*     */     
/* 517 */     if (blockState.is(Blocks.CACTUS) || blockState.is(Blocks.SWEET_BERRY_BUSH)) {
/* 518 */       return PathType.DAMAGE_OTHER;
/*     */     }
/*     */     
/* 521 */     if (blockState.is(Blocks.HONEY_BLOCK)) {
/* 522 */       return PathType.STICKY_HONEY;
/*     */     }
/*     */     
/* 525 */     if (blockState.is(Blocks.COCOA)) {
/* 526 */       return PathType.COCOA;
/*     */     }
/*     */     
/* 529 */     if (blockState.is(Blocks.WITHER_ROSE) || blockState.is(Blocks.POINTED_DRIPSTONE)) {
/* 530 */       return PathType.DAMAGE_CAUTIOUS;
/*     */     }
/*     */     
/* 533 */     FluidState fluidState = blockState.getFluidState();
/* 534 */     if (fluidState.is(FluidTags.LAVA)) {
/* 535 */       return PathType.LAVA;
/*     */     }
/*     */     
/* 538 */     if (isBurningBlock(blockState)) {
/* 539 */       return PathType.DAMAGE_FIRE;
/*     */     }
/*     */     
/* 542 */     if (block instanceof DoorBlock) { DoorBlock door = (DoorBlock)block;
/* 543 */       if (((Boolean)blockState.getValue(DoorBlock.OPEN)).booleanValue()) {
/* 544 */         return PathType.DOOR_OPEN;
/*     */       }
/* 546 */       return door.type().canOpenByHand() ? PathType.DOOR_WOOD_CLOSED : PathType.DOOR_IRON_CLOSED; }
/*     */ 
/*     */     
/* 549 */     if (block instanceof net.minecraft.world.level.block.BaseRailBlock) {
/* 550 */       return PathType.RAIL;
/*     */     }
/*     */     
/* 553 */     if (block instanceof net.minecraft.world.level.block.LeavesBlock) {
/* 554 */       return PathType.LEAVES;
/*     */     }
/*     */     
/* 557 */     if (blockState.is(BlockTags.FENCES) || blockState.is(BlockTags.WALLS) || (block instanceof FenceGateBlock && !((Boolean)blockState.getValue(FenceGateBlock.OPEN)).booleanValue())) {
/* 558 */       return PathType.FENCE;
/*     */     }
/*     */ 
/*     */     
/* 562 */     if (!blockState.isPathfindable(PathComputationType.LAND)) {
/* 563 */       return PathType.BLOCKED;
/*     */     }
/*     */     
/* 566 */     if (fluidState.is(FluidTags.WATER)) {
/* 567 */       return PathType.WATER;
/*     */     }
/*     */     
/* 570 */     return PathType.OPEN;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\WalkNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */