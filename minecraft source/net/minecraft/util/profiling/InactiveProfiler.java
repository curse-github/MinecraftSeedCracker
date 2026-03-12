/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class InactiveProfiler
/*    */   implements ProfileCollector
/*    */ {
/* 12 */   public static final InactiveProfiler INSTANCE = new InactiveProfiler();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void endTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(String name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(Supplier<String> name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void markForCharting(MetricCategory category) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void pop() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(String name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(Supplier<String> name) {}
/*    */ 
/*    */ 
/*    */   
/* 51 */   public Zone zone(String name) { return Zone.INACTIVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 56 */   public Zone zone(Supplier<String> name) { return Zone.INACTIVE; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(String name, int amount) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(Supplier<String> name, int amount) {}
/*    */ 
/*    */ 
/*    */   
/* 69 */   public ProfileResults getResults() { return EmptyProfileResults.EMPTY; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 74 */   public ActiveProfiler.PathEntry getEntry(String path) { return null; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 79 */   public Set<Pair<String, MetricCategory>> getChartedPaths() { return ImmutableSet.of(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\InactiveProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */