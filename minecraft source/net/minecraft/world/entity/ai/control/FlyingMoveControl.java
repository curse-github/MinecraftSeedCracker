/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ 
/*    */ public class FlyingMoveControl extends MoveControl {
/*    */   private final int maxTurn;
/*    */   private final boolean hoversInPlace;
/*    */   
/*    */   public FlyingMoveControl(Mob mob, int maxTurn, boolean hoversInPlace) {
/* 12 */     super(mob);
/* 13 */     this.maxTurn = maxTurn;
/* 14 */     this.hoversInPlace = hoversInPlace;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 19 */     if (this.operation == MoveControl.Operation.MOVE_TO) {
/* 20 */       float speed; this.operation = MoveControl.Operation.WAIT;
/*    */       
/* 22 */       this.mob.setNoGravity(true);
/*    */       
/* 24 */       double xd = this.wantedX - this.mob.getX();
/* 25 */       double yd = this.wantedY - this.mob.getY();
/* 26 */       double zd = this.wantedZ - this.mob.getZ();
/* 27 */       double dd = xd * xd + yd * yd + zd * zd;
/* 28 */       if (dd < 2.500000277905201E-7D) {
/* 29 */         this.mob.setYya(0.0F);
/* 30 */         this.mob.setZza(0.0F);
/*    */         return;
/*    */       } 
/* 33 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 34 */       this.mob.setYRot(rotlerp(this.mob.getYRot(), yRotD, 90.0F));
/*    */       
/* 36 */       if (this.mob.onGround()) {
/* 37 */         speed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
/*    */       } else {
/* 39 */         speed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
/*    */       } 
/* 41 */       this.mob.setSpeed(speed);
/*    */       
/* 43 */       double sd = Math.sqrt(xd * xd + zd * zd);
/* 44 */       if (Math.abs(yd) > 9.999999747378752E-6D || Math.abs(sd) > 9.999999747378752E-6D) {
/* 45 */         float xRotD = (float)-(Mth.atan2(yd, sd) * 57.2957763671875D);
/* 46 */         this.mob.setXRot(rotlerp(this.mob.getXRot(), xRotD, this.maxTurn));
/* 47 */         this.mob.setYya((yd > 0.0D) ? speed : -speed);
/*    */       } 
/*    */     } else {
/* 50 */       if (!this.hoversInPlace) {
/* 51 */         this.mob.setNoGravity(false);
/*    */       }
/*    */       
/* 54 */       this.mob.setYya(0.0F);
/* 55 */       this.mob.setZza(0.0F);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\control\FlyingMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */