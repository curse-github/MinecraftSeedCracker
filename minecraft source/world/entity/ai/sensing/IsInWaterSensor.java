/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ public class IsInWaterSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 14 */   public Set<MemoryModuleType<?>> requires() { return ImmutableSet.of(MemoryModuleType.IS_IN_WATER); }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 19 */     if (body.isInWater()) {
/* 20 */       body.getBrain().setMemory(MemoryModuleType.IS_IN_WATER, Unit.INSTANCE);
/*    */     } else {
/* 22 */       body.getBrain().eraseMemory(MemoryModuleType.IS_IN_WATER);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\sensing\IsInWaterSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */