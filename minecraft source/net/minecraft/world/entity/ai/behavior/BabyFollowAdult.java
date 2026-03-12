/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class BabyFollowAdult {
/* 13 */   public static OneShot<LivingEntity> create(UniformInt followRange, float speedModifier) { return create(followRange, mob -> Float.valueOf(speedModifier), MemoryModuleType.NEAREST_VISIBLE_ADULT, false); }
/*    */ 
/*    */   
/*    */   public static OneShot<LivingEntity> create(UniformInt followRange, Function<LivingEntity, Float> speedModifier, MemoryModuleType<? extends LivingEntity> nearestVisibleType, boolean targetEye) {
/* 17 */     return BehaviorBuilder.create(i -> i.group(i
/* 18 */           .present(nearestVisibleType), i
/* 19 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 20 */           .absent(MemoryModuleType.WALK_TARGET))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BabyFollowAdult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */