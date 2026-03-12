/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SetLookAndInteract {
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, int interactionRange) {
/* 12 */     int interactionRangeSqr = interactionRange * interactionRange;
/* 13 */     return BehaviorBuilder.create(i -> i.group(i
/* 14 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 15 */           .absent(MemoryModuleType.INTERACTION_TARGET), i
/* 16 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetLookAndInteract.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */