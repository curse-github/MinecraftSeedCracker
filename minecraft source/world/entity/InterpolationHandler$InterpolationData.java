/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class InterpolationData
/*    */ {
/*    */   protected int steps;
/*    */   Vec3 position;
/*    */   float yRot;
/*    */   float xRot;
/*    */   
/*    */   private InterpolationData(int steps, Vec3 position, float yRot, float xRot) {
/* 22 */     this.steps = steps;
/* 23 */     this.position = position;
/* 24 */     this.yRot = yRot;
/* 25 */     this.xRot = xRot;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public void decrease() { this.steps--; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public void addDelta(Vec3 delta) { this.position = this.position.add(delta); }
/*    */ 
/*    */   
/*    */   public void addRotation(float yRot, float xRot) {
/* 37 */     this.yRot += yRot;
/* 38 */     this.xRot += xRot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\InterpolationHandler$InterpolationData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */