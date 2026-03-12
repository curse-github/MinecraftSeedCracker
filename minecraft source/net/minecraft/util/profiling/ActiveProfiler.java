/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ActiveProfiler
/*     */   implements ProfileCollector
/*     */ {
/*  28 */   private static final long WARNING_TIME_NANOS = Duration.ofMillis(100L).toNanos();
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger(); private final List<String> paths; private final LongList startTimes; private final Map<String, PathEntry> entries; private final IntSupplier getTickTime; private final LongSupplier getRealTime; private final long startTimeNano;
/*     */   public ActiveProfiler(LongSupplier getRealTime, IntSupplier getTickTime, BooleanSupplier suppressWarnings) {
/*  31 */     this.paths = Lists.newArrayList();
/*  32 */     this.startTimes = new LongArrayList();
/*  33 */     this.entries = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.path = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     this.chartedPaths = new ObjectArraySet();
/*     */ 
/*     */     
/*  47 */     this.startTimeNano = getRealTime.getAsLong();
/*  48 */     this.getRealTime = getRealTime;
/*  49 */     this.startTimeTicks = getTickTime.getAsInt();
/*  50 */     this.getTickTime = getTickTime;
/*  51 */     this.suppressWarnings = suppressWarnings;
/*     */   }
/*     */   private final int startTimeTicks; private String path; private boolean started; private PathEntry currentEntry; private final BooleanSupplier suppressWarnings; private final Set<Pair<String, MetricCategory>> chartedPaths;
/*     */   
/*     */   public void startTick() {
/*  56 */     if (this.started) {
/*  57 */       LOGGER.error("Profiler tick already started - missing endTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     this.started = true;
/*  62 */     this.path = "";
/*  63 */     this.paths.clear();
/*  64 */     push("root");
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  69 */     if (!this.started) {
/*  70 */       LOGGER.error("Profiler tick already ended - missing startTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     pop();
/*  75 */     this.started = false;
/*     */     
/*  77 */     if (!this.path.isEmpty()) {
/*  78 */       LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String name) {
/*  84 */     if (!this.started) {
/*  85 */       LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", name);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     if (!this.path.isEmpty()) {
/*  90 */       this.path += "\036";
/*     */     }
/*  92 */     this.path += this.path;
/*  93 */     this.paths.add(this.path);
/*  94 */     this.startTimes.add(Util.getNanos());
/*  95 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 100 */   public void push(Supplier<String> name) { push((String)name.get()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public void markForCharting(MetricCategory category) { this.chartedPaths.add(Pair.of(this.path, category)); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void pop() {
/* 110 */     if (!this.started) {
/* 111 */       LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
/*     */       return;
/*     */     } 
/* 114 */     if (this.startTimes.isEmpty()) {
/* 115 */       LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
/*     */       return;
/*     */     } 
/* 118 */     long endTime = Util.getNanos();
/* 119 */     long startTime = this.startTimes.removeLong(this.startTimes.size() - 1);
/* 120 */     this.paths.removeLast();
/* 121 */     long time = endTime - startTime;
/*     */     
/* 123 */     PathEntry currentEntry = getCurrentEntry();
/* 124 */     currentEntry.accumulatedDuration += time;
/* 125 */     currentEntry.count++;
/* 126 */     currentEntry.maxDuration = Math.max(currentEntry.maxDuration, time);
/* 127 */     currentEntry.minDuration = Math.min(currentEntry.minDuration, time);
/*     */     
/* 129 */     if (time > WARNING_TIME_NANOS && !this.suppressWarnings.getAsBoolean()) {
/* 130 */       LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)), LogUtils.defer(() -> Double.valueOf(time / 1000000.0D)));
/*     */     }
/*     */     
/* 133 */     this.path = this.paths.isEmpty() ? "" : (String)this.paths.getLast();
/* 134 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String name) {
/* 139 */     pop();
/* 140 */     push(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> name) {
/* 145 */     pop();
/* 146 */     push(name);
/*     */   }
/*     */   
/*     */   private PathEntry getCurrentEntry() {
/* 150 */     if (this.currentEntry == null) {
/* 151 */       this.currentEntry = (PathEntry)this.entries.computeIfAbsent(this.path, key -> new PathEntry());
/*     */     }
/*     */     
/* 154 */     return this.currentEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void incrementCounter(String name, int amount) { (getCurrentEntry()).counters.addTo(name, amount); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   public void incrementCounter(Supplier<String> name, int amount) { (getCurrentEntry()).counters.addTo((String)name.get(), amount); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 169 */   public ProfileResults getResults() { return new FilledProfileResults(this.entries, this.startTimeNano, this.startTimeTicks, this.getRealTime.getAsLong(), this.getTickTime.getAsInt()); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 174 */   public PathEntry getEntry(String path) { return (PathEntry)this.entries.get(path); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   public Set<Pair<String, MetricCategory>> getChartedPaths() { return this.chartedPaths; }
/*     */   
/*     */   public static class PathEntry
/*     */     implements ProfilerPathEntry {
/* 183 */     private long maxDuration = Float.MIN_VALUE;
/* 184 */     private long minDuration = Float.MAX_VALUE;
/*     */     private long accumulatedDuration;
/*     */     private long count;
/* 187 */     private final Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();
/*     */ 
/*     */ 
/*     */     
/* 191 */     public long getDuration() { return this.accumulatedDuration; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     public long getMaxDuration() { return this.maxDuration; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     public long getCount() { return this.count; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     public Object2LongMap<String> getCounters() { return Object2LongMaps.unmodifiable(this.counters); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ActiveProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */