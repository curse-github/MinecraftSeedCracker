/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StopAdmiringIfItemTooFarAway<E extends Piglin> extends Object {
/*    */   public static BehaviorControl<LivingEntity> create(int maxDistanceToItem) {
/* 13 */     return BehaviorBuilder.create(i -> i.group(i
/* 14 */           .present(MemoryModuleType.ADMIRING_ITEM), i
/* 15 */           .registered(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM))
/* 16 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopAdmiringIfItemTooFarAway.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */