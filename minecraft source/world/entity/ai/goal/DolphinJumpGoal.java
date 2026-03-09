/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class DolphinJumpGoal extends JumpGoal {
/*  13 */   private static final int[] STEPS_TO_CHECK = { 0, 1, 4, 5, 6, 7 };
/*     */   
/*     */   private final Dolphin dolphin;
/*     */   
/*     */   private final int interval;
/*     */   
/*     */   private boolean breached;
/*     */   
/*     */   public DolphinJumpGoal(Dolphin dolphin, int interval) {
/*  22 */     this.dolphin = dolphin;
/*  23 */     this.interval = reducedTickDelay(interval);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  28 */     if (this.dolphin.getRandom().nextInt(this.interval) != 0) {
/*  29 */       return false;
/*     */     }
/*     */     
/*  32 */     Direction motion = this.dolphin.getMotionDirection();
/*  33 */     int stepX = motion.getStepX();
/*  34 */     int stepZ = motion.getStepZ();
/*  35 */     BlockPos dolphinPos = this.dolphin.blockPosition();
/*     */     
/*  37 */     for (int i : STEPS_TO_CHECK) {
/*  38 */       if (!waterIsClear(dolphinPos, stepX, stepZ, i) || !surfaceIsClear(dolphinPos, stepX, stepZ, i)) {
/*  39 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  43 */     return true;
/*     */   }
/*     */   
/*     */   private boolean waterIsClear(BlockPos dolphinPos, int stepX, int stepZ, int currentStep) {
/*  47 */     BlockPos nextPos = dolphinPos.offset(stepX * currentStep, 0, stepZ * currentStep);
/*     */     
/*  49 */     return (this.dolphin.level().getFluidState(nextPos).is(FluidTags.WATER) && !this.dolphin.level().getBlockState(nextPos).blocksMotion());
/*     */   }
/*     */   
/*     */   private boolean surfaceIsClear(BlockPos dolphinPos, int stepX, int stepZ, int currentStep) {
/*  53 */     return (this.dolphin.level().getBlockState(dolphinPos.offset(stepX * currentStep, 1, stepZ * currentStep)).isAir() && this.dolphin
/*  54 */       .level().getBlockState(dolphinPos.offset(stepX * currentStep, 2, stepZ * currentStep)).isAir());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  59 */     double yd = (this.dolphin.getDeltaMovement()).y;
/*  60 */     return ((yd * yd >= 0.029999999329447746D || this.dolphin.getXRot() == 0.0F || Math.abs(this.dolphin.getXRot()) >= 10.0F || !this.dolphin.isInWater()) && !this.dolphin.onGround());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  65 */   public boolean isInterruptable() { return false; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  71 */     Direction direction = this.dolphin.getMotionDirection();
/*  72 */     this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(direction
/*  73 */           .getStepX() * 0.6D, 0.7D, direction
/*     */           
/*  75 */           .getStepZ() * 0.6D));
/*     */ 
/*     */     
/*  78 */     this.dolphin.getNavigation().stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public void stop() { this.dolphin.setXRot(0.0F); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/*  88 */     boolean alreadyBreached = this.breached;
/*  89 */     if (!alreadyBreached) {
/*  90 */       FluidState fluidState = this.dolphin.level().getFluidState(this.dolphin.blockPosition());
/*  91 */       this.breached = fluidState.is(FluidTags.WATER);
/*     */     } 
/*     */     
/*  94 */     if (this.breached && !alreadyBreached) {
/*  95 */       this.dolphin.playSound(SoundEvents.DOLPHIN_JUMP, 1.0F, 1.0F);
/*     */     }
/*     */     
/*  98 */     Vec3 movement = this.dolphin.getDeltaMovement();
/*  99 */     if (movement.y * movement.y < 0.029999999329447746D && this.dolphin.getXRot() != 0.0F) {
/* 100 */       this.dolphin.setXRot(Mth.rotLerp(0.2F, this.dolphin.getXRot(), 0.0F));
/* 101 */     } else if (movement.length() > 9.999999747378752E-6D) {
/* 102 */       double horizontalDistance = movement.horizontalDistance();
/* 103 */       double rotation = Math.atan2(-movement.y, horizontalDistance) * 57.2957763671875D;
/* 104 */       this.dolphin.setXRot((float)rotation);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\DolphinJumpGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */