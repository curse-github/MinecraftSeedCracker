/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElytraAnimationState
/*    */ {
/*    */   private static final float DEFAULT_X_ROT = 0.2617994F;
/*    */   private static final float DEFAULT_Z_ROT = -0.2617994F;
/*    */   private float rotX;
/*    */   private float rotY;
/*    */   private float rotZ;
/*    */   private float rotXOld;
/*    */   private float rotYOld;
/*    */   private float rotZOld;
/*    */   private final LivingEntity entity;
/*    */   
/* 20 */   public ElytraAnimationState(LivingEntity entity) { this.entity = entity; }
/*    */   
/*    */   public void tick() {
/*    */     float targetYRot, targetZRot, targetXRot;
/* 24 */     this.rotXOld = this.rotX;
/* 25 */     this.rotYOld = this.rotY;
/* 26 */     this.rotZOld = this.rotZ;
/*    */ 
/*    */ 
/*    */     
/* 30 */     if (this.entity.isFallFlying()) {
/*    */       
/* 32 */       float ratio = 1.0F;
/* 33 */       Vec3 movement = this.entity.getDeltaMovement();
/* 34 */       if (movement.y < 0.0D) {
/* 35 */         Vec3 vec = movement.normalize();
/* 36 */         ratio = 1.0F - (float)Math.pow(-vec.y, 1.5D);
/*    */       } 
/*    */       
/* 39 */       targetXRot = Mth.lerp(ratio, 0.2617994F, 0.34906584F);
/* 40 */       targetZRot = Mth.lerp(ratio, -0.2617994F, -1.5707964F);
/* 41 */       targetYRot = 0.0F;
/* 42 */     } else if (this.entity.isCrouching()) {
/* 43 */       targetXRot = 0.6981317F;
/* 44 */       targetZRot = -0.7853982F;
/* 45 */       targetYRot = 0.08726646F;
/*    */     } else {
/* 47 */       targetXRot = 0.2617994F;
/* 48 */       targetZRot = -0.2617994F;
/* 49 */       targetYRot = 0.0F;
/*    */     } 
/* 51 */     this.rotX += (targetXRot - this.rotX) * 0.3F;
/* 52 */     this.rotY += (targetYRot - this.rotY) * 0.3F;
/* 53 */     this.rotZ += (targetZRot - this.rotZ) * 0.3F;
/*    */   }
/*    */ 
/*    */   
/* 57 */   public float getRotX(float partialTicks) { return Mth.lerp(partialTicks, this.rotXOld, this.rotX); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public float getRotY(float partialTicks) { return Mth.lerp(partialTicks, this.rotYOld, this.rotY); }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public float getRotZ(float partialTicks) { return Mth.lerp(partialTicks, this.rotZOld, this.rotZ); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ElytraAnimationState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */