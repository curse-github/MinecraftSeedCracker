/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class Mount {
/*    */   private static final int CLOSE_ENOUGH_TO_START_RIDING_DIST = 1;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(float speedModifier) {
/* 17 */     return BehaviorBuilder.create(i -> i.group(i
/* 18 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 19 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 20 */           .present(MemoryModuleType.RIDE_TARGET))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Mount.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */