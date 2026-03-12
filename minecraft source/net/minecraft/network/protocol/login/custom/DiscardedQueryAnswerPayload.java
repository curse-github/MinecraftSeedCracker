/*   */ package net.minecraft.network.protocol.login.custom;
/*   */ 
/*   */ import net.minecraft.network.FriendlyByteBuf;
/*   */ 
/*   */ public final class DiscardedQueryAnswerPayload extends Record implements CustomQueryAnswerPayload {
/* 6 */   public static final DiscardedQueryAnswerPayload INSTANCE = new DiscardedQueryAnswerPayload();
/*   */   
/*   */   public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload;
/*   */     //   0	8	1	o	Ljava/lang/Object; }
/*   */   
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload; }
/*   */   
/*   */   public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryAnswerPayload; }
/*   */   
/*   */   public void write(FriendlyByteBuf output) {}
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\custom\DiscardedQueryAnswerPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */