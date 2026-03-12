/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class CountDownCooldownTicks
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final MemoryModuleType<Integer> cooldownTicks;
/*    */   
/*    */   public CountDownCooldownTicks(MemoryModuleType<Integer> cooldownTicks) {
/* 16 */     super(ImmutableMap.of(cooldownTicks, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */     
/* 19 */     this.cooldownTicks = cooldownTicks;
/*    */   }
/*    */ 
/*    */   
/* 23 */   private Optional<Integer> getCooldownTickMemory(LivingEntity body) { return body.getBrain().getMemory(this.cooldownTicks); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   protected boolean timedOut(long timestamp) { return false; }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, LivingEntity body, long timestamp) {
/* 33 */     Optional<Integer> calmDownTicks = getCooldownTickMemory(body);
/* 34 */     return (calmDownTicks.isPresent() && ((Integer)calmDownTicks.get()).intValue() > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, LivingEntity body, long timestamp) {
/* 39 */     Optional<Integer> calmDownTicks = getCooldownTickMemory(body);
/* 40 */     body.getBrain().setMemory(this.cooldownTicks, Integer.valueOf(((Integer)calmDownTicks.get()).intValue() - 1));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   protected void stop(ServerLevel level, LivingEntity body, long timestamp) { body.getBrain().eraseMemory(this.cooldownTicks); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\CountDownCooldownTicks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */