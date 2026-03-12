/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ class Entry<S, T>
/*    */   extends Object
/*    */   implements NamedRule<S, T>, Supplier<String>
/*    */ {
/*    */   private final Atom<T> name;
/*    */   private Rule<S, T> value;
/*    */   
/* 81 */   private Entry(Atom<T> name) { this.name = name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 86 */   public Atom<T> name() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 91 */   public Rule<S, T> value() { return (Rule)Objects.requireNonNull(this.value, this); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 97 */   public String get() { return "Unbound rule " + String.valueOf(this.name); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Dictionary$Entry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */