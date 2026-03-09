/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StopHoldingItemIfNoLongerAdmiring {
/*    */   public static BehaviorControl<Piglin> create() {
/* 10 */     return BehaviorBuilder.create(i -> i.group(i
/* 11 */           .absent(MemoryModuleType.ADMIRING_ITEM))
/* 12 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopHoldingItemIfNoLongerAdmiring.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */