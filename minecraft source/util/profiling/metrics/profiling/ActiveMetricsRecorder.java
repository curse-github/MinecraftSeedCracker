/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.util.profiling.ActiveProfiler;
/*     */ import net.minecraft.util.profiling.ContinuousProfiler;
/*     */ import net.minecraft.util.profiling.EmptyProfileResults;
/*     */ import net.minecraft.util.profiling.InactiveProfiler;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*     */ import net.minecraft.util.profiling.metrics.storage.RecordedDeviation;
/*     */ 
/*     */ public class ActiveMetricsRecorder
/*     */   implements MetricsRecorder
/*     */ {
/*     */   public static final int PROFILING_MAX_DURATION_SECONDS = 10;
/*  33 */   private static Consumer<Path> globalOnReportFinished = null; private final Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler; private final ContinuousProfiler taskProfiler; private final Executor ioExecutor; private final MetricsPersister metricsPersister; private final Consumer<ProfileResults> onProfilingEnd; private final Consumer<Path> onReportFinished;
/*     */   private ActiveMetricsRecorder(MetricsSamplerProvider metricsSamplerProvider, LongSupplier timeSource, Executor ioExecutor, MetricsPersister metricsPersister, Consumer<ProfileResults> onProfilingEnd, Consumer<Path> onReportFinished) {
/*  35 */     this.deviationsBySampler = new Object2ObjectOpenHashMap();
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
/*  49 */     this.thisTickSamplers = ImmutableSet.of();
/*     */ 
/*     */     
/*  52 */     this.metricsSamplerProvider = metricsSamplerProvider;
/*  53 */     this.wallTimeSource = timeSource;
/*  54 */     this.taskProfiler = new ContinuousProfiler(timeSource, () -> this.currentTick, () -> false);
/*  55 */     this.ioExecutor = ioExecutor;
/*  56 */     this.metricsPersister = metricsPersister;
/*  57 */     this.onProfilingEnd = onProfilingEnd;
/*  58 */     this.onReportFinished = (globalOnReportFinished == null) ? onReportFinished : onReportFinished.andThen(globalOnReportFinished);
/*  59 */     this.deadlineNano = timeSource.getAsLong() + TimeUnit.NANOSECONDS.convert(10L, TimeUnit.SECONDS);
/*  60 */     this.singleTickProfiler = new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*  61 */     this.taskProfiler.enable();
/*     */   }
/*     */   private final MetricsSamplerProvider metricsSamplerProvider; private final LongSupplier wallTimeSource; private final long deadlineNano; private int currentTick; private ProfileCollector singleTickProfiler; private Set<MetricSampler> thisTickSamplers;
/*     */   
/*  65 */   public static ActiveMetricsRecorder createStarted(MetricsSamplerProvider metricsSamplerProvider, LongSupplier timeSource, Executor ioExecutor, MetricsPersister metricsPersister, Consumer<ProfileResults> onProfilingEnd, Consumer<Path> onReportFinished) { return new ActiveMetricsRecorder(metricsSamplerProvider, timeSource, ioExecutor, metricsPersister, onProfilingEnd, onReportFinished); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() {
/*  70 */     if (!isRecording()) {
/*     */       return;
/*     */     }
/*  73 */     this.killSwitch = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/*  78 */     if (!isRecording()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     this.singleTickProfiler = InactiveProfiler.INSTANCE;
/*  83 */     this.onProfilingEnd.accept(EmptyProfileResults.EMPTY);
/*     */     
/*  85 */     cleanup(this.thisTickSamplers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  90 */     verifyStarted();
/*  91 */     this.thisTickSamplers = this.metricsSamplerProvider.samplers(() -> this.singleTickProfiler);
/*  92 */     for (MetricSampler sampler : this.thisTickSamplers) {
/*  93 */       sampler.onStartTick();
/*     */     }
/*  95 */     this.currentTick++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/* 100 */     verifyStarted();
/* 101 */     if (this.currentTick == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 106 */     for (MetricSampler sampler : this.thisTickSamplers) {
/* 107 */       sampler.onEndTick(this.currentTick);
/* 108 */       if (sampler.triggersThreshold()) {
/* 109 */         RecordedDeviation recordedDeviation = new RecordedDeviation(Instant.now(), this.currentTick, this.singleTickProfiler.getResults());
/* 110 */         ((List)this.deviationsBySampler.computeIfAbsent(sampler, ignored -> Lists.newArrayList())).add(recordedDeviation);
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     if (this.killSwitch || this.wallTimeSource.getAsLong() > this.deadlineNano) {
/* 115 */       this.killSwitch = false;
/* 116 */       ProfileResults results = this.taskProfiler.getResults();
/* 117 */       this.singleTickProfiler = InactiveProfiler.INSTANCE;
/* 118 */       this.onProfilingEnd.accept(results);
/* 119 */       scheduleSaveResults(results);
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     this.singleTickProfiler = new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 128 */   public boolean isRecording() { return this.taskProfiler.isEnabled(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   public ProfilerFiller getProfiler() { return ProfilerFiller.combine(this.taskProfiler.getFiller(), this.singleTickProfiler); }
/*     */ 
/*     */   
/*     */   private void verifyStarted() {
/* 137 */     if (!isRecording()) {
/* 138 */       throw new IllegalStateException("Not started!");
/*     */     }
/*     */   }
/*     */   
/*     */   private void scheduleSaveResults(ProfileResults profilerResults) {
/* 143 */     HashSet<MetricSampler> metricSamplers = new HashSet<MetricSampler>(this.thisTickSamplers);
/* 144 */     this.ioExecutor.execute(() -> {
/* 145 */           Path pathToLogs = this.metricsPersister.saveReports(metricSamplers, this.deviationsBySampler, profilerResults);
/*     */           
/* 147 */           cleanup(metricSamplers);
/* 148 */           this.onReportFinished.accept(pathToLogs);
/*     */         });
/*     */   }
/*     */   
/*     */   private void cleanup(Collection<MetricSampler> metricSamplers) {
/* 153 */     for (MetricSampler sampler : metricSamplers) {
/* 154 */       sampler.onFinished();
/*     */     }
/*     */     
/* 157 */     this.deviationsBySampler.clear();
/* 158 */     this.taskProfiler.disable();
/*     */   }
/*     */ 
/*     */   
/* 162 */   public static void registerGlobalCompletionCallback(Consumer<Path> onFinished) { globalOnReportFinished = onFinished; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\ActiveMetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */