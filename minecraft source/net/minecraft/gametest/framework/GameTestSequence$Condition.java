/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
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
/*    */ public class Condition
/*    */ {
/*    */   private static final int NOT_TRIGGERED = -1;
/* 17 */   private int triggerTime = -1;
/*    */   
/*    */   void trigger(int time) {
/* 20 */     if (this.triggerTime != -1) {
/* 21 */       throw new IllegalStateException("Condition already triggered at " + this.triggerTime);
/*    */     }
/* 23 */     this.triggerTime = time;
/*    */   }
/*    */   
/*    */   public void assertTriggeredThisTick() {
/* 27 */     int tick = GameTestSequence.this.parent.getTick();
/* 28 */     if (this.triggerTime != tick) {
/* 29 */       if (this.triggerTime == -1) {
/* 30 */         throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_not_triggered"), tick);
/*    */       }
/* 32 */       throw new GameTestAssertException(Component.translatable("test.error.sequence.condition_already_triggered", new Object[] { Integer.valueOf(this.triggerTime) }), tick);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\GameTestSequence$Condition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */