/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class StopBeingAngryIfTargetDead {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 13 */     return BehaviorBuilder.create(i -> i.group(i
/* 14 */           .present(MemoryModuleType.ANGRY_AT))
/* 15 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StopBeingAngryIfTargetDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */