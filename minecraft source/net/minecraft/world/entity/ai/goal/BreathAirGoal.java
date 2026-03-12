/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.MoverType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreathAirGoal
/*    */   extends Goal
/*    */ {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public BreathAirGoal(PathfinderMob mob) {
/* 20 */     this.mob = mob;
/* 21 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean canUse() { return (this.mob.getAirSupply() < 140); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean canContinueToUse() { return canUse(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public boolean isInterruptable() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public void start() { findAirPosition(); }
/*    */ 
/*    */   
/*    */   private void findAirPosition() {
/* 45 */     Iterable<BlockPos> between = BlockPos.betweenClosed(
/* 46 */         Mth.floor(this.mob.getX() - 1.0D), this.mob
/* 47 */         .getBlockY(), 
/* 48 */         Mth.floor(this.mob.getZ() - 1.0D), 
/* 49 */         Mth.floor(this.mob.getX() + 1.0D), 
/* 50 */         Mth.floor(this.mob.getY() + 8.0D), 
/* 51 */         Mth.floor(this.mob.getZ() + 1.0D));
/*    */ 
/*    */     
/* 54 */     BlockPos destinationPos = null;
/* 55 */     for (BlockPos pos : between) {
/* 56 */       if (givesAir(this.mob.level(), pos)) {
/* 57 */         destinationPos = pos;
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 62 */     if (destinationPos == null) {
/* 63 */       destinationPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 8.0D, this.mob.getZ());
/*    */     }
/*    */     
/* 66 */     this.mob.getNavigation().moveTo(destinationPos.getX(), (destinationPos.getY() + 1), destinationPos.getZ(), 1.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 71 */     findAirPosition();
/*    */     
/* 73 */     this.mob.moveRelative(0.02F, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/* 74 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*    */   }
/*    */   
/*    */   private boolean givesAir(LevelReader level, BlockPos pos) {
/* 78 */     BlockState state = level.getBlockState(pos);
/* 79 */     return ((level.getFluidState(pos).isEmpty() || state.is(Blocks.BUBBLE_COLUMN)) && state.isPathfindable(PathComputationType.LAND));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\BreathAirGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */