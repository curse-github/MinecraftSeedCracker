/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Ticker;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ public class ServerMetricsSamplersProvider implements MetricsSamplerProvider {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   public ServerMetricsSamplersProvider(LongSupplier wallTimeSource, boolean isDedicatedServer) {
/*  28 */     this.samplers = new ObjectOpenHashSet();
/*  29 */     this.samplerFactory = new ProfilerSamplerAdapter();
/*     */ 
/*     */     
/*  32 */     this.samplers.add(tickTimeSampler(wallTimeSource));
/*     */     
/*  34 */     if (isDedicatedServer) {
/*  35 */       this.samplers.addAll(runtimeIndependentSamplers());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private final Set<MetricSampler> samplers;
/*     */   private final ProfilerSamplerAdapter samplerFactory;
/*     */   
/*     */   public static Set<MetricSampler> runtimeIndependentSamplers() {
/*  44 */     result = ImmutableSet.builder();
/*     */     
/*     */     try {
/*  47 */       CpuStats cpuStats = new CpuStats();
/*     */ 
/*     */       
/*  50 */       Objects.requireNonNull(result); IntStream.range(0, cpuStats.nrOfCpus).mapToObj(i -> MetricSampler.create("cpu#" + i, MetricCategory.CPU, ())).forEach(result::add);
/*  51 */     } catch (Throwable t) {
/*  52 */       LOGGER.warn("Failed to query cpu, no cpu stats will be recorded", t);
/*     */     } 
/*     */     
/*  55 */     result.add(MetricSampler.create("heap MiB", MetricCategory.JVM, () -> SystemReport.sizeInMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
/*  56 */     result.addAll(MetricsRegistry.INSTANCE.getRegisteredSamplers());
/*  57 */     return result.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MetricSampler> samplers(Supplier<ProfileCollector> profiler) {
/*  62 */     this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(profiler));
/*  63 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public static MetricSampler tickTimeSampler(final LongSupplier timeSource) {
/*  67 */     Stopwatch stopwatch = Stopwatch.createUnstarted(new Ticker()
/*     */         {
/*     */           public long read() {
/*  70 */             return timeSource.getAsLong();
/*     */           }
/*     */         });
/*     */     
/*  74 */     ToDoubleFunction<Stopwatch> timeSampler = watch -> {
/*  75 */         if (watch.isRunning()) {
/*  76 */           watch.stop();
/*     */         }
/*  78 */         long deltaTime = watch.elapsed(TimeUnit.NANOSECONDS);
/*  79 */         watch.reset();
/*  80 */         return deltaTime;
/*     */       };
/*     */     
/*  83 */     MetricSampler.ValueIncreasedByPercentage thresholdAlerter = new MetricSampler.ValueIncreasedByPercentage(2.0F);
/*     */     
/*  85 */     return MetricSampler.builder("ticktime", MetricCategory.TICK_LOOP, timeSampler, stopwatch)
/*  86 */       .withBeforeTick(Stopwatch::start)
/*  87 */       .withThresholdAlert(thresholdAlerter)
/*  88 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class CpuStats
/*     */   {
/*  96 */     private final SystemInfo systemInfo = new SystemInfo();
/*  97 */     private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*  98 */     public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */     
/* 100 */     private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 101 */     private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */     private long lastPollMs;
/*     */     
/*     */     public double loadForCpu(int i) {
/* 105 */       long now = System.currentTimeMillis();
/* 106 */       if (this.lastPollMs == 0L || this.lastPollMs + 501L < now) {
/* 107 */         this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 108 */         this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 109 */         this.lastPollMs = now;
/*     */       } 
/*     */       
/* 112 */       return this.currentLoad[i] * 100.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */