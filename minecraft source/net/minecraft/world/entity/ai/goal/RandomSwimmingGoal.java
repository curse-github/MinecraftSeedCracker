/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class RandomSwimmingGoal
/*    */   extends RandomStrollGoal
/*    */ {
/* 10 */   public RandomSwimmingGoal(PathfinderMob mob, double speedModifier, int interval) { super(mob, speedModifier, interval); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected Vec3 getPosition() { return BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 7); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\RandomSwimmingGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */