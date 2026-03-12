/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class LookAtTargetSink extends Behavior<Mob> {
/* 11 */   public LookAtTargetSink(int minDuration, int maxDuration) { super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), minDuration, maxDuration); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) { return body.getBrain().getMemory(MemoryModuleType.LOOK_TARGET)
/* 17 */       .filter(pos -> pos.isVisibleBy(body))
/* 18 */       .isPresent(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   protected void stop(ServerLevel level, Mob body, long timestamp) { body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 28 */     body.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(target -> 
/* 29 */         body.getLookControl().setLookAt(target.currentPosition()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LookAtTargetSink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */