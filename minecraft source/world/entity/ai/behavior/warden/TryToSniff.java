/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class TryToSniff {
/* 14 */   private static final IntProvider SNIFF_COOLDOWN = UniformInt.of(100, 200);
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 17 */     return BehaviorBuilder.create(i -> i.group(i
/* 18 */           .registered(MemoryModuleType.IS_SNIFFING), i
/* 19 */           .registered(MemoryModuleType.WALK_TARGET), i
/* 20 */           .absent(MemoryModuleType.SNIFF_COOLDOWN), i
/* 21 */           .present(MemoryModuleType.NEAREST_ATTACKABLE), i
/* 22 */           .absent(MemoryModuleType.DISTURBANCE_LOCATION))
/* 23 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\warden\TryToSniff.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */