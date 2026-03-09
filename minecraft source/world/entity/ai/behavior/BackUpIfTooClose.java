/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class BackUpIfTooClose {
/*    */   public static OneShot<Mob> create(int tooCloseDistance, float strafeSpeed) {
/* 14 */     return BehaviorBuilder.create(i -> i.group(i
/* 15 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 16 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 17 */           .present(MemoryModuleType.ATTACK_TARGET), i
/* 18 */           .present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES))
/* 19 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BackUpIfTooClose.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */