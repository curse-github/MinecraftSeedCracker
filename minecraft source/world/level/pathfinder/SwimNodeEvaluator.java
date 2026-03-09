/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class SwimNodeEvaluator extends NodeEvaluator {
/*     */   private final boolean allowBreaching;
/*     */   
/*     */   public SwimNodeEvaluator(boolean allowBreaching) {
/*  20 */     this.pathTypesByPosCache = new Long2ObjectOpenHashMap();
/*     */ 
/*     */     
/*  23 */     this.allowBreaching = allowBreaching;
/*     */   }
/*     */   private final Long2ObjectMap<PathType> pathTypesByPosCache;
/*     */   
/*     */   public void prepare(PathNavigationRegion level, Mob entity) {
/*  28 */     super.prepare(level, entity);
/*  29 */     this.pathTypesByPosCache.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/*  34 */     super.done();
/*  35 */     this.pathTypesByPosCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  40 */   public Node getStart() { return getNode(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY + 0.5D), Mth.floor((this.mob.getBoundingBox()).minZ)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   public Target getTarget(double x, double y, double z) { return getTargetNodeAt(x, y, z); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNeighbors(Node[] neighbors, Node pos) {
/*  50 */     int count = 0;
/*     */     
/*  52 */     Map<Direction, Node> nodes = Maps.newEnumMap(Direction.class);
/*     */     
/*  54 */     for (Direction direction : Direction.values()) {
/*  55 */       Node node = findAcceptedNode(pos.x + direction.getStepX(), pos.y + direction.getStepY(), pos.z + direction.getStepZ());
/*  56 */       nodes.put(direction, node);
/*  57 */       if (isNodeValid(node)) {
/*  58 */         neighbors[count++] = node;
/*     */       }
/*     */     } 
/*     */     
/*  62 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  63 */       Direction secondDirection = direction.getClockWise();
/*  64 */       if (hasMalus((Node)nodes.get(direction)) && hasMalus((Node)nodes.get(secondDirection))) {
/*  65 */         Node diagonalNode = findAcceptedNode(pos.x + direction.getStepX() + secondDirection.getStepX(), pos.y, pos.z + direction.getStepZ() + secondDirection.getStepZ());
/*  66 */         if (isNodeValid(diagonalNode)) {
/*  67 */           neighbors[count++] = diagonalNode;
/*     */         }
/*     */       } 
/*     */     } 
/*  71 */     return count;
/*     */   }
/*     */ 
/*     */   
/*  75 */   protected boolean isNodeValid(Node node) { return (node != null && !node.closed); }
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static boolean hasMalus(Node node) { return (node != null && node.costMalus >= 0.0F); }
/*     */ 
/*     */   
/*     */   protected Node findAcceptedNode(int x, int y, int z) {
/*  83 */     Node best = null;
/*  84 */     PathType pathType = getCachedBlockType(x, y, z);
/*     */     
/*  86 */     if ((this.allowBreaching && pathType == PathType.BREACH) || pathType == PathType.WATER) {
/*  87 */       float pathCost = this.mob.getPathfindingMalus(pathType);
/*     */ 
/*     */       
/*  90 */       best = getNode(x, y, z);
/*  91 */       best.type = pathType;
/*  92 */       best.costMalus = Math.max(best.costMalus, pathCost);
/*     */       
/*  94 */       if (pathCost >= 0.0F && this.currentContext.level().getFluidState(new BlockPos(x, y, z)).isEmpty()) {
/*  95 */         best.costMalus += 8.0F;
/*     */       }
/*     */     } 
/*     */     
/*  99 */     return best;
/*     */   }
/*     */ 
/*     */   
/* 103 */   protected PathType getCachedBlockType(int x, int y, int z) { return (PathType)this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(x, y, z), k -> getPathType(this.currentContext, x, y, z)); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   public PathType getPathType(PathfindingContext context, int x, int y, int z) { return getPathTypeOfMob(context, x, y, z, this.mob); }
/*     */ 
/*     */ 
/*     */   
/*     */   public PathType getPathTypeOfMob(PathfindingContext context, int x, int y, int z, Mob mob) {
/* 113 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 114 */     for (int xx = x; xx < x + this.entityWidth; xx++) {
/* 115 */       for (int yy = y; yy < y + this.entityHeight; yy++) {
/* 116 */         for (int zz = z; zz < z + this.entityDepth; zz++) {
/* 117 */           BlockState blockState = context.getBlockState(pos.set(xx, yy, zz));
/* 118 */           FluidState fluidState = blockState.getFluidState();
/*     */           
/* 120 */           if (fluidState.isEmpty() && blockState.isPathfindable(PathComputationType.WATER) && blockState.isAir())
/* 121 */             return PathType.BREACH; 
/* 122 */           if (!fluidState.is(FluidTags.WATER)) {
/* 123 */             return PathType.BLOCKED;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 129 */     BlockState blockState = context.getBlockState(pos);
/*     */ 
/*     */     
/* 132 */     if (blockState.isPathfindable(PathComputationType.WATER)) {
/* 133 */       return PathType.WATER;
/*     */     }
/*     */     
/* 136 */     return PathType.BLOCKED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\SwimNodeEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */