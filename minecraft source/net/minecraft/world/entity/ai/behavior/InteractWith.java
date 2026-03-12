/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class InteractWith
/*    */ {
/* 18 */   public static <T extends LivingEntity> BehaviorControl<LivingEntity> of(EntityType<? extends T> type, int interactionRange, MemoryModuleType<T> interactionTarget, float speedModifier, int stopDistance) { return of(type, interactionRange, mob -> true, mob -> true, interactionTarget, speedModifier, stopDistance); }
/*    */ 
/*    */   
/*    */   public static <E extends LivingEntity, T extends LivingEntity> BehaviorControl<E> of(EntityType<? extends T> type, int interactionRange, Predicate<E> selfFilter, Predicate<T> targetFilter, MemoryModuleType<T> interactionTarget, float speedModifier, int stopDistance) {
/* 22 */     int interactionRangeSqr = interactionRange * interactionRange;
/* 23 */     Predicate<LivingEntity> isTargetValid = mob -> (type.equals(mob.getType()) && targetFilter.test(mob));
/*    */     
/* 25 */     return BehaviorBuilder.create(i -> i.group(i
/* 26 */           .registered(interactionTarget), i
/* 27 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 28 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 29 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 30 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\InteractWith.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */