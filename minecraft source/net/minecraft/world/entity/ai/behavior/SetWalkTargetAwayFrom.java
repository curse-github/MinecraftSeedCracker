/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SetWalkTargetAwayFrom {
/* 17 */   public static BehaviorControl<PathfinderMob> pos(MemoryModuleType<BlockPos> memory, float speedModifier, int desiredDistance, boolean interruptCurrentWalk) { return create(memory, speedModifier, desiredDistance, interruptCurrentWalk, Vec3::atBottomCenterOf); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static OneShot<PathfinderMob> entity(MemoryModuleType<? extends Entity> memory, float speedModifier, int desiredDistance, boolean interruptCurrentWalk) { return create(memory, speedModifier, desiredDistance, interruptCurrentWalk, Entity::position); }
/*    */ 
/*    */   
/*    */   private static <T> OneShot<PathfinderMob> create(MemoryModuleType<T> walkAwayFromMemory, float speedModifier, int desiredDistance, boolean interruptCurrentWalk, Function<T, Vec3> toPosition) {
/* 25 */     return BehaviorBuilder.create(i -> i.group(i
/* 26 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 27 */           .present(walkAwayFromMemory))
/* 28 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetAwayFrom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */