/*    */ package net.minecraft.world.level.pathfinder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PathfindingContext {
/*    */   private final CollisionGetter level;
/*    */   private final PathTypeCache cache;
/*    */   
/*    */   public PathfindingContext(CollisionGetter level, Mob mob) {
/* 14 */     this.mutablePos = new BlockPos.MutableBlockPos();
/*    */ 
/*    */     
/* 17 */     this.level = level;
/* 18 */     Level level1 = mob.level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 19 */       this.cache = serverLevel.getPathTypeCache(); }
/*    */     else
/* 21 */     { this.cache = null; }
/*    */     
/* 23 */     this.mobPosition = mob.blockPosition();
/*    */   }
/*    */   private final BlockPos mobPosition; private final BlockPos.MutableBlockPos mutablePos;
/*    */   public PathType getPathTypeFromState(int x, int y, int z) {
/* 27 */     BlockPos.MutableBlockPos mutableBlockPos = this.mutablePos.set(x, y, z);
/* 28 */     if (this.cache == null) {
/* 29 */       return WalkNodeEvaluator.getPathTypeFromState(this.level, mutableBlockPos);
/*    */     }
/* 31 */     return this.cache.getOrCompute(this.level, mutableBlockPos);
/*    */   }
/*    */ 
/*    */   
/* 35 */   public BlockState getBlockState(BlockPos pos) { return this.level.getBlockState(pos); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public CollisionGetter level() { return this.level; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public BlockPos mobPosition() { return this.mobPosition; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\PathfindingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */