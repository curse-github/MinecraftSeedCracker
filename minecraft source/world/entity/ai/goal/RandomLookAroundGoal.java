/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class RandomLookAroundGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private double relX;
/*    */   private double relZ;
/*    */   private int lookTime;
/*    */   
/*    */   public RandomLookAroundGoal(Mob mob) {
/* 14 */     this.mob = mob;
/* 15 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public boolean canUse() { return (this.mob.getRandom().nextFloat() < 0.02F); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public boolean canContinueToUse() { return (this.lookTime >= 0); }
/*    */ 
/*    */ 
/*    */   
/*    */   public void start() {
/* 30 */     double rnd = 6.283185307179586D * this.mob.getRandom().nextDouble();
/* 31 */     this.relX = Math.cos(rnd);
/* 32 */     this.relZ = Math.sin(rnd);
/* 33 */     this.lookTime = 20 + this.mob.getRandom().nextInt(20);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 38 */   public boolean requiresUpdateEveryTick() { return true; }
/*    */ 
/*    */ 
/*    */   
/*    */   public void tick() {
/* 43 */     this.lookTime--;
/* 44 */     this.mob.getLookControl().setLookAt(this.mob.getX() + this.relX, this.mob.getEyeY(), this.mob.getZ() + this.relZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomLookAroundGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */