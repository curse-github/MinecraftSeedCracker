/*    */ package net.minecraft.resources;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class InternKey
/*    */   extends Record
/*    */ {
/*    */   private final Identifier registry;
/*    */   private final Identifier identifier;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/resources/ResourceKey$InternKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/ResourceKey$InternKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/resources/ResourceKey$InternKey; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/resources/ResourceKey$InternKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/resources/ResourceKey$InternKey;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 18 */   private InternKey(Identifier registry, Identifier identifier) { this.registry = registry; this.identifier = identifier; } public Identifier registry() { return this.registry; } public Identifier identifier() { return this.identifier; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\ResourceKey$InternKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */