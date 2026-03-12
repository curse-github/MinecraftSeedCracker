/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class WalkAnimationState {
/*    */   private float speedOld;
/*    */   private float speed;
/*    */   private float position;
/*  9 */   private float positionScale = 1.0F;
/*    */ 
/*    */   
/* 12 */   public void setSpeed(float speed) { this.speed = speed; }
/*    */ 
/*    */   
/*    */   public void update(float targetSpeed, float factor, float positionScale) {
/* 16 */     this.speedOld = this.speed;
/* 17 */     this.speed += (targetSpeed - this.speed) * factor;
/* 18 */     this.position += this.speed;
/* 19 */     this.positionScale = positionScale;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 23 */     this.speedOld = 0.0F;
/* 24 */     this.speed = 0.0F;
/* 25 */     this.position = 0.0F;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public float speed() { return this.speed; }
/*    */ 
/*    */ 
/*    */   
/* 33 */   public float speed(float partialTicks) { return Math.min(Mth.lerp(partialTicks, this.speedOld, this.speed), 1.0F); }
/*    */ 
/*    */ 
/*    */   
/* 37 */   public float position() { return this.position * this.positionScale; }
/*    */ 
/*    */ 
/*    */   
/* 41 */   public float position(float partialTicks) { return (this.position - this.speed * (1.0F - partialTicks)) * this.positionScale; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public boolean isMoving() { return (this.speed > 1.0E-5F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\WalkAnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */