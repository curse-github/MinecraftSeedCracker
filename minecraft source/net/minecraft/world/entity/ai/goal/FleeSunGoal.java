/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FleeSunGoal
/*    */   extends Goal
/*    */ {
/*    */   protected final PathfinderMob mob;
/*    */   private double wantedX;
/*    */   private double wantedY;
/*    */   private double wantedZ;
/*    */   private final double speedModifier;
/*    */   private final Level level;
/*    */   
/*    */   public FleeSunGoal(PathfinderMob mob, double speedModifier) {
/* 22 */     this.mob = mob;
/* 23 */     this.speedModifier = speedModifier;
/* 24 */     this.level = mob.level();
/* 25 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 30 */     if (this.mob.getTarget() != null) {
/* 31 */       return false;
/*    */     }
/* 33 */     if (!this.level.isBrightOutside()) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!this.mob.isOnFire()) {
/* 37 */       return false;
/*    */     }
/* 39 */     if (!this.level.canSeeSky(this.mob.blockPosition())) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     return setWantedPos();
/*    */   }
/*    */   
/*    */   protected boolean setWantedPos() {
/* 50 */     Vec3 pos = getHidePos();
/* 51 */     if (pos == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     this.wantedX = pos.x;
/* 55 */     this.wantedY = pos.y;
/* 56 */     this.wantedZ = pos.z;
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 62 */   public boolean canContinueToUse() { return !this.mob.getNavigation().isDone(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   public void start() { this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier); }
/*    */ 
/*    */   
/*    */   protected Vec3 getHidePos() {
/* 71 */     RandomSource random = this.mob.getRandom();
/* 72 */     BlockPos pos = this.mob.blockPosition();
/*    */     
/* 74 */     for (int i = 0; i < 10; i++) {
/* 75 */       BlockPos randomPos = pos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
/*    */       
/* 77 */       if (!this.level.canSeeSky(randomPos) && this.mob.getWalkTargetValue(randomPos) < 0.0F) {
/* 78 */         return Vec3.atBottomCenterOf(randomPos);
/*    */       }
/*    */     } 
/* 81 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FleeSunGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */