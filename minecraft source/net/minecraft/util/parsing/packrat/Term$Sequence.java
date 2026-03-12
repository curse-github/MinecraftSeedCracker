/*    */ package net.minecraft.util.parsing.packrat;
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
/*    */ public final class Sequence<S>
/*    */   extends Record
/*    */   implements Term<S>
/*    */ {
/*    */   private final Term<S>[] elements;
/*    */   
/* 26 */   public Sequence(Term[] elements) { this.elements = elements; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 26 */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; } public Term<S>[] elements() { return this.elements; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; }
/*    */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 29 */     int mark = state.mark();
/* 30 */     for (Term<S> element : this.elements) {
/* 31 */       if (!element.parse(state, scope, control)) {
/* 32 */         state.restore(mark);
/* 33 */         return false;
/*    */       } 
/*    */     } 
/* 36 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Term$Sequence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */