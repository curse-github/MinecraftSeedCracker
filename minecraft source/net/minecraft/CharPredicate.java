/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CharPredicate
/*    */ {
/*    */   default CharPredicate and(CharPredicate other) {
/* 10 */     Objects.requireNonNull(other);
/* 11 */     return value -> (test(value) && other.test(value));
/*    */   }
/*    */ 
/*    */   
/* 15 */   default CharPredicate negate() { return value -> !test(value); }
/*    */ 
/*    */   
/*    */   default CharPredicate or(CharPredicate other) {
/* 19 */     Objects.requireNonNull(other);
/* 20 */     return value -> (test(value) || other.test(value));
/*    */   }
/*    */   
/*    */   boolean test(char paramChar);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\CharPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */