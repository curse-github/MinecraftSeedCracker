/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.util.Mth;
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
/*     */ class VexMoveControl
/*     */   extends MoveControl
/*     */ {
/* 225 */   public VexMoveControl(Vex vex) { super(vex); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 230 */     if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 238 */     Vec3 delta = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
/*     */ 
/*     */     
/* 241 */     double deltaLength = delta.length();
/* 242 */     if (deltaLength < Vex.this.getBoundingBox().getSize()) {
/* 243 */       this.operation = MoveControl.Operation.WAIT;
/* 244 */       Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5D));
/*     */     } else {
/* 246 */       Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(delta.scale(this.speedModifier * 0.05D / deltaLength)));
/*     */       
/* 248 */       if (Vex.this.getTarget() == null) {
/* 249 */         Vec3 movement = Vex.this.getDeltaMovement();
/* 250 */         Vex.this.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 251 */         Vex.this.yBodyRot = Vex.this.getYRot();
/*     */       } else {
/*     */         
/* 254 */         double tx = Vex.this.getTarget().getX() - Vex.this.getX();
/* 255 */         double tz = Vex.this.getTarget().getZ() - Vex.this.getZ();
/* 256 */         Vex.this.setYRot(-((float)Mth.atan2(tx, tz)) * 57.295776F);
/* 257 */         Vex.this.yBodyRot = Vex.this.getYRot();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\Vex$VexMoveControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */