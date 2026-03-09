/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class TryLaySpawnOnWaterNearLand {
/*    */   public static BehaviorControl<LivingEntity> create(Block spawnBlock) {
/* 17 */     return BehaviorBuilder.create(i -> i.group(i
/* 18 */           .absent(MemoryModuleType.ATTACK_TARGET), i
/* 19 */           .present(MemoryModuleType.WALK_TARGET), i
/* 20 */           .present(MemoryModuleType.IS_PREGNANT))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\TryLaySpawnOnWaterNearLand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */