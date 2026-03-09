/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ public interface Control {
/*  4 */   public static final Control UNBOUND = new Control()
/*    */     {
/*    */       public void cut() {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 11 */       public boolean hasCut() { return false; }
/*    */     };
/*    */   
/*    */   void cut();
/*    */   
/*    */   boolean hasCut();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Control.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */