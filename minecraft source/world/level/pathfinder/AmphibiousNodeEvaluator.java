/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ public class AmphibiousNodeEvaluator
/*     */   extends WalkNodeEvaluator
/*     */ {
/*     */   private final boolean prefersShallowSwimming;
/*     */   private float oldWalkableCost;
/*     */   private float oldWaterBorderCost;
/*     */   
/*  16 */   public AmphibiousNodeEvaluator(boolean prefersShallowSwimming) { this.prefersShallowSwimming = prefersShallowSwimming; }
/*     */ 
/*     */ 
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  21 */     super.prepare(level, entity);
/*  22 */     entity.setPathfindingMalus(PathType.WATER, 0.0F);
/*  23 */     this.oldWalkableCost = entity.getPathfindingMalus(PathType.WALKABLE);
/*  24 */     entity.setPathfindingMalus(PathType.WALKABLE, 6.0F);
/*  25 */     this.oldWaterBorderCost = entity.getPathfindingMalus(PathType.WATER_BORDER);
/*  26 */     entity.setPathfindingMalus(PathType.WATER_BORDER, 4.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  31 */     this.mob.setPathfindingMalus(PathType.WALKABLE, this.oldWalkableCost);
/*  32 */     this.mob.setPathfindingMalus(PathType.WATER_BORDER, this.oldWaterBorderCost);
/*  33 */     super.done();
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getStart() {
/*  38 */     if (!this.mob.isInWater()) {
/*  39 */       return super.getStart();
/*     */     }
/*  41 */     return getStartNode(new BlockPos(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public Target getTarget(double x, double y, double z) { return getTargetNodeAt(x, y + 0.5D, z); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] neighbors, Node pos) {
/*  53 */     int jumpSize, numValidNeighbors = super.getNeighbors(neighbors, pos);
/*     */ 
/*     */     
/*  56 */     PathType blockPathTypeAbove = getCachedPathType(pos.x, pos.y + 1, pos.z);
/*  57 */     PathType blockPathTypeCurrent = getCachedPathType(pos.x, pos.y, pos.z);
/*     */     
/*  59 */     if (this.mob.getPathfindingMalus(blockPathTypeAbove) >= 0.0F && blockPathTypeCurrent != PathType.STICKY_HONEY) {
/*  60 */       jumpSize = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
/*     */     } else {
/*  62 */       jumpSize = 0;
/*     */     } 
/*     */     
/*  65 */     double posHeight = getFloorLevel(new BlockPos(pos.x, pos.y, pos.z));
/*     */     
/*  67 */     Node upNode = findAcceptedNode(pos.x, pos.y + 1, pos.z, Math.max(0, jumpSize - 1), posHeight, Direction.UP, blockPathTypeCurrent);
/*  68 */     Node downNode = findAcceptedNode(pos.x, pos.y - 1, pos.z, jumpSize, posHeight, Direction.DOWN, blockPathTypeCurrent);
/*     */     
/*  70 */     if (isVerticalNeighborValid(upNode, pos)) {
/*  71 */       neighbors[numValidNeighbors++] = upNode;
/*     */     }
/*     */     
/*  74 */     if (isVerticalNeighborValid(downNode, pos) && blockPathTypeCurrent != PathType.TRAPDOOR) {
/*  75 */       neighbors[numValidNeighbors++] = downNode;
/*     */     }
/*     */ 
/*     */     
/*  79 */     for (int i = 0; i < numValidNeighbors; i++) {
/*  80 */       Node neighbor = neighbors[i];
/*  81 */       if (neighbor.type == PathType.WATER && this.prefersShallowSwimming && neighbor.y < this.mob.level().getSeaLevel() - 10) {
/*  82 */         neighbor.costMalus++;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return numValidNeighbors;
/*     */   }
/*     */ 
/*     */   
/*  90 */   private boolean isVerticalNeighborValid(Node verticalNode, Node pos) { return (isNeighborValid(verticalNode, pos) && verticalNode.type == PathType.WATER); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   protected boolean isAmphibious() { return true; }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 100 */     PathType blockPathType = context.getPathTypeFromState(x, y, z);
/*     */     
/* 102 */     if (blockPathType == PathType.WATER) {
/* 103 */       BlockPos.MutableBlockPos reusablePos = new BlockPos.MutableBlockPos();
/* 104 */       for (Direction direction : Direction.values()) {
/* 105 */         reusablePos.set(x, y, z).move(direction);
/* 106 */         PathType pathType = context.getPathTypeFromState(reusablePos.getX(), reusablePos.getY(), reusablePos.getZ());
/* 107 */         if (pathType == PathType.BLOCKED) {
/* 108 */           return PathType.WATER_BORDER;
/*     */         }
/*     */       } 
/*     */       
/* 112 */       return PathType.WATER;
/*     */     } 
/*     */     
/* 115 */     return super.getPathType(context, x, y, z);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\AmphibiousNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */