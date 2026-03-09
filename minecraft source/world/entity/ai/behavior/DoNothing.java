/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class DoNothing
/*    */   extends Object
/*    */   implements BehaviorControl<LivingEntity> {
/*    */   private final int minDuration;
/*    */   private final int maxDuration;
/*    */   private Behavior.Status status;
/*    */   private long endTimestamp;
/*    */   
/*    */   public DoNothing(int minDuration, int maxDuration) {
/* 15 */     this.status = Behavior.Status.STOPPED;
/*    */ 
/*    */ 
/*    */     
/* 19 */     this.minDuration = minDuration;
/* 20 */     this.maxDuration = maxDuration;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 25 */   public Behavior.Status getStatus() { return this.status; }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, LivingEntity body, long timestamp) {
/* 30 */     this.status = Behavior.Status.RUNNING;
/* 31 */     int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/* 32 */     this.endTimestamp = timestamp + duration;
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void tickOrStop(ServerLevel level, LivingEntity body, long timestamp) {
/* 38 */     if (timestamp > this.endTimestamp) {
/* 39 */       doStop(level, body, timestamp);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public final void doStop(ServerLevel level, LivingEntity body, long timestamp) { this.status = Behavior.Status.STOPPED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 50 */   public String debugString() { return getClass().getSimpleName(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\DoNothing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */