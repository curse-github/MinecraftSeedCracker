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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WrappedTerm<S, T>
/*    */   extends Record
/*    */   implements Rule<S, T>
/*    */ {
/*    */   private final Rule.RuleAction<S, T> action;
/*    */   private final Term<S> child;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Rule$WrappedTerm<TS;TT;>; }
/*    */   
/* 37 */   public WrappedTerm(Rule.RuleAction<S, T> action, Term<S> child) { this.action = action; this.child = child; } public Rule.RuleAction<S, T> action() { return this.action; } public Term<S> child() { return this.child; }
/*    */   
/*    */   public T parse(ParseState<S> state) {
/* 40 */     scope = state.scope();
/* 41 */     scope.pushFrame();
/*    */     try {
/* 43 */       if (this.child.parse(state, scope, Control.UNBOUND)) {
/*    */ 
/*    */         
/* 46 */         object1 = this.action.run(state); return (T)object1;
/*    */       } 
/* 48 */       object = null; return (T)object;
/*    */     } finally {
/*    */       
/* 51 */       scope.popFrame();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Rule$WrappedTerm.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */