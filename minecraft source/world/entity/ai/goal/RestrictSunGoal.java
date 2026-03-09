/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*    */ 
/*    */ public class RestrictSunGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/* 12 */   public RestrictSunGoal(PathfinderMob mob) { this.mob = mob; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public boolean canUse() { return (this.mob.level().isBrightOutside() && this.mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && GoalUtils.hasGroundPathNavigation(this.mob)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 22 */     PathNavigation pathNavigation1 = this.mob.getNavigation(); if (pathNavigation1 instanceof GroundPathNavigation) { GroundPathNavigation pathNavigation = (GroundPathNavigation)pathNavigation1;
/* 23 */       pathNavigation.setAvoidSun(true); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 29 */     if (GoalUtils.hasGroundPathNavigation(this.mob)) { PathNavigation pathNavigation1 = this.mob.getNavigation(); if (pathNavigation1 instanceof GroundPathNavigation) { GroundPathNavigation pathNavigation = (GroundPathNavigation)pathNavigation1;
/* 30 */         pathNavigation.setAvoidSun(false); }
/*    */        }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RestrictSunGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */