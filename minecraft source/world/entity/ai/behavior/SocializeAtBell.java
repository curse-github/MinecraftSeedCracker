/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SocializeAtBell {
/*    */   public static OneShot<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 17 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 18 */           .present(MemoryModuleType.MEETING_POINT), i
/* 19 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES), i
/* 20 */           .absent(MemoryModuleType.INTERACTION_TARGET))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */   
/*    */   private static final float SPEED_MODIFIER = 0.3F;
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SocializeAtBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */