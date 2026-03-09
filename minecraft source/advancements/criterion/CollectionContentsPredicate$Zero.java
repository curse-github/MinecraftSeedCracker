/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
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
/*    */ public class Zero<T, P extends Predicate<T>>
/*    */   extends Object
/*    */   implements CollectionContentsPredicate<T, P>
/*    */ {
/* 35 */   public boolean test(Iterable<T> values) { return true; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 40 */   public List<P> unpack() { return List.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\CollectionContentsPredicate$Zero.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */