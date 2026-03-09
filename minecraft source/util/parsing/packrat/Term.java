/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public interface Term<S>
/*     */ {
/*     */   boolean parse(ParseState<S> paramParseState, Scope paramScope, Control paramControl);
/*     */   
/*  10 */   static <S, T> Term<S> marker(Atom<T> name, T value) { return new Marker(name, value); }
/*     */   public static final class Marker<S, T> extends Record implements Term<S> { private final Atom<T> name; private final T value;
/*     */     
/*  13 */     public Marker(Atom<T> name, T value) { this.name = name; this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  13 */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>; } public Atom<T> name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Marker;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Marker;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #13	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  13 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Marker<TS;TT;>; } public T value() { return (T)this.value; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  16 */       scope.put(this.name, this.value);
/*  17 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*  23 */   static <S> Term<S> sequence(Term... terms) { return new Sequence(terms); }
/*     */   public static final class Sequence<S> extends Record implements Term<S> { private final Term<S>[] elements;
/*     */     
/*  26 */     public Sequence(Term[] elements) { this.elements = elements; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Sequence;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #26	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  26 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Sequence<TS;>; } public Term<S>[] elements() { return this.elements; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  29 */       int mark = state.mark();
/*  30 */       for (Term<S> element : this.elements) {
/*  31 */         if (!element.parse(state, scope, control)) {
/*  32 */           state.restore(mark);
/*  33 */           return false;
/*     */         } 
/*     */       } 
/*  36 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*  42 */   static <S> Term<S> alternative(Term... terms) { return new Alternative(terms); }
/*     */   public static final class Alternative<S> extends Record implements Term<S> { private final Term<S>[] elements;
/*     */     
/*  45 */     public Alternative(Term[] elements) { this.elements = elements; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Alternative;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #45	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  45 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Alternative<TS;>; } public Term<S>[] elements() { return this.elements; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  48 */       Control controlForThis = state.acquireControl();
/*     */       try {
/*  50 */         int mark = state.mark();
/*  51 */         scope.splitFrame();
/*  52 */         for (Term<S> element : this.elements) {
/*  53 */           if (element.parse(state, scope, controlForThis)) {
/*  54 */             scope.mergeFrame();
/*  55 */             return true;
/*     */           } 
/*  57 */           scope.clearFrameValues();
/*  58 */           state.restore(mark);
/*     */           
/*  60 */           if (controlForThis.hasCut()) {
/*     */             break;
/*     */           }
/*     */         } 
/*  64 */         scope.popFrame();
/*  65 */         return false;
/*     */       } finally {
/*  67 */         state.releaseControl();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*  73 */   static <S> Term<S> optional(Term<S> term) { return new Maybe(term); }
/*     */   public static final class Maybe<S> extends Record implements Term<S> { private final Term<S> term;
/*     */     
/*  76 */     public Maybe(Term<S> term) { this.term = term; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Maybe;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  76 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Maybe<TS;>; } public Term<S> term() { return this.term; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  79 */       int mark = state.mark();
/*  80 */       if (!this.term.parse(state, scope, control)) {
/*  81 */         state.restore(mark);
/*     */       }
/*  83 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*  88 */   static <S, T> Term<S> repeated(NamedRule<S, T> element, Atom<List<T>> listName) { return repeated(element, listName, 0); }
/*     */ 
/*     */ 
/*     */   
/*  92 */   static <S, T> Term<S> repeated(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) { return new Repeated(element, listName, minRepetitions); }
/*     */   public static final class Repeated<S, T> extends Record implements Term<S> { private final NamedRule<S, T> element; private final Atom<List<T>> listName; private final int minRepetitions;
/*     */     
/*  95 */     public Repeated(NamedRule<S, T> element, Atom<List<T>> listName, int minRepetitions) { this.element = element; this.listName = listName; this.minRepetitions = minRepetitions; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$Repeated;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  95 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$Repeated<TS;TT;>; } public NamedRule<S, T> element() { return this.element; } public Atom<List<T>> listName() { return this.listName; } public int minRepetitions() { return this.minRepetitions; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/*  98 */       int entryMark, mark = state.mark();
/*  99 */       List<T> elements = new ArrayList<T>(this.minRepetitions);
/*     */       
/*     */       while (true) {
/* 102 */         entryMark = state.mark();
/* 103 */         T parsedElement = (T)state.parse(this.element);
/* 104 */         if (parsedElement != null) {
/* 105 */           elements.add(parsedElement); continue;
/*     */         }  break;
/* 107 */       }  state.restore(entryMark);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 112 */       if (elements.size() < this.minRepetitions) {
/* 113 */         state.restore(mark);
/* 114 */         return false;
/*     */       } 
/* 116 */       scope.put(this.listName, elements);
/* 117 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 122 */   static <S, T> Term<S> repeatedWithTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator) { return repeatedWithTrailingSeparator(element, listName, separator, 0); }
/*     */ 
/*     */ 
/*     */   
/* 126 */   static <S, T> Term<S> repeatedWithTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions) { return new RepeatedWithSeparator(element, listName, separator, minRepetitions, true); }
/*     */ 
/*     */ 
/*     */   
/* 130 */   static <S, T> Term<S> repeatedWithoutTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator) { return repeatedWithoutTrailingSeparator(element, listName, separator, 0); }
/*     */ 
/*     */ 
/*     */   
/* 134 */   static <S, T> Term<S> repeatedWithoutTrailingSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions) { return new RepeatedWithSeparator(element, listName, separator, minRepetitions, false); }
/*     */   public static final class RepeatedWithSeparator<S, T> extends Record implements Term<S> { private final NamedRule<S, T> element; private final Atom<List<T>> listName;
/*     */     private final Term<S> separator;
/*     */     private final int minRepetitions;
/*     */     private final boolean allowTrailingSeparator;
/*     */     
/* 140 */     public RepeatedWithSeparator(NamedRule<S, T> element, Atom<List<T>> listName, Term<S> separator, int minRepetitions, boolean allowTrailingSeparator) { this.element = element; this.listName = listName; this.separator = separator; this.minRepetitions = minRepetitions; this.allowTrailingSeparator = allowTrailingSeparator; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #140	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 140 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$RepeatedWithSeparator<TS;TT;>; } public NamedRule<S, T> element() { return this.element; } public Atom<List<T>> listName() { return this.listName; } public Term<S> separator() { return this.separator; } public int minRepetitions() { return this.minRepetitions; } public boolean allowTrailingSeparator() { return this.allowTrailingSeparator; }
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 143 */       int listMark = state.mark();
/* 144 */       List<T> elements = new ArrayList<T>(this.minRepetitions);
/*     */       
/* 146 */       boolean first = true;
/*     */       while (true) {
/* 148 */         int markBeforeSeparator = state.mark();
/* 149 */         if (!first && 
/* 150 */           !this.separator.parse(state, scope, control)) {
/* 151 */           state.restore(markBeforeSeparator);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 156 */         int markAfterSeparator = state.mark();
/* 157 */         T parsedElement = (T)state.parse(this.element);
/* 158 */         if (parsedElement != null) {
/* 159 */           elements.add(parsedElement);
/*     */         } else {
/* 161 */           if (first) {
/*     */             
/* 163 */             state.restore(markAfterSeparator);
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 168 */           if (this.allowTrailingSeparator) {
/* 169 */             state.restore(markAfterSeparator);
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 174 */           state.restore(listMark);
/* 175 */           return false;
/*     */         } 
/*     */ 
/*     */         
/* 179 */         first = false;
/*     */       } 
/*     */       
/* 182 */       if (elements.size() < this.minRepetitions) {
/* 183 */         state.restore(listMark);
/* 184 */         return false;
/*     */       } 
/* 186 */       scope.put(this.listName, elements);
/* 187 */       return true;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 192 */   static <S> Term<S> positiveLookahead(Term<S> term) { return new LookAhead(term, true); }
/*     */ 
/*     */ 
/*     */   
/* 196 */   static <S> Term<S> negativeLookahead(Term<S> term) { return new LookAhead(term, false); }
/*     */   public static final class LookAhead<S> extends Record implements Term<S> { private final Term<S> term; private final boolean positive;
/*     */     
/* 199 */     public LookAhead(Term<S> term, boolean positive) { this.term = term; this.positive = positive; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Term$LookAhead;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #199	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 199 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Term$LookAhead<TS;>; } public Term<S> term() { return this.term; } public boolean positive() { return this.positive; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 205 */       int mark = state.mark();
/*     */       
/* 207 */       boolean result = this.term.parse(state.silent(), scope, control);
/* 208 */       state.restore(mark);
/* 209 */       return (this.positive == result);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <S> Term<S> cut() {
/* 217 */     return new Term<S>()
/*     */       {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 220 */           control.cut();
/* 221 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 226 */         public String toString() { return "↑"; }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static <S> Term<S> empty() {
/* 232 */     return new Term<S>()
/*     */       {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 235 */           return true;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 240 */         public String toString() { return "ε"; }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   static <S> Term<S> fail(final Object message) {
/* 246 */     return new Term<S>()
/*     */       {
/*     */         public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 249 */           state.errorCollector().store(state.mark(), message);
/* 250 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 255 */         public String toString() { return "fail"; }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Term.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */