/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ public abstract class OneShot<E extends LivingEntity>
/*    */   extends Object
/*    */   implements BehaviorControl<E>, Trigger<E>
/*    */ {
/* 11 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */ 
/*    */ 
/*    */   
/* 15 */   public final Behavior.Status getStatus() { return this.status; }
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/* 20 */     if (trigger(level, body, timestamp)) {
/* 21 */       this.status = Behavior.Status.RUNNING;
/* 22 */       return true;
/*    */     } 
/* 24 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 29 */   public final void tickOrStop(ServerLevel level, E body, long timestamp) { doStop(level, body, timestamp); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   public final void doStop(ServerLevel level, E body, long timestamp) { this.status = Behavior.Status.STOPPED; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   public String debugString() { return getClass().getSimpleName(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\behavior\OneShot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */