/*    */ package net.minecraft.commands;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CommandResultCallback {
/*  5 */   public static final CommandResultCallback EMPTY = new CommandResultCallback()
/*    */     {
/*    */       public void onResult(boolean success, int result) {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 12 */       public String toString() { return "<empty>"; }
/*    */     };
/*    */ 
/*    */   
/*    */   void onResult(boolean paramBoolean, int paramInt);
/*    */ 
/*    */   
/* 19 */   default void onSuccess(int result) { onResult(true, result); }
/*    */ 
/*    */ 
/*    */   
/* 23 */   default void onFailure() { onResult(false, 0); }
/*    */ 
/*    */   
/*    */   static CommandResultCallback chain(CommandResultCallback first, CommandResultCallback second) {
/* 27 */     if (first == EMPTY) {
/* 28 */       return second;
/*    */     }
/*    */     
/* 31 */     if (second == EMPTY) {
/* 32 */       return first;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 37 */     return (success, result) -> {
/* 38 */         first.onResult(success, result);
/* 39 */         second.onResult(success, result);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\CommandResultCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */