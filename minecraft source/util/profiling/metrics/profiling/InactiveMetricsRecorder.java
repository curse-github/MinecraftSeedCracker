/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import net.minecraft.util.profiling.InactiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public class InactiveMetricsRecorder implements MetricsRecorder {
/*  7 */   public static final MetricsRecorder INSTANCE = new InactiveMetricsRecorder();
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancel() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */ 
/*    */   
/* 23 */   public boolean isRecording() { return false; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   public ProfilerFiller getProfiler() { return InactiveProfiler.INSTANCE; }
/*    */   
/*    */   public void endTick() {}
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\InactiveMetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */