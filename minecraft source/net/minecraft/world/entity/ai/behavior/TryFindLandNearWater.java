/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class TryFindLandNearWater {
/*    */   public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
/* 19 */     MutableLong nextOkStartTime = new MutableLong(0L);
/*    */     
/* 21 */     return BehaviorBuilder.create(i -> i.group(i
/* 22 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 23 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 24 */           .registered(MemoryModuleType.LOOK_TARGET))
/* 25 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TryFindLandNearWater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */