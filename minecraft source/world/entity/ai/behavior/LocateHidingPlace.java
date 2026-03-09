/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*    */ 
/*    */ public class LocateHidingPlace {
/*    */   public static OneShot<LivingEntity> create(int radius, float speedModifier, int closeEnoughDist) {
/* 13 */     return BehaviorBuilder.create(i -> i.group(i
/* 14 */           .absent(MemoryModuleType.WALK_TARGET), i
/* 15 */           .registered(MemoryModuleType.HOME), i
/* 16 */           .registered(MemoryModuleType.HIDING_PLACE), i
/* 17 */           .registered(MemoryModuleType.PATH), i
/* 18 */           .registered(MemoryModuleType.LOOK_TARGET), i
/* 19 */           .registered(MemoryModuleType.BREED_TARGET), i
/* 20 */           .registered(MemoryModuleType.INTERACTION_TARGET))
/* 21 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LocateHidingPlace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */