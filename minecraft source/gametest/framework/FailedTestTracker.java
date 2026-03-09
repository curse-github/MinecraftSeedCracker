/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public class FailedTestTracker
/*    */ {
/* 10 */   private static final Set<Holder.Reference<GameTestInstance>> LAST_FAILED_TESTS = Sets.newHashSet();
/*    */ 
/*    */   
/* 13 */   public static Stream<Holder.Reference<GameTestInstance>> getLastFailedTests() { return LAST_FAILED_TESTS.stream(); }
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static void rememberFailedTest(Holder.Reference<GameTestInstance> test) { LAST_FAILED_TESTS.add(test); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public static void forgetFailedTests() { LAST_FAILED_TESTS.clear(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\gametest\framework\FailedTestTracker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */