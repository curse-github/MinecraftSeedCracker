/*   */ package net.minecraft.util.parsing.packrat;public final class ErrorEntry<S> extends Record { private final int cursor; private final SuggestionSupplier<S> suggestions; private final Object reason;
/*   */   
/* 3 */   public ErrorEntry(int cursor, SuggestionSupplier<S> suggestions, Object reason) { this.cursor = cursor; this.suggestions = suggestions; this.reason = reason; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/ErrorEntry;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry<TS;>; } public int cursor() { return this.cursor; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/ErrorEntry;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/*   */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry<TS;>; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/ErrorEntry;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */     // Local variable type table:
/*   */     //   start	length	slot	name	signature
/* 3 */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/ErrorEntry<TS;>; } public SuggestionSupplier<S> suggestions() { return this.suggestions; } public Object reason() { return this.reason; } }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\ErrorEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */