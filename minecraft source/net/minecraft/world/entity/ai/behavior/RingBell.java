/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.block.BellBlock;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class RingBell {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 16 */     return BehaviorBuilder.create(i -> i.group(i
/* 17 */           .present(MemoryModuleType.MEETING_POINT))
/* 18 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   private static final float BELL_RING_CHANCE = 0.95F;
/*    */   public static final int RING_BELL_FROM_DISTANCE = 3;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RingBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */