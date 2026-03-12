/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetHiddenState
/*    */ {
/*    */   private static final int HIDE_TIMEOUT = 300;
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(int seconds, int closeEnoughDist) {
/* 21 */     int stayHiddenTicks = seconds * 20;
/*    */ 
/*    */     
/* 24 */     MutableInt ticksHidden = new MutableInt(0);
/*    */     
/* 26 */     return BehaviorBuilder.create(i -> i.group(i
/* 27 */           .present(MemoryModuleType.HIDING_PLACE), i
/* 28 */           .present(MemoryModuleType.HEARD_BELL_TIME))
/* 29 */         .apply(i, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetHiddenState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */