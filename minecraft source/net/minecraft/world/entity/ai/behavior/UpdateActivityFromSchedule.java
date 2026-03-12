/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ 
/*    */ public class UpdateActivityFromSchedule {
/*    */   public static BehaviorControl<LivingEntity> create() {
/*  8 */     return BehaviorBuilder.create(i -> i.point(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\UpdateActivityFromSchedule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */