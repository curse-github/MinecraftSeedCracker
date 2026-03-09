/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ public class FlyNodeEvaluator
/*     */   extends WalkNodeEvaluator
/*     */ {
/*  18 */   private final Long2ObjectMap<PathType> pathTypeByPosCache = new Long2ObjectOpenHashMap();
/*     */   
/*     */   private static final float SMALL_MOB_SIZE = 1.0F;
/*     */   private static final float SMALL_MOB_INFLATED_START_NODE_BOUNDING_BOX = 1.1F;
/*     */   private static final int MAX_START_NODE_CANDIDATES = 10;
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  25 */     super.prepare(level, entity);
/*  26 */     this.pathTypeByPosCache.clear();
/*     */     
/*  28 */     entity.onPathfindingStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  33 */     this.mob.onPathfindingDone();
/*     */     
/*  35 */     this.pathTypeByPosCache.clear();
/*  36 */     super.done();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*     */     int startY;
/*  43 */     if (canFloat() && this.mob.isInWater()) {
/*  44 */       startY = this.mob.getBlockY();
/*  45 */       BlockPos.MutableBlockPos reusableBlockPos = new BlockPos.MutableBlockPos(this.mob.getX(), startY, this.mob.getZ());
/*  46 */       BlockState state = this.currentContext.getBlockState(reusableBlockPos);
/*  47 */       while (state.is(Blocks.WATER)) {
/*  48 */         startY++;
/*  49 */         reusableBlockPos.set(this.mob.getX(), startY, this.mob.getZ());
/*  50 */         state = this.currentContext.getBlockState(reusableBlockPos);
/*     */       } 
/*     */     } else {
/*  53 */       startY = Mth.floor(this.mob.getY() + 0.5D);
/*     */     } 
/*     */     
/*  56 */     BlockPos startPos = BlockPos.containing(this.mob.getX(), startY, this.mob.getZ());
/*  57 */     if (!canStartAt(startPos)) {
/*  58 */       for (BlockPos testedPosition : iteratePathfindingStartNodeCandidatePositions(this.mob)) {
/*  59 */         if (canStartAt(testedPosition)) {
/*  60 */           return getStartNode(testedPosition);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*  65 */     return getStartNode(startPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStartAt(BlockPos pos) {
/*  70 */     PathType blockPathType = getCachedPathType(pos.getX(), pos.getY(), pos.getZ());
/*  71 */     return (this.mob.getPathfindingMalus(blockPathType) >= 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  76 */   public Target getTarget(double x, double y, double z) { return getTargetNodeAt(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] neighbors, Node pos) {
/*  81 */     int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     Node south = findAcceptedNode(pos.x, pos.y, pos.z + 1);
/*  87 */     if (isOpen(south)) {
/*  88 */       neighbors[count++] = south;
/*     */     }
/*     */     
/*  91 */     Node west = findAcceptedNode(pos.x - 1, pos.y, pos.z);
/*  92 */     if (isOpen(west)) {
/*  93 */       neighbors[count++] = west;
/*     */     }
/*     */     
/*  96 */     Node east = findAcceptedNode(pos.x + 1, pos.y, pos.z);
/*  97 */     if (isOpen(east)) {
/*  98 */       neighbors[count++] = east;
/*     */     }
/*     */     
/* 101 */     Node north = findAcceptedNode(pos.x, pos.y, pos.z - 1);
/* 102 */     if (isOpen(north)) {
/* 103 */       neighbors[count++] = north;
/*     */     }
/*     */     
/* 106 */     Node up = findAcceptedNode(pos.x, pos.y + 1, pos.z);
/* 107 */     if (isOpen(up)) {
/* 108 */       neighbors[count++] = up;
/*     */     }
/*     */     
/* 111 */     Node down = findAcceptedNode(pos.x, pos.y - 1, pos.z);
/* 112 */     if (isOpen(down)) {
/* 113 */       neighbors[count++] = down;
/*     */     }
/*     */     
/* 116 */     Node southUp = findAcceptedNode(pos.x, pos.y + 1, pos.z + 1);
/* 117 */     if (isOpen(southUp) && hasMalus(south) && hasMalus(up)) {
/* 118 */       neighbors[count++] = southUp;
/*     */     }
/*     */     
/* 121 */     Node westUp = findAcceptedNode(pos.x - 1, pos.y + 1, pos.z);
/* 122 */     if (isOpen(westUp) && hasMalus(west) && hasMalus(up)) {
/* 123 */       neighbors[count++] = westUp;
/*     */     }
/*     */     
/* 126 */     Node eastUp = findAcceptedNode(pos.x + 1, pos.y + 1, pos.z);
/* 127 */     if (isOpen(eastUp) && hasMalus(east) && hasMalus(up)) {
/* 128 */       neighbors[count++] = eastUp;
/*     */     }
/*     */     
/* 131 */     Node northUp = findAcceptedNode(pos.x, pos.y + 1, pos.z - 1);
/* 132 */     if (isOpen(northUp) && hasMalus(north) && hasMalus(up)) {
/* 133 */       neighbors[count++] = northUp;
/*     */     }
/*     */     
/* 136 */     Node southDown = findAcceptedNode(pos.x, pos.y - 1, pos.z + 1);
/* 137 */     if (isOpen(southDown) && hasMalus(south) && hasMalus(down)) {
/* 138 */       neighbors[count++] = southDown;
/*     */     }
/*     */     
/* 141 */     Node westDown = findAcceptedNode(pos.x - 1, pos.y - 1, pos.z);
/* 142 */     if (isOpen(westDown) && hasMalus(west) && hasMalus(down)) {
/* 143 */       neighbors[count++] = westDown;
/*     */     }
/*     */     
/* 146 */     Node eastDown = findAcceptedNode(pos.x + 1, pos.y - 1, pos.z);
/* 147 */     if (isOpen(eastDown) && hasMalus(east) && hasMalus(down)) {
/* 148 */       neighbors[count++] = eastDown;
/*     */     }
/*     */     
/* 151 */     Node northDown = findAcceptedNode(pos.x, pos.y - 1, pos.z - 1);
/* 152 */     if (isOpen(northDown) && hasMalus(north) && hasMalus(down)) {
/* 153 */       neighbors[count++] = northDown;
/*     */     }
/*     */     
/* 156 */     Node northEast = findAcceptedNode(pos.x + 1, pos.y, pos.z - 1);
/* 157 */     if (isOpen(northEast) && hasMalus(north) && hasMalus(east)) {
/* 158 */       neighbors[count++] = northEast;
/*     */     }
/*     */     
/* 161 */     Node southEast = findAcceptedNode(pos.x + 1, pos.y, pos.z + 1);
/* 162 */     if (isOpen(southEast) && hasMalus(south) && hasMalus(east)) {
/* 163 */       neighbors[count++] = southEast;
/*     */     }
/*     */     
/* 166 */     Node northWest = findAcceptedNode(pos.x - 1, pos.y, pos.z - 1);
/* 167 */     if (isOpen(northWest) && hasMalus(north) && hasMalus(west)) {
/* 168 */       neighbors[count++] = northWest;
/*     */     }
/*     */     
/* 171 */     Node southWest = findAcceptedNode(pos.x - 1, pos.y, pos.z + 1);
/* 172 */     if (isOpen(southWest) && hasMalus(south) && hasMalus(west)) {
/* 173 */       neighbors[count++] = southWest;
/*     */     }
/*     */     
/* 176 */     Node northEastUp = findAcceptedNode(pos.x + 1, pos.y + 1, pos.z - 1);
/* 177 */     if (isOpen(northEastUp) && hasMalus(northEast) && hasMalus(north) && hasMalus(east) && hasMalus(up) && hasMalus(northUp) && hasMalus(eastUp)) {
/* 178 */       neighbors[count++] = northEastUp;
/*     */     }
/*     */     
/* 181 */     Node southEastUp = findAcceptedNode(pos.x + 1, pos.y + 1, pos.z + 1);
/* 182 */     if (isOpen(southEastUp) && hasMalus(southEast) && hasMalus(south) && hasMalus(east) && hasMalus(up) && hasMalus(southUp) && hasMalus(eastUp)) {
/* 183 */       neighbors[count++] = southEastUp;
/*     */     }
/*     */     
/* 186 */     Node northWestUp = findAcceptedNode(pos.x - 1, pos.y + 1, pos.z - 1);
/* 187 */     if (isOpen(northWestUp) && hasMalus(northWest) && hasMalus(north) && hasMalus(west) && hasMalus(up) && hasMalus(northUp) && hasMalus(westUp)) {
/* 188 */       neighbors[count++] = northWestUp;
/*     */     }
/*     */     
/* 191 */     Node southWestUp = findAcceptedNode(pos.x - 1, pos.y + 1, pos.z + 1);
/* 192 */     if (isOpen(southWestUp) && hasMalus(southWest) && hasMalus(south) && hasMalus(west) && hasMalus(up) && hasMalus(southUp) && hasMalus(westUp)) {
/* 193 */       neighbors[count++] = southWestUp;
/*     */     }
/*     */     
/* 196 */     Node northEastDown = findAcceptedNode(pos.x + 1, pos.y - 1, pos.z - 1);
/* 197 */     if (isOpen(northEastDown) && hasMalus(northEast) && hasMalus(north) && hasMalus(east) && hasMalus(down) && hasMalus(northDown) && hasMalus(eastDown)) {
/* 198 */       neighbors[count++] = northEastDown;
/*     */     }
/*     */     
/* 201 */     Node southEastDown = findAcceptedNode(pos.x + 1, pos.y - 1, pos.z + 1);
/* 202 */     if (isOpen(southEastDown) && hasMalus(southEast) && hasMalus(south) && hasMalus(east) && hasMalus(down) && hasMalus(southDown) && hasMalus(eastDown)) {
/* 203 */       neighbors[count++] = southEastDown;
/*     */     }
/*     */     
/* 206 */     Node northWestDown = findAcceptedNode(pos.x - 1, pos.y - 1, pos.z - 1);
/* 207 */     if (isOpen(northWestDown) && hasMalus(northWest) && hasMalus(north) && hasMalus(west) && hasMalus(down) && hasMalus(northDown) && hasMalus(westDown)) {
/* 208 */       neighbors[count++] = northWestDown;
/*     */     }
/*     */     
/* 211 */     Node southWestDown = findAcceptedNode(pos.x - 1, pos.y - 1, pos.z + 1);
/* 212 */     if (isOpen(southWestDown) && hasMalus(southWest) && hasMalus(south) && hasMalus(west) && hasMalus(down) && hasMalus(southDown) && hasMalus(westDown)) {
/* 213 */       neighbors[count++] = southWestDown;
/*     */     }
/*     */     
/* 216 */     return count;
/*     */   }
/*     */ 
/*     */   
/* 220 */   private boolean hasMalus(Node node) { return (node != null && node.costMalus >= 0.0F); }
/*     */ 
/*     */ 
/*     */   
/* 224 */   private boolean isOpen(Node node) { return (node != null && !node.closed); }
/*     */ 
/*     */   
/*     */   protected Node findAcceptedNode(int x, int y, int z) {
/* 228 */     Node best = null;
/*     */     
/* 230 */     PathType pathType = getCachedPathType(x, y, z);
/*     */     
/* 232 */     float pathCost = this.mob.getPathfindingMalus(pathType);
/*     */     
/* 234 */     if (pathCost >= 0.0F) {
/* 235 */       best = getNode(x, y, z);
/* 236 */       best.type = pathType;
/* 237 */       best.costMalus = Math.max(best.costMalus, pathCost);
/*     */       
/* 239 */       if (pathType == PathType.WALKABLE) {
/* 240 */         best.costMalus++;
/*     */       }
/*     */     } 
/*     */     
/* 244 */     return best;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 249 */   protected PathType getCachedPathType(int x, int y, int z) { return (PathType)this.pathTypeByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), key -> getPathTypeOfMob(this.currentContext, x, y, z, this.mob)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 254 */     PathType blockPathType = context.getPathTypeFromState(x, y, z);
/*     */     
/* 256 */     if (blockPathType == PathType.OPEN && y >= context.level().getMinY() + 1) {
/* 257 */       BlockPos belowPos = new BlockPos(x, y - 1, z);
/* 258 */       PathType belowType = context.getPathTypeFromState(belowPos.getX(), belowPos.getY(), belowPos.getZ());
/*     */       
/* 260 */       if (belowType == PathType.DAMAGE_FIRE || belowType == PathType.LAVA) {
/* 261 */         blockPathType = PathType.DAMAGE_FIRE;
/* 262 */       } else if (belowType == PathType.DAMAGE_OTHER) {
/* 263 */         blockPathType = PathType.DAMAGE_OTHER;
/* 264 */       } else if (belowType == PathType.COCOA) {
/* 265 */         blockPathType = PathType.COCOA;
/* 266 */       } else if (belowType == PathType.FENCE) {
/* 267 */         if (!belowPos.equals(context.mobPosition())) {
/* 268 */           blockPathType = PathType.FENCE;
/*     */         }
/*     */       } else {
/*     */         
/* 272 */         blockPathType = (belowType == PathType.WALKABLE || belowType == PathType.OPEN || belowType == PathType.WATER) ? PathType.OPEN : PathType.WALKABLE;
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     if (blockPathType == PathType.WALKABLE || blockPathType == PathType.OPEN) {
/* 277 */       blockPathType = checkNeighbourBlocks(context, x, y, z, blockPathType);
/*     */     }
/*     */     
/* 280 */     return blockPathType;
/*     */   }
/*     */   
/*     */   private Iterable<BlockPos> iteratePathfindingStartNodeCandidatePositions(Mob mob) {
/* 284 */     AABB boundingBox = mob.getBoundingBox();
/* 285 */     boolean isSmallMob = (boundingBox.getSize() < 1.0D);
/* 286 */     if (!isSmallMob) {
/* 287 */       return List.of(
/* 288 */           BlockPos.containing(boundingBox.minX, mob.getBlockY(), boundingBox.minZ), 
/* 289 */           BlockPos.containing(boundingBox.minX, mob.getBlockY(), boundingBox.maxZ), 
/* 290 */           BlockPos.containing(boundingBox.maxX, mob.getBlockY(), boundingBox.minZ), 
/* 291 */           BlockPos.containing(boundingBox.maxX, mob.getBlockY(), boundingBox.maxZ));
/*     */     }
/*     */     
/* 294 */     double zPadding = Math.max(0.0D, 1.100000023841858D - boundingBox.getZsize());
/* 295 */     double xPadding = Math.max(0.0D, 1.100000023841858D - boundingBox.getXsize());
/* 296 */     double yPadding = Math.max(0.0D, 1.100000023841858D - boundingBox.getYsize());
/* 297 */     AABB inflatedBoundingBox = boundingBox.inflate(xPadding, yPadding, zPadding);
/* 298 */     return BlockPos.randomBetweenClosed(mob.getRandom(), 10, 
/* 299 */         Mth.floor(inflatedBoundingBox.minX), Mth.floor(inflatedBoundingBox.minY), Mth.floor(inflatedBoundingBox.minZ), 
/* 300 */         Mth.floor(inflatedBoundingBox.maxX), Mth.floor(inflatedBoundingBox.maxY), Mth.floor(inflatedBoundingBox.maxZ));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\FlyNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */