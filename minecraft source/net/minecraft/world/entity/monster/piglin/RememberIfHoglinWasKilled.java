/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class RememberIfHoglinWasKilled {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 11 */     return BehaviorBuilder.create(i -> i.group(i
/* 12 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 13 */           .registered(MemoryModuleType.HUNTED_RECENTLY))
/* 14 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\RememberIfHoglinWasKilled.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */