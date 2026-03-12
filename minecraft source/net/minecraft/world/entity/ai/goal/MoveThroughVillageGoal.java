/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.PoiTypeTags;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MoveThroughVillageGoal
/*     */   extends Goal {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*     */   private Path path;
/*     */   private BlockPos poiPos;
/*     */   
/*     */   public MoveThroughVillageGoal(PathfinderMob mob, double speedModifier, boolean onlyAtNight, int distanceToPoi, BooleanSupplier canDealWithDoors) {
/*  32 */     this.visited = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     this.mob = mob;
/*  38 */     this.speedModifier = speedModifier;
/*  39 */     this.onlyAtNight = onlyAtNight;
/*  40 */     this.distanceToPoi = distanceToPoi;
/*  41 */     this.canDealWithDoors = canDealWithDoors;
/*  42 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     
/*  44 */     if (!GoalUtils.hasGroundPathNavigation(mob))
/*  45 */       throw new IllegalArgumentException("Unsupported mob for MoveThroughVillageGoal"); 
/*     */   }
/*     */   private final boolean onlyAtNight; private final List<BlockPos> visited; private final int distanceToPoi;
/*     */   private final BooleanSupplier canDealWithDoors;
/*     */   
/*     */   public boolean canUse() {
/*  51 */     if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
/*  52 */       return false;
/*     */     }
/*  54 */     updateVisited();
/*     */     
/*  56 */     if (this.onlyAtNight && this.mob.level().isBrightOutside()) {
/*  57 */       return false;
/*     */     }
/*     */     
/*  60 */     ServerLevel level = (ServerLevel)this.mob.level();
/*  61 */     BlockPos pos = this.mob.blockPosition();
/*     */     
/*  63 */     if (!level.isCloseToVillage(pos, 6)) {
/*  64 */       return false;
/*     */     }
/*     */     
/*  67 */     Vec3 landPos = LandRandomPos.getPos(this.mob, 15, 7, p -> {
/*  68 */           if (!level.isVillage(p)) {
/*  69 */             return Double.NEGATIVE_INFINITY;
/*     */           }
/*  71 */           Optional<BlockPos> newPoiPos = level.getPoiManager().find((), this::hasNotVisited, p, 10, PoiManager.Occupancy.IS_OCCUPIED);
/*  72 */           return ((Double)newPoiPos.map(()).orElse(Double.valueOf(Double.NEGATIVE_INFINITY))).doubleValue();
/*     */         });
/*  74 */     if (landPos == null) {
/*  75 */       return false;
/*     */     }
/*  77 */     Optional<BlockPos> target = level.getPoiManager().find(e -> e.is(PoiTypeTags.VILLAGE), this::hasNotVisited, BlockPos.containing(landPos), 10, PoiManager.Occupancy.IS_OCCUPIED);
/*  78 */     if (target.isEmpty()) {
/*  79 */       return false;
/*     */     }
/*  81 */     this.poiPos = ((BlockPos)target.get()).immutable();
/*     */     
/*  83 */     PathNavigation navigation = this.mob.getNavigation();
/*  84 */     navigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  85 */     this.path = navigation.createPath(this.poiPos, 0);
/*  86 */     navigation.setCanOpenDoors(true);
/*  87 */     if (this.path == null) {
/*  88 */       Vec3 partialStep = DefaultRandomPos.getPosTowards(this.mob, 10, 7, Vec3.atBottomCenterOf(this.poiPos), 1.5707963705062866D);
/*  89 */       if (partialStep == null) {
/*  90 */         return false;
/*     */       }
/*  92 */       navigation.setCanOpenDoors(this.canDealWithDoors.getAsBoolean());
/*  93 */       this.path = this.mob.getNavigation().createPath(partialStep.x, partialStep.y, partialStep.z, 0);
/*  94 */       navigation.setCanOpenDoors(true);
/*  95 */       if (this.path == null) {
/*  96 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 100 */     for (int i = 0; i < this.path.getNodeCount(); i++) {
/* 101 */       Node node = this.path.getNode(i);
/* 102 */       BlockPos doorPos = new BlockPos(node.x, node.y + 1, node.z);
/* 103 */       if (DoorBlock.isWoodenDoor(this.mob.level(), doorPos)) {
/*     */         
/* 105 */         this.path = this.mob.getNavigation().createPath(node.x, node.y, node.z, 0);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 110 */     return (this.path != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 115 */     if (this.mob.getNavigation().isDone()) {
/* 116 */       return false;
/*     */     }
/* 118 */     return !this.poiPos.closerToCenterThan(this.mob.position(), (this.mob.getBbWidth() + this.distanceToPoi));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public void start() { this.mob.getNavigation().moveTo(this.path, this.speedModifier); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 128 */     if (this.mob.getNavigation().isDone() || this.poiPos.closerToCenterThan(this.mob.position(), this.distanceToPoi)) {
/* 129 */       this.visited.add(this.poiPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasNotVisited(BlockPos poi) {
/* 134 */     for (BlockPos visitedPoi : this.visited) {
/* 135 */       if (Objects.equals(poi, visitedPoi)) {
/* 136 */         return false;
/*     */       }
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */   
/*     */   private void updateVisited() {
/* 143 */     if (this.visited.size() > 15)
/* 144 */       this.visited.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\MoveThroughVillageGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */