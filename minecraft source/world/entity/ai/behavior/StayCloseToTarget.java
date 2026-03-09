/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class StayCloseToTarget {
/*    */   public static BehaviorControl<LivingEntity> create(Function<LivingEntity, Optional<PositionTracker>> targetPositionGetter, Predicate<LivingEntity> shouldRunPredicate, int closeEnough, int tooFar, float speedModifier) {
/* 14 */     return BehaviorBuilder.create(i -> i.group(i
/* 15 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 16 */           .registered(MemoryModuleType.WALK_TARGET))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StayCloseToTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */