/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class MeleeAttack {
/* 17 */   public static <T extends Mob> OneShot<T> create(int cooldownBetweenAttacks) { return create(body -> true, cooldownBetweenAttacks); }
/*    */ 
/*    */   
/*    */   public static <T extends Mob> OneShot<T> create(Predicate<T> canAttackPredicate, int cooldownBetweenAttacks) {
/* 21 */     return BehaviorBuilder.create(i -> i.group(i
/* 22 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 23 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 24 */           .absent(MemoryModuleType.ATTACK_COOLING_DOWN), i
/* 25 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 26 */         .apply(i, ()));
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
/* 42 */   private static boolean isHoldingUsableNonMeleeWeapon(Mob body) { Objects.requireNonNull(body); return body.isHolding(body::canUseNonMeleeWeapon); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\MeleeAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */