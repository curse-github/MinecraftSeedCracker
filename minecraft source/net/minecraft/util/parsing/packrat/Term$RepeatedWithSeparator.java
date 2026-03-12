/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RepeatedWithSeparator<S, T>
/*     */   extends Record
/*     */   implements Term<S>
/*     */ {
/*     */   private final NamedRule<S, T> element;
/*     */   private final Atom<List<T>> listName;
/*     */   private final Term<S> separator;
/*     */   private final int minRepetitions;
/*     */   private final boolean allowTrailingSeparator;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; }
/*     */   
/* 140 */   public RepeatedWithSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions, boolean allowTrailingSeparator) { this.element = element; this.listName = listName; this.separator = separator; this.minRepetitions = minRepetitions; this.allowTrailingSeparator = allowTrailingSeparator; } public NamedRule<S, T> element() { return this.element; } public Atom<List<T>> listName() { return this.listName; } public Term<S> separator() { return this.separator; } public int minRepetitions() { return this.minRepetitions; } public boolean allowTrailingSeparator() { return this.allowTrailingSeparator; }
/*     */   
/*     */   public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 143 */     int listMark = state.mark();
/* 144 */     List<T> elements = new ArrayList<T>(this.minRepetitions);
/*     */     
/* 146 */     boolean first = true;
/*     */     while (true) {
/* 148 */       int markBeforeSeparator = state.mark();
/* 149 */       if (!first && 
/* 150 */         !this.separator.parse(state, scope, control)) {
/* 151 */         state.restore(markBeforeSeparator);
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/* 156 */       int markAfterSeparator = state.mark();
/* 157 */       T parsedElement = (T)state.parse(this.element);
/* 158 */       if (parsedElement != null) {
/* 159 */         elements.add(parsedElement);
/*     */       } else {
/* 161 */         if (first) {
/*     */           
/* 163 */           state.restore(markAfterSeparator);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 168 */         if (this.allowTrailingSeparator) {
/* 169 */           state.restore(markAfterSeparator);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 174 */         state.restore(listMark);
/* 175 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 179 */       first = false;
/*     */     } 
/*     */     
/* 182 */     if (elements.size() < this.minRepetitions) {
/* 183 */       state.restore(listMark);
/* 184 */       return false;
/*     */     } 
/* 186 */     scope.put(this.listName, elements);
/* 187 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Term$RepeatedWithSeparator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */