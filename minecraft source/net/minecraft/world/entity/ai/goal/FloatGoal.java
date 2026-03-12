/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class FloatGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   
/*    */   public FloatGoal(Mob mob) {
/* 12 */     this.mob = mob;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP));
/* 14 */     mob.getNavigation().setCanFloat(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 19 */   public boolean canUse() { return ((this.mob.isInWater() && this.mob.getFluidHeight(FluidTags.WATER) > this.mob.getFluidJumpThreshold()) || this.mob.isInLava()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 29 */     if (this.mob.getRandom().nextFloat() < 0.8F)
/* 30 */       this.mob.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\FloatGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */