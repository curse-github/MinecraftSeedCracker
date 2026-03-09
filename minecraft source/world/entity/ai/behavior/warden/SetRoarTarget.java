/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class SetRoarTarget {
/*    */   public static <E extends Warden> BehaviorControl<E> create(Function<E, Optional<? extends LivingEntity>> targetFinderFunction) {
/* 17 */     return BehaviorBuilder.create(i -> i.group(i
/* 18 */           .absent(MemoryModuleType.ROAR_TARGET), i
/* 19 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 20 */           .registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\SetRoarTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */