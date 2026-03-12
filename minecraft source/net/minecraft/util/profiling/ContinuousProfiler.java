/*    */ package net.minecraft.util.profiling;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.function.IntSupplier;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ public class ContinuousProfiler {
/*    */   private final LongSupplier realTime;
/*    */   private final IntSupplier tickCount;
/*    */   
/*    */   public ContinuousProfiler(LongSupplier realTime, IntSupplier tickCount, BooleanSupplier suppressWarnings) {
/* 11 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */ 
/*    */     
/* 14 */     this.realTime = realTime;
/* 15 */     this.tickCount = tickCount;
/* 16 */     this.suppressWarnings = suppressWarnings;
/*    */   }
/*    */   private final BooleanSupplier suppressWarnings; private ProfileCollector profiler;
/*    */   
/* 20 */   public boolean isEnabled() { return (this.profiler != InactiveProfiler.INSTANCE); }
/*    */ 
/*    */ 
/*    */   
/* 24 */   public void disable() { this.profiler = InactiveProfiler.INSTANCE; }
/*    */ 
/*    */ 
/*    */   
/* 28 */   public void enable() { this.profiler = new ActiveProfiler(this.realTime, this.tickCount, this.suppressWarnings); }
/*    */ 
/*    */ 
/*    */   
/* 32 */   public ProfilerFiller getFiller() { return this.profiler; }
/*    */ 
/*    */ 
/*    */   
/* 36 */   public ProfileResults getResults() { return this.profiler.getResults(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ContinuousProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */