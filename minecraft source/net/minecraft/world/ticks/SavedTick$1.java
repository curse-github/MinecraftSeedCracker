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
/*    */ class null
/*    */   extends Object
/*    */   implements Hash.Strategy<SavedTick<?>>
/*    */ {
/* 37 */   public int hashCode(SavedTick<?> o) { return 31 * o.pos().hashCode() + o.type().hashCode(); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(SavedTick<?> a, SavedTick<?> b) {
/* 42 */     if (a == b) {
/* 43 */       return true;
/*    */     }
/* 45 */     if (a == null || b == null) {
/* 46 */       return false;
/*    */     }
/* 48 */     return (a.type() == b.type() && a.pos().equals(b.pos()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\SavedTick$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */