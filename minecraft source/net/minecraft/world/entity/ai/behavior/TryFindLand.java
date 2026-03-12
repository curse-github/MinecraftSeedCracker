/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
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
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import org.apache.commons.lang3.mutable.MutableLong;
/*    */ 
/*    */ public class TryFindLand
/*    */ {
/*    */   public static BehaviorControl<PathfinderMob> create(int range, float speedModifier) {
/* 22 */     MutableLong nextOkStartTime = new MutableLong(0L);
/*    */     
/* 24 */     return BehaviorBuilder.create(i -> i.group(i
/* 25 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 26 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 27 */           .registered(MemoryModuleType.LOOK_TARGET))
/* 28 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   private static final int COOLDOWN_TICKS = 60;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TryFindLand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */