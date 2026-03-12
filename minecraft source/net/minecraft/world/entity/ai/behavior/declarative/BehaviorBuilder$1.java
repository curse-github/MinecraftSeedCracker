/*    */ package net.minecraft.world.entity.ai.behavior.declarative;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   extends OneShot<E>
/*    */ {
/*    */   public boolean trigger(ServerLevel level, E body, long timestamp) {
/* 48 */     Trigger<E> trigger = (Trigger)resolvedBuilder.tryTrigger(level, body, timestamp);
/* 49 */     if (trigger == null) {
/* 50 */       return false;
/*    */     }
/*    */     
/* 53 */     return trigger.trigger(level, body, timestamp);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 58 */   public String debugString() { return "OneShot[" + resolvedBuilder.debugString() + "]"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public String toString() { return debugString(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */