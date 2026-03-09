/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ public class ReactToBell {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 11 */     return BehaviorBuilder.create(i -> i.group(i
/* 12 */           .present(MemoryModuleType.HEARD_BELL_TIME))
/* 13 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ReactToBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */