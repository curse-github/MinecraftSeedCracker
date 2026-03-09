/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class ValidatePlayDead {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 10 */     return BehaviorBuilder.create(i -> i.group(i
/* 11 */           .present(MemoryModuleType.PLAY_DEAD_TICKS), i
/* 12 */           .registered(MemoryModuleType.HURT_BY_ENTITY))
/* 13 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\axolotl\ValidatePlayDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */