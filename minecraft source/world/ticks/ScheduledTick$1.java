/*    */ package net.minecraft.world.ticks;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.Hash;
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
/*    */   extends Object
/*    */   implements Hash.Strategy<ScheduledTick<?>>
/*    */ {
/* 43 */   public int hashCode(ScheduledTick<?> o) { return 31 * o.pos().hashCode() + o.type().hashCode(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(ScheduledTick<?> a, ScheduledTick<?> b) {
/* 48 */     if (a == b) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (a == null || b == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\ScheduledTick$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */