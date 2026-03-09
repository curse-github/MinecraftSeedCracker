/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public interface ParseState<S>
/*    */ {
/*    */   Scope scope();
/*    */   
/*    */   ErrorCollector<S> errorCollector();
/*    */   
/*    */   default <T> Optional<T> parseTopRule(NamedRule<S, T> rule) {
/* 13 */     T result = (T)parse(rule);
/* 14 */     if (result != null)
/*    */     {
/* 16 */       errorCollector().finish(mark());
/*    */     }
/*    */     
/* 19 */     if (!scope().hasOnlySingleFrame()) {
/* 20 */       throw new IllegalStateException("Malformed scope: " + String.valueOf(scope()));
/*    */     }
/*    */     
/* 23 */     return Optional.ofNullable(result);
/*    */   }
/*    */   
/*    */   <T> T parse(NamedRule<S, T> paramNamedRule);
/*    */   
/*    */   S input();
/*    */   
/*    */   int mark();
/*    */   
/*    */   void restore(int paramInt);
/*    */   
/*    */   Control acquireControl();
/*    */   
/*    */   void releaseControl();
/*    */   
/*    */   ParseState<S> silent();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\ParseState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */