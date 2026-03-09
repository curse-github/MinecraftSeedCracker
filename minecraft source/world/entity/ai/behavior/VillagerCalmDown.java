/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class VillagerCalmDown {
/*    */   private static final int SAFE_DISTANCE_FROM_DANGER = 36;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group(i
/* 16 */           .registered(MemoryModuleType.HURT_BY), i
/* 17 */           .registered(MemoryModuleType.HURT_BY_ENTITY), i
/* 18 */           .registered(MemoryModuleType.NEAREST_HOSTILE))
/* 19 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerCalmDown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */