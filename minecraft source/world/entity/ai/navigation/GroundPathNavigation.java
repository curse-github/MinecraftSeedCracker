/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class GroundPathNavigation
/*     */   extends PathNavigation {
/*     */   private boolean avoidSun;
/*     */   private boolean canPathToTargetsBelowSurface;
/*     */   
/*  25 */   public GroundPathNavigation(Mob mob, Level level) { super(mob, level); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/*  30 */     this.nodeEvaluator = new WalkNodeEvaluator();
/*  31 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  36 */   protected boolean canUpdatePath() { return (this.mob.onGround() || this.mob.isInLiquid() || this.mob.isPassenger()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   protected Vec3 getTempMobPos() { return new Vec3(this.mob.getX(), getSurfaceY(), this.mob.getZ()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath(BlockPos pos, int reachRange) {
/*  46 */     LevelChunk chunk = this.level.getChunkSource().getChunkNow(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*  47 */     if (chunk == null) {
/*  48 */       return null;
/*     */     }
/*     */     
/*  51 */     if (!this.canPathToTargetsBelowSurface) {
/*  52 */       pos = findSurfacePosition(chunk, pos, reachRange);
/*     */     }
/*     */     
/*  55 */     return super.createPath(pos, reachRange);
/*     */   }
/*     */   final BlockPos findSurfacePosition(LevelChunk chunk, BlockPos pos, int reachRange) {
/*     */     BlockPos.MutableBlockPos mutableBlockPos;
/*  59 */     if (chunk.getBlockState(pos).isAir()) {
/*  60 */       BlockPos.MutableBlockPos columnPos = pos.mutable().move(Direction.DOWN);
/*  61 */       while (columnPos.getY() >= this.level.getMinY() && chunk.getBlockState(columnPos).isAir()) {
/*  62 */         columnPos.move(Direction.DOWN);
/*     */       }
/*     */       
/*  65 */       if (columnPos.getY() >= this.level.getMinY()) {
/*  66 */         return columnPos.above();
/*     */       }
/*     */       
/*  69 */       columnPos.setY(pos.getY() + 1);
/*  70 */       while (columnPos.getY() <= this.level.getMaxY() && chunk.getBlockState(columnPos).isAir()) {
/*  71 */         columnPos.move(Direction.UP);
/*     */       }
/*  73 */       mutableBlockPos = columnPos;
/*     */     } 
/*     */     
/*  76 */     if (chunk.getBlockState(mutableBlockPos).isSolid()) {
/*  77 */       BlockPos.MutableBlockPos columnPos = mutableBlockPos.mutable().move(Direction.UP);
/*  78 */       while (columnPos.getY() <= this.level.getMaxY() && chunk.getBlockState(columnPos).isSolid()) {
/*  79 */         columnPos.move(Direction.UP);
/*     */       }
/*  81 */       return columnPos.immutable();
/*     */     } 
/*  83 */     return mutableBlockPos;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  88 */   public Path createPath(Entity target, int reachRange) { return createPath(target.blockPosition(), reachRange); }
/*     */ 
/*     */   
/*     */   private int getSurfaceY() {
/*  92 */     if (!this.mob.isInWater() || !canFloat()) {
/*  93 */       return Mth.floor(this.mob.getY() + 0.5D);
/*     */     }
/*     */ 
/*     */     
/*  97 */     int surface = this.mob.getBlockY();
/*  98 */     BlockState state = this.level.getBlockState(BlockPos.containing(this.mob.getX(), surface, this.mob.getZ()));
/*  99 */     int steps = 0;
/* 100 */     while (state.is(Blocks.WATER)) {
/* 101 */       surface++;
/* 102 */       state = this.level.getBlockState(BlockPos.containing(this.mob.getX(), surface, this.mob.getZ()));
/* 103 */       if (++steps > 16) {
/* 104 */         return this.mob.getBlockY();
/*     */       }
/*     */     } 
/* 107 */     return surface;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void trimPath() {
/* 112 */     super.trimPath();
/*     */     
/* 114 */     if (this.avoidSun) {
/* 115 */       if (this.level.canSeeSky(BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5D, this.mob.getZ()))) {
/*     */         return;
/*     */       }
/*     */       
/* 119 */       for (int i = 0; i < this.path.getNodeCount(); i++) {
/* 120 */         Node node = this.path.getNode(i);
/* 121 */         if (this.level.canSeeSky(new BlockPos(node.x, node.y, node.z))) {
/* 122 */           this.path.truncateNodes(i);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public boolean canNavigateGround() { return true; }
/*     */ 
/*     */   
/*     */   protected boolean hasValidPathType(PathType pathType) {
/* 135 */     if (pathType == PathType.WATER) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (pathType == PathType.LAVA) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     if (pathType == PathType.OPEN) {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 151 */   public void setAvoidSun(boolean avoidSun) { this.avoidSun = avoidSun; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public void setCanWalkOverFences(boolean canWalkOverFences) { this.nodeEvaluator.setCanWalkOverFences(canWalkOverFences); }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void setCanPathToTargetsBelowSurface(boolean canPathToTargetsBelowSurface) { this.canPathToTargetsBelowSurface = canPathToTargetsBelowSurface; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\navigation\GroundPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */