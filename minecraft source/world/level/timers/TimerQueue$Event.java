/*    */ package net.minecraft.world.level.timers;
/*    */ 
/*    */ import com.google.common.primitives.UnsignedLong;
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
/*    */ public class Event<T>
/*    */   extends Object
/*    */ {
/*    */   public final long triggerTime;
/*    */   public final UnsignedLong sequentialId;
/*    */   public final String id;
/*    */   public final TimerCallback<T> callback;
/*    */   
/*    */   private Event(long triggerTime, UnsignedLong sequentialId, String id, TimerCallback<T> callback) {
/* 35 */     this.triggerTime = triggerTime;
/* 36 */     this.sequentialId = sequentialId;
/* 37 */     this.id = id;
/* 38 */     this.callback = callback;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\timers\TimerQueue$Event.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */