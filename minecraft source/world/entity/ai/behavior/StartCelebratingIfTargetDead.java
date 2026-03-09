/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StartCelebratingIfTargetDead
/*    */ {
/*    */   public static BehaviorControl<LivingEntity> create(int celebrateDuration, BiPredicate<LivingEntity, LivingEntity> dancePredicate) {
/* 19 */     return BehaviorBuilder.create(i -> i.group(i
/* 20 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 21 */           .registered(MemoryModuleType.ANGRY_AT), i
/* 22 */           .absent(MemoryModuleType.CELEBRATE_LOCATION), i
/* 23 */           .registered(MemoryModuleType.DANCING))
/* 24 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartCelebratingIfTargetDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */