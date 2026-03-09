/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ 
/*    */ public class SmoothSwimmingMoveControl
/*    */   extends MoveControl {
/*    */   private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;
/*    */   private static final float STOP_TURN_THRESHOLD = 60.0F;
/*    */   private final int maxTurnX;
/*    */   private final int maxTurnY;
/*    */   private final float inWaterSpeedModifier;
/*    */   private final float outsideWaterSpeedModifier;
/*    */   private final boolean applyGravity;
/*    */   
/*    */   public SmoothSwimmingMoveControl(Mob mob, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, float outsideWaterSpeedModifier, boolean applyGravity) {
/* 18 */     super(mob);
/* 19 */     this.maxTurnX = maxTurnX;
/* 20 */     this.maxTurnY = maxTurnY;
/* 21 */     this.inWaterSpeedModifier = inWaterSpeedModifier;
/* 22 */     this.outsideWaterSpeedModifier = outsideWaterSpeedModifier;
/* 23 */     this.applyGravity = applyGravity;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 28 */     if (this.applyGravity && this.mob.isInWater())
/*    */     {
/* 30 */       this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*    */     }
/*    */     
/* 33 */     if (this.operation != MoveControl.Operation.MOVE_TO || this.mob.getNavigation().isDone()) {
/*    */       
/* 35 */       this.mob.setSpeed(0.0F);
/* 36 */       this.mob.setXxa(0.0F);
/* 37 */       this.mob.setYya(0.0F);
/* 38 */       this.mob.setZza(0.0F);
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     double xd = this.wantedX - this.mob.getX();
/* 43 */     double yd = this.wantedY - this.mob.getY();
/* 44 */     double zd = this.wantedZ - this.mob.getZ();
/* 45 */     double dd = xd * xd + yd * yd + zd * zd;
/*    */     
/* 47 */     if (dd < 2.500000277905201E-7D) {
/* 48 */       this.mob.setZza(0.0F);
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 53 */     this.mob.setYRot(rotlerp(this.mob.getYRot(), yRotD, this.maxTurnY));
/* 54 */     this.mob.yBodyRot = this.mob.getYRot();
/* 55 */     this.mob.yHeadRot = this.mob.getYRot();
/*    */     
/* 57 */     float speed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 58 */     if (this.mob.isInWater()) {
/* 59 */       this.mob.setSpeed(speed * this.inWaterSpeedModifier);
/*    */       
/* 61 */       double sqrt = Math.sqrt(xd * xd + zd * zd);
/* 62 */       if (Math.abs(yd) > 9.999999747378752E-6D || Math.abs(sqrt) > 9.999999747378752E-6D) {
/* 63 */         float xRotD = -((float)(Mth.atan2(yd, sqrt) * 57.2957763671875D));
/* 64 */         xRotD = Mth.clamp(Mth.wrapDegrees(xRotD), -this.maxTurnX, this.maxTurnX);
/* 65 */         this.mob.setXRot(rotateTowards(this.mob.getXRot(), xRotD, 5.0F));
/*    */       } 
/*    */       
/* 68 */       float cos = Mth.cos((this.mob.getXRot() * 0.017453292F));
/* 69 */       float sin = Mth.sin((this.mob.getXRot() * 0.017453292F));
/* 70 */       this.mob.zza = cos * speed;
/* 71 */       this.mob.yya = -sin * speed;
/*    */     } else {
/* 73 */       float leftToTurn = Math.abs(Mth.wrapDegrees(this.mob.getYRot() - yRotD));
/* 74 */       float turningSpeedFactor = getTurningSpeedFactor(leftToTurn);
/*    */       
/* 76 */       this.mob.setSpeed(speed * this.outsideWaterSpeedModifier * turningSpeedFactor);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/* 81 */   private static float getTurningSpeedFactor(float leftToTurn) { return 1.0F - Mth.clamp((leftToTurn - 10.0F) / 50.0F, 0.0F, 1.0F); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\SmoothSwimmingMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */