/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StopAdmiringIfTiredOfTryingToReachItem {
/*    */   public static BehaviorControl<LivingEntity> create(int maxTimeToReachItem, int disableTime) {
/* 12 */     return BehaviorBuilder.create(i -> i.group(i
/* 13 */           .present(MemoryModuleType.ADMIRING_ITEM), i
/* 14 */           .present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), i
/* 15 */           .registered(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM), i
/* 16 */           .registered(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopAdmiringIfTiredOfTryingToReachItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */