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
/*    */ 
/*    */ 
/*    */ public class StopAttackingIfTargetInvalid
/*    */ {
/*    */   private static final int TIMEOUT_TO_GET_WITHIN_ATTACK_RANGE = 200;
/*    */   
/* 20 */   public static <E extends Mob> BehaviorControl<E> create(TargetErasedCallback<E> onTargetErased) { return create((level, entity) -> false, onTargetErased, true); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition stopAttackingWhen) { return create(stopAttackingWhen, (level, body, target) -> {  }true); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 32 */   public static <E extends Mob> BehaviorControl<E> create() { return create((level, entity) -> false, (level, body, target) -> {  }true); }
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition stopAttackingWhen, TargetErasedCallback<E> onTargetErased, boolean canGrowTiredOfTryingToReachTarget) {
/* 36 */     return BehaviorBuilder.create(i -> i.group(i
/* 37 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 38 */           .registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE))
/* 39 */         .apply(i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 58 */   private static boolean isTiredOfTryingToReachTarget(LivingEntity body, Optional<Long> cantReachSince) { return (cantReachSince.isPresent() && body.level().getGameTime() - ((Long)cantReachSince.get()).longValue() > 200L); }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StopAttackCondition {
/*    */     boolean test(ServerLevel param1ServerLevel, LivingEntity param1LivingEntity);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetErasedCallback<E> {
/*    */     void accept(ServerLevel param1ServerLevel, E param1E, LivingEntity param1LivingEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */