/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ public class PathFinder
/*     */ {
/*     */   private static final float FUDGING = 1.5F;
/*     */   private final Node[] neighbors;
/*     */   private int maxVisitedNodes;
/*     */   
/*     */   public PathFinder(NodeEvaluator nodeEvaluator, int maxVisitedNodes) {
/*  28 */     this.neighbors = new Node[32];
/*     */ 
/*     */ 
/*     */     
/*  32 */     this.openSet = new BinaryHeap();
/*  33 */     this.captureDebug = (() -> false);
/*     */ 
/*     */     
/*  36 */     this.nodeEvaluator = nodeEvaluator;
/*  37 */     this.maxVisitedNodes = maxVisitedNodes;
/*     */   }
/*     */   private final NodeEvaluator nodeEvaluator; private final BinaryHeap openSet; private BooleanSupplier captureDebug;
/*     */   
/*  41 */   public void setCaptureDebug(BooleanSupplier captureDebug) { this.captureDebug = captureDebug; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public void setMaxVisitedNodes(int maxVisitedNodes) { this.maxVisitedNodes = maxVisitedNodes; }
/*     */ 
/*     */   
/*     */   public Path findPath(PathNavigationRegion level, Mob entity, Set<BlockPos> targets, float maxPathLength, int reachRange, float maxVisitedNodesMultiplier) {
/*  49 */     this.openSet.clear();
/*  50 */     this.nodeEvaluator.prepare(level, entity);
/*  51 */     Node from = this.nodeEvaluator.getStart();
/*  52 */     if (from == null) {
/*  53 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  57 */     Map<Target, BlockPos> tos = (Map)targets.stream().collect(Collectors.toMap(pos -> this.nodeEvaluator.getTarget(pos.getX(), pos.getY(), pos.getZ()), Function.identity()));
/*  58 */     Path path = findPath(from, tos, maxPathLength, reachRange, maxVisitedNodesMultiplier);
/*     */     
/*  60 */     this.nodeEvaluator.done();
/*  61 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path findPath(Node from, Map<Target, BlockPos> targetMap, float maxPathLength, int reachRange, float maxVisitedNodesMultiplier) {
/*  70 */     ProfilerFiller profiler = Profiler.get();
/*  71 */     profiler.push("find_path");
/*  72 */     profiler.markForCharting(MetricCategory.PATH_FINDING);
/*  73 */     Set<Target> targets = targetMap.keySet();
/*     */     
/*  75 */     from.g = 0.0F;
/*  76 */     from.h = getBestH(from, targets);
/*  77 */     from.f = from.h;
/*     */     
/*  79 */     this.openSet.clear();
/*  80 */     this.openSet.insert(from);
/*     */     
/*  82 */     boolean captureDebug = this.captureDebug.getAsBoolean();
/*  83 */     Set<Node> closedSet = captureDebug ? new HashSet() : Set.of();
/*     */ 
/*     */     
/*  86 */     int count = 0;
/*     */     
/*  88 */     Set<Target> reachedTargets = Sets.newHashSetWithExpectedSize(targets.size());
/*     */     
/*  90 */     int maxVisitedNodesAdjusted = (int)(this.maxVisitedNodes * maxVisitedNodesMultiplier);
/*  91 */     while (!this.openSet.isEmpty() && ++count < maxVisitedNodesAdjusted) {
/*  92 */       Node current = this.openSet.pop();
/*  93 */       current.closed = true;
/*     */ 
/*     */       
/*  96 */       for (Target target : targets) {
/*  97 */         if (current.distanceManhattan(target) <= reachRange) {
/*  98 */           target.setReached();
/*  99 */           reachedTargets.add(target);
/*     */         } 
/*     */       } 
/*     */       
/* 103 */       if (!reachedTargets.isEmpty()) {
/*     */         break;
/*     */       }
/*     */       
/* 107 */       if (captureDebug) {
/* 108 */         closedSet.add(current);
/*     */       }
/*     */       
/* 111 */       if (current.distanceTo(from) >= maxPathLength) {
/*     */         continue;
/*     */       }
/*     */       
/* 115 */       int neighborCount = this.nodeEvaluator.getNeighbors(this.neighbors, current);
/* 116 */       for (int i = 0; i < neighborCount; i++) {
/* 117 */         Node neighbor = this.neighbors[i];
/*     */         
/* 119 */         float distance = distance(current, neighbor);
/* 120 */         current.walkedDistance += distance;
/*     */         
/* 122 */         float tentativeGScore = current.g + distance + neighbor.costMalus;
/* 123 */         if (neighbor.walkedDistance < maxPathLength && (!neighbor.inOpenSet() || tentativeGScore < neighbor.g)) {
/* 124 */           neighbor.cameFrom = current;
/* 125 */           neighbor.g = tentativeGScore;
/* 126 */           neighbor.h = getBestH(neighbor, targets) * 1.5F;
/*     */           
/* 128 */           if (neighbor.inOpenSet()) {
/* 129 */             this.openSet.changeCost(neighbor, neighbor.g + neighbor.h);
/*     */           } else {
/* 131 */             neighbor.f = neighbor.g + neighbor.h;
/* 132 */             this.openSet.insert(neighbor);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     Optional<Path> optPath = !reachedTargets.isEmpty() ? reachedTargets.stream().map(target -> reconstructPath(target.getBestNode(), (BlockPos)targetMap.get(target), true)).min(Comparator.comparingInt(Path::getNodeCount)) : targets.stream().map(target -> reconstructPath(target.getBestNode(), (BlockPos)targetMap.get(target), false)).min(Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
/*     */     
/* 146 */     profiler.pop();
/* 147 */     if (optPath.isEmpty()) {
/* 148 */       return null;
/*     */     }
/* 150 */     Path path = (Path)optPath.get();
/*     */     
/* 152 */     if (captureDebug) {
/* 153 */       path.setDebug(this.openSet.getHeap(), (Node[])closedSet.toArray(x$0 -> new Node[x$0]), targets);
/*     */     }
/* 155 */     return path;
/*     */   }
/*     */ 
/*     */   
/* 159 */   protected float distance(Node from, Node to) { return from.distanceTo(to); }
/*     */ 
/*     */ 
/*     */   
/*     */   private float getBestH(Node from, Set<Target> targets) {
/* 164 */     float bestH = Float.MAX_VALUE;
/* 165 */     for (Target target : targets) {
/* 166 */       float h = from.distanceTo(target);
/* 167 */       target.updateBest(h, from);
/* 168 */       bestH = Math.min(h, bestH);
/*     */     } 
/* 170 */     return bestH;
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node closest, BlockPos target, boolean reached) {
/* 174 */     List<Node> nodes = Lists.newArrayList();
/* 175 */     Node node = closest;
/* 176 */     nodes.add(0, node);
/* 177 */     while (node.cameFrom != null) {
/* 178 */       node = node.cameFrom;
/* 179 */       nodes.add(0, node);
/*     */     } 
/* 181 */     return new Path(nodes, target, reached);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\PathFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */