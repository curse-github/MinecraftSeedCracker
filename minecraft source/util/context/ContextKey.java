/*    */ package net.minecraft.util.context;
/*    */ 
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ContextKey<T>
/*    */   extends Object
/*    */ {
/*    */   private final Identifier name;
/*    */   
/* 10 */   public ContextKey(Identifier name) { this.name = name; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static <T> ContextKey<T> vanilla(String name) { return new ContextKey(Identifier.withDefaultNamespace(name)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public Identifier name() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   public String toString() { return "<parameter " + String.valueOf(this.name) + ">"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\context\ContextKey.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */