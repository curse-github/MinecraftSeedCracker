/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class InteractGoal
/*    */   extends LookAtPlayerGoal {
/*    */   public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/* 10 */     super(mob, lookAtType, lookDistance);
/* 11 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */   
/*    */   public InteractGoal(Mob mob, Class<? extends LivingEntity> lookAtType, float lookDistance, float probability) {
/* 15 */     super(mob, lookAtType, lookDistance, probability);
/* 16 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\goal\InteractGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */