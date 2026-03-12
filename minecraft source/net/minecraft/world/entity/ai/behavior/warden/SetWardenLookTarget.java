/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class SetWardenLookTarget {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 17 */           .registered(MemoryModuleType.DISTURBANCE_LOCATION), i
/* 18 */           .registered(MemoryModuleType.ROAR_TARGET), i
/* 19 */           .absent(MemoryModuleType.ATTACK_TARGET))
/* 20 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\SetWardenLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */