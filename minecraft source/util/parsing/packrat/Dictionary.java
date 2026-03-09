/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class Dictionary<S>
/*    */   extends Object {
/* 12 */   private final Map<Atom<?>, Entry<S, ?>> terms = new IdentityHashMap();
/*    */   
/*    */   public <T> NamedRule<S, T> put(Atom<T> name, Rule<S, T> entry) {
/* 15 */     Entry<S, T> holder = (Entry)this.terms.computeIfAbsent(name, Entry::new);
/* 16 */     if (holder.value != null) {
/* 17 */       throw new IllegalArgumentException("Trying to override rule: " + String.valueOf(name));
/*    */     }
/*    */     
/* 20 */     holder.value = entry;
/* 21 */     return holder;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public <T> NamedRule<S, T> putComplex(Atom<T> name, Term<S> term, Rule.RuleAction<S, T> action) { return put(name, Rule.fromTerm(term, action)); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public <T> NamedRule<S, T> put(Atom<T> name, Term<S> term, Rule.SimpleRuleAction<S, T> action) { return put(name, Rule.fromTerm(term, action)); }
/*    */ 
/*    */   
/*    */   public void checkAllBound() {
/* 36 */     List<? extends Atom<?>> unboundNames = this.terms.entrySet().stream().filter(e -> (((Entry)e.getValue()).value == null)).map(Map.Entry::getKey).toList();
/* 37 */     if (!unboundNames.isEmpty()) {
/* 38 */       throw new IllegalStateException("Unbound names: " + String.valueOf(unboundNames));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public <T> NamedRule<S, T> getOrThrow(Atom<T> name) { return (NamedRule)Objects.requireNonNull((Entry)this.terms.get(name), () -> "No rule called " + String.valueOf(name)); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public <T> NamedRule<S, T> forward(Atom<T> name) { return getOrCreateEntry(name); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 53 */   private <T> Entry<S, T> getOrCreateEntry(Atom<T> name) { return (Entry)this.terms.computeIfAbsent(name, Entry::new); }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public <T> Term<S> named(Atom<T> name) { return new Reference(getOrCreateEntry(name), name); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public <T> Term<S> namedWithAlias(Atom<T> nameToParse, Atom<T> nameToStore) { return new Reference(getOrCreateEntry(nameToParse), nameToStore); }
/*    */   private static final class Reference<S, T> extends Record implements Term<S> { private final Dictionary.Entry<S, T> ruleToParse; private final Atom<T> nameToStore;
/*    */     
/* 64 */     private Reference(Dictionary.Entry<S, T> ruleToParse, Atom<T> nameToStore) { this.ruleToParse = ruleToParse; this.nameToStore = nameToStore; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 64 */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>; } public Dictionary.Entry<S, T> ruleToParse() { return this.ruleToParse; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #64	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 64 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/Dictionary$Reference<TS;TT;>; } public Atom<T> nameToStore() { return this.nameToStore; }
/*    */     
/*    */     public boolean parse(ParseState<S> state, Scope scope, Control control) {
/* 67 */       T result = (T)state.parse(this.ruleToParse);
/* 68 */       if (result == null) {
/* 69 */         return false;
/*    */       }
/* 71 */       scope.put(this.nameToStore, result);
/* 72 */       return true;
/*    */     } }
/*    */ 
/*    */   
/*    */   private static class Entry<S, T>
/*    */     extends Object implements NamedRule<S, T>, Supplier<String> {
/*    */     private final Atom<T> name;
/*    */     private Rule<S, T> value;
/*    */     
/* 81 */     private Entry(Atom<T> name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 86 */     public Atom<T> name() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 91 */     public Rule<S, T> value() { return (Rule)Objects.requireNonNull(this.value, this); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 97 */     public String get() { return "Unbound rule " + String.valueOf(this.name); }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Dictionary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */