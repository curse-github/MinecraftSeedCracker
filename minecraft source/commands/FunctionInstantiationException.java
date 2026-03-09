/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FunctionInstantiationException extends Exception {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public FunctionInstantiationException(Component messageComponent) {
/*  9 */     super(messageComponent.getString());
/* 10 */     this.messageComponent = messageComponent;
/*    */   }
/*    */ 
/*    */   
/* 14 */   public Component messageComponent() { return this.messageComponent; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\FunctionInstantiationException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */