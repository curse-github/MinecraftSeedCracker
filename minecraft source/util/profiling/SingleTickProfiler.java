/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.File;
/*    */ import java.util.function.LongSupplier;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class SingleTickProfiler
/*    */ {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final LongSupplier realTime;
/*    */   private final long saveThreshold;
/*    */   
/*    */   public SingleTickProfiler(LongSupplier realTime, String location, long saveThresholdNs) {
/* 18 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */ 
/*    */     
/* 21 */     this.realTime = realTime;
/* 22 */     this.location = new File("debug", location);
/* 23 */     this.saveThreshold = saveThresholdNs;
/*    */   }
/*    */   private int tick; private final File location; private ProfileCollector profiler;
/*    */   public ProfilerFiller startTick() {
/* 27 */     this.profiler = new ActiveProfiler(this.realTime, () -> this.tick, () -> true);
/* 28 */     this.tick++;
/* 29 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public void endTick() {
/* 33 */     if (this.profiler == InactiveProfiler.INSTANCE) {
/*    */       return;
/*    */     }
/*    */     
/* 37 */     ProfileResults results = this.profiler.getResults();
/* 38 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */     
/* 40 */     if (results.getNanoDuration() >= this.saveThreshold) {
/* 41 */       File file = new File(this.location, "tick-results-" + Util.getFilenameFormattedDateTime() + ".txt");
/* 42 */       results.saveResults(file.toPath());
/* 43 */       LOGGER.info("Recorded long tick -- wrote info to: {}", file.getAbsolutePath());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static SingleTickProfiler createTickProfiler(String name) {
/* 48 */     if (SharedConstants.DEBUG_MONITOR_TICK_TIMES) {
/* 49 */       return new SingleTickProfiler(Util.timeSource, name, SharedConstants.MAXIMUM_TICK_TIME_NANOS);
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public static ProfilerFiller decorateFiller(ProfilerFiller filler, SingleTickProfiler tickProfiler) {
/* 55 */     if (tickProfiler != null) {
/* 56 */       return ProfilerFiller.combine(tickProfiler.startTick(), filler);
/*    */     }
/* 58 */     return filler;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\SingleTickProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */