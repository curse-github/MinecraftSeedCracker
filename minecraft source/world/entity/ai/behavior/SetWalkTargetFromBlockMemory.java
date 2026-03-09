/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetWalkTargetFromBlockMemory
/*    */ {
/*    */   public static OneShot<Villager> create(MemoryModuleType<GlobalPos> memoryType, float speedModifier, int closeEnoughDist, int tooFarDistance, int tooLongUnreachableDuration) {
/* 26 */     return BehaviorBuilder.create(i -> i.group(i
/* 27 */           .registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE), i
/* 28 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 29 */           .present(memoryType))
/* 30 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromBlockMemory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */