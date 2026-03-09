/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class SmoothSwimmingLookControl extends LookControl {
/*    */   private final int maxYRotFromCenter;
/*    */   private static final int HEAD_TILT_X = 10;
/*    */   private static final int HEAD_TILT_Y = 20;
/*    */   
/*    */   public SmoothSwimmingLookControl(Mob mob, int maxYRotFromCenter) {
/* 12 */     super(mob);
/* 13 */     this.maxYRotFromCenter = maxYRotFromCenter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.lookAtCooldown > 0) {
/* 19 */       this.lookAtCooldown--;
/*    */       
/* 21 */       getYRotD().ifPresent(yRotD -> this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, yRotD.floatValue() + 20.0F, this.yMaxRotSpeed));
/* 22 */       getXRotD().ifPresent(xRotD -> this.mob.setXRot(rotateTowards(this.mob.getXRot(), xRotD.floatValue() + 10.0F, this.xMaxRotAngle)));
/*    */     } else {
/* 24 */       if (this.mob.getNavigation().isDone()) {
/* 25 */         this.mob.setXRot(rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
/*    */       }
/* 27 */       this.mob.yHeadRot = rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
/*    */     } 
/*    */     
/* 30 */     float headDiffBody = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
/*    */ 
/*    */     
/* 33 */     if (headDiffBody < -this.maxYRotFromCenter) {
/* 34 */       this.mob.yBodyRot -= 4.0F;
/* 35 */     } else if (headDiffBody > this.maxYRotFromCenter) {
/* 36 */       this.mob.yBodyRot += 4.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\SmoothSwimmingLookControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */