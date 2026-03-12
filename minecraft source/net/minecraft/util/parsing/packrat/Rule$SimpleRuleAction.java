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
/*    */ @FunctionalInterface
/*    */ public interface SimpleRuleAction<S, T>
/*    */   extends Rule.RuleAction<S, T>
/*    */ {
/*    */   T run(Scope paramScope);
/*    */   
/* 25 */   default T run(ParseState<S> state) { return (T)run(state.scope()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\parsing\packrat\Rule$SimpleRuleAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */