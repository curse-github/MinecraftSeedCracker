/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SignedArguments
/*    */   extends Record
/*    */   implements CommandSigningContext
/*    */ {
/*    */   private final Map<String, PlayerChatMessage> arguments;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments; }
/*    */   
/* 18 */   public SignedArguments(Map<String, PlayerChatMessage> arguments) { this.arguments = arguments; } public Map<String, PlayerChatMessage> arguments() { return this.arguments; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments; }
/*    */   
/* 21 */   public PlayerChatMessage getArgument(String name) { return (PlayerChatMessage)this.arguments.get(name); }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/CommandSigningContext$SignedArguments;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/CommandSigningContext$SignedArguments;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\CommandSigningContext$SignedArguments.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */