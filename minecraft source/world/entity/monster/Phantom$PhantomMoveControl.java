/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PhantomMoveControl
/*     */   extends MoveControl
/*     */ {
/* 233 */   private float speed = 0.1F;
/*     */ 
/*     */   
/* 236 */   public PhantomMoveControl(Mob mob) { super(mob); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 241 */     if (Phantom.this.horizontalCollision) {
/*     */       
/* 243 */       Phantom.this.setYRot(Phantom.this.getYRot() + 180.0F);
/* 244 */       this.speed = 0.1F;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     double tdx = this.this$0.moveTargetPoint.x - Phantom.this.getX();
/* 249 */     double tdy = this.this$0.moveTargetPoint.y - Phantom.this.getY();
/* 250 */     double tdz = this.this$0.moveTargetPoint.z - Phantom.this.getZ();
/* 251 */     double sd = Math.sqrt(tdx * tdx + tdz * tdz);
/*     */ 
/*     */     
/* 254 */     if (Math.abs(sd) > 9.999999747378752E-6D) {
/* 255 */       double yRelativeScale = 1.0D - Math.abs(tdy * 0.699999988079071D) / sd;
/* 256 */       tdx *= yRelativeScale;
/* 257 */       tdz *= yRelativeScale;
/* 258 */       sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 259 */       double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
/*     */ 
/*     */       
/* 262 */       float prev = Phantom.this.getYRot();
/* 263 */       float angle = (float)Mth.atan2(tdz, tdx);
/* 264 */       float a = Mth.wrapDegrees(Phantom.this.getYRot() + 90.0F);
/* 265 */       float b = Mth.wrapDegrees(angle * 57.295776F);
/* 266 */       Phantom.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
/* 267 */       Phantom.this.yBodyRot = Phantom.this.getYRot();
/*     */       
/* 269 */       if (Mth.degreesDifferenceAbs(prev, Phantom.this.getYRot()) < 3.0F) {
/* 270 */         this.speed = Mth.approach(this.speed, 1.8F, 0.005F * 1.8F / this.speed);
/*     */       } else {
/* 272 */         this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
/*     */       } 
/*     */       
/* 275 */       float xRotD = (float)-(Mth.atan2(-tdy, sd) * 57.2957763671875D);
/* 276 */       Phantom.this.setXRot(xRotD);
/*     */       
/* 278 */       float moveAngle = Phantom.this.getYRot() + 90.0F;
/* 279 */       double txd = (this.speed * Mth.cos((moveAngle * 0.017453292F))) * Math.abs(tdx / sd2);
/* 280 */       double tzd = (this.speed * Mth.sin((moveAngle * 0.017453292F))) * Math.abs(tdz / sd2);
/* 281 */       double tyd = (this.speed * Mth.sin((xRotD * 0.017453292F))) * Math.abs(tdy / sd2);
/*     */       
/* 283 */       Vec3 movement = Phantom.this.getDeltaMovement();
/* 284 */       Phantom.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd)).subtract(movement).scale(0.2D)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Phantom$PhantomMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */