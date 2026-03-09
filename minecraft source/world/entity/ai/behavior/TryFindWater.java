/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class TryFindWater {
/*    */   public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
/* 17 */     MutableLong nextOkStartTime = new MutableLong(0L);
/*    */     
/* 19 */     return BehaviorBuilder.create(i -> i.group(i
/* 20 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 21 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 22 */           .registered(MemoryModuleType.LOOK_TARGET))
/* 23 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TryFindWater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */