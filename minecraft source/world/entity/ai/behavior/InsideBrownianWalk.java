/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class InsideBrownianWalk {
/*    */   public static BehaviorControl<PathfinderMob> create(float speedModifier) {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .absent(MemoryModuleType.WALK_TARGET))
/* 17 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\InsideBrownianWalk.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */