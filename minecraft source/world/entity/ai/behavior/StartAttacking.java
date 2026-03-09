/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ public class StartAttacking
/*    */ {
/* 16 */   public static <E extends Mob> BehaviorControl<E> create(TargetFinder<E> targetFinderFunction) { return create((level, body) -> true, targetFinderFunction); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StartAttackingCondition<E> canAttackPredicate, TargetFinder<E> targetFinderFunction) {
/* 23 */     return BehaviorBuilder.create(i -> i.group(i
/* 24 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 25 */           .registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE))
/* 26 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StartAttackingCondition<E> {
/*    */     boolean test(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetFinder<E> {
/*    */     Optional<? extends LivingEntity> get(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */