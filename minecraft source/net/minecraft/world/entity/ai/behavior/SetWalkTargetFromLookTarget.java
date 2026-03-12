/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SetWalkTargetFromLookTarget
/*    */ {
/* 16 */   public static OneShot<LivingEntity> create(float speedModifier, int closeEnoughDistance) { return create(mob -> true, mob -> Float.valueOf(speedModifier), closeEnoughDistance); }
/*    */ 
/*    */   
/*    */   public static OneShot<LivingEntity> create(Predicate<LivingEntity> canSetWalkTargetPredicate, Function<LivingEntity, Float> speedModifier, int closeEnoughDistance) {
/* 20 */     return BehaviorBuilder.create(i -> i.group(i
/* 21 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 22 */           .present(MemoryModuleType.LOOK_TARGET))
/* 23 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */