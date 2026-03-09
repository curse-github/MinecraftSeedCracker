/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class BecomePassiveIfMemoryPresent {
/*    */   public static BehaviorControl<LivingEntity> create(MemoryModuleType<?> pacifyingMemory, int pacifyDuration) {
/*  9 */     return BehaviorBuilder.create(i -> i.group(i
/* 10 */           .registered(MemoryModuleType.ATTACK_TARGET), i
/* 11 */           .absent(MemoryModuleType.PACIFIED), i
/* 12 */           .present(pacifyingMemory))
/* 13 */         .apply(i, i.point((), ())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BecomePassiveIfMemoryPresent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */