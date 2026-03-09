/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface CommandSource {
/*  6 */   public static final CommandSource NULL = new CommandSource()
/*    */     {
/*    */       public void sendSystemMessage(Component message) {}
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 13 */       public boolean acceptsSuccess() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 18 */       public boolean acceptsFailure() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 23 */       public boolean shouldInformAdmins() { return false; }
/*    */     };
/*    */ 
/*    */   
/*    */   void sendSystemMessage(Component paramComponent);
/*    */ 
/*    */   
/*    */   boolean acceptsSuccess();
/*    */   
/*    */   boolean acceptsFailure();
/*    */   
/*    */   boolean shouldInformAdmins();
/*    */   
/* 36 */   default boolean alwaysAccepts() { return false; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\CommandSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */