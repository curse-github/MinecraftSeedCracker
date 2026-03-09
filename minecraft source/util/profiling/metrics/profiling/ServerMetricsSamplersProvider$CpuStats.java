/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CpuStats
/*     */ {
/*  96 */   private final SystemInfo systemInfo = new SystemInfo();
/*  97 */   private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*  98 */   public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */   
/* 100 */   private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 101 */   private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */   private long lastPollMs;
/*     */   
/*     */   public double loadForCpu(int i) {
/* 105 */     long now = System.currentTimeMillis();
/* 106 */     if (this.lastPollMs == 0L || this.lastPollMs + 501L < now) {
/* 107 */       this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 108 */       this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 109 */       this.lastPollMs = now;
/*     */     } 
/*     */     
/* 112 */     return this.currentLoad[i] * 100.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider$CpuStats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */