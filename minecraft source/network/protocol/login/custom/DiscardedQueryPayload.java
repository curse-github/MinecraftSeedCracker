/*   */ package net.minecraft.network.protocol.login.custom;
/*   */ import net.minecraft.network.FriendlyByteBuf;
/*   */ import net.minecraft.resources.Identifier;
/*   */ 
/*   */ public final class DiscardedQueryPayload extends Record implements CustomQueryPayload {
/* 6 */   public DiscardedQueryPayload(Identifier id) { this.id = id; } private final Identifier id; public Identifier id() { return this.id; }
/*   */   
/*   */   public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload; }
/*   */   
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload; }
/*   */   
/*   */   public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/custom/DiscardedQueryPayload;
/*   */     //   0	8	1	o	Ljava/lang/Object; }
/*   */   
/*   */   public void write(FriendlyByteBuf output) {}
/*   */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\login\custom\DiscardedQueryPayload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */