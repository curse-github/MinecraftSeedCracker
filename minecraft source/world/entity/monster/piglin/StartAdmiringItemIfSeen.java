/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StartAdmiringItemIfSeen {
/*    */   public static BehaviorControl<LivingEntity> create(int admireDuration) {
/* 11 */     return BehaviorBuilder.create(i -> i.group(i
/* 12 */           .present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), i
/* 13 */           .absent(MemoryModuleType.ADMIRING_ITEM), i
/* 14 */           .absent(MemoryModuleType.ADMIRING_DISABLED), i
/* 15 */           .absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM))
/* 16 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StartAdmiringItemIfSeen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */