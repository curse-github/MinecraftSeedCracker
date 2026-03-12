/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.SharedConstants;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.ObjectUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FilledProfileResults
/*     */   implements ProfileResults {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  29 */   private static final ProfilerPathEntry EMPTY = new ProfilerPathEntry()
/*     */     {
/*     */       public long getDuration() {
/*  32 */         return 0L;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  37 */       public long getMaxDuration() { return 0L; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  42 */       public long getCount() { return 0L; }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  47 */       public Object2LongMap<String> getCounters() { return Object2LongMaps.emptyMap(); }
/*     */     };
/*     */ 
/*     */   
/*  51 */   private static final Splitter SPLITTER = Splitter.on('\036');
/*  52 */   private static final Comparator<Map.Entry<String, CounterCollector>> COUNTER_ENTRY_COMPARATOR = Map.Entry.comparingByValue(Comparator.comparingLong(c -> c.totalValue)).reversed();
/*     */   
/*     */   private final Map<String, ? extends ProfilerPathEntry> entries;
/*     */   private final long startTimeNano;
/*     */   private final int startTimeTicks;
/*     */   private final long endTimeNano;
/*     */   private final int endTimeTicks;
/*     */   private final int tickDuration;
/*     */   
/*     */   public FilledProfileResults(Map<String, ? extends ProfilerPathEntry> entries, long startTimeNano, int startTimeTicks, long endTimeNano, int endTimeTicks) {
/*  62 */     this.entries = entries;
/*  63 */     this.startTimeNano = startTimeNano;
/*  64 */     this.startTimeTicks = startTimeTicks;
/*  65 */     this.endTimeNano = endTimeNano;
/*  66 */     this.endTimeTicks = endTimeTicks;
/*  67 */     this.tickDuration = endTimeTicks - startTimeTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   private ProfilerPathEntry getEntry(String path) {
/*  72 */     ProfilerPathEntry result = (ProfilerPathEntry)this.entries.get(path);
/*  73 */     return (result != null) ? result : EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ResultField> getTimes(String path) {
/*  78 */     String rawPath = path;
/*  79 */     ProfilerPathEntry rootEntry = getEntry("root");
/*  80 */     long globalTime = rootEntry.getDuration();
/*  81 */     ProfilerPathEntry currentEntry = getEntry(path);
/*  82 */     long selfTime = currentEntry.getDuration();
/*  83 */     long selfCount = currentEntry.getCount();
/*     */     
/*  85 */     List<ResultField> result = Lists.newArrayList();
/*     */     
/*  87 */     if (!path.isEmpty()) {
/*  88 */       path = path + "\036";
/*     */     }
/*  90 */     long totalTime = 0L;
/*     */     
/*  92 */     for (String key : this.entries.keySet()) {
/*  93 */       if (isDirectChild(path, key)) {
/*  94 */         totalTime += getEntry(key).getDuration();
/*     */       }
/*     */     } 
/*     */     
/*  98 */     float oldTime = (float)totalTime;
/*  99 */     if (totalTime < selfTime) {
/* 100 */       totalTime = selfTime;
/*     */     }
/* 102 */     if (globalTime < totalTime) {
/* 103 */       globalTime = totalTime;
/*     */     }
/*     */     
/* 106 */     for (String key : this.entries.keySet()) {
/* 107 */       if (isDirectChild(path, key)) {
/* 108 */         ProfilerPathEntry entry = getEntry(key);
/* 109 */         long time = entry.getDuration();
/* 110 */         double timePercentage = time * 100.0D / totalTime;
/* 111 */         double globalPercentage = time * 100.0D / globalTime;
/* 112 */         String name = key.substring(path.length());
/* 113 */         result.add(new ResultField(name, timePercentage, globalPercentage, entry.getCount()));
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     if ((float)totalTime > oldTime) {
/* 118 */       result.add(new ResultField("unspecified", ((float)totalTime - oldTime) * 100.0D / totalTime, ((float)totalTime - oldTime) * 100.0D / globalTime, selfCount));
/*     */     }
/*     */     
/* 121 */     Collections.sort(result);
/* 122 */     result.add(0, new ResultField(rawPath, 100.0D, totalTime * 100.0D / globalTime, selfCount));
/* 123 */     return result;
/*     */   }
/*     */ 
/*     */   
/* 127 */   private static boolean isDirectChild(String path, String test) { return (test.length() > path.length() && test.startsWith(path) && test.indexOf('\036', path.length() + 1) < 0); }
/*     */ 
/*     */   
/*     */   private Map<String, CounterCollector> getCounterValues() {
/* 131 */     Map<String, CounterCollector> result = Maps.newTreeMap();
/* 132 */     this.entries.forEach((path, entry) -> {
/* 133 */           Object2LongMap<String> counters = entry.getCounters();
/* 134 */           if (!counters.isEmpty()) {
/* 135 */             List<String> pathSegments = SPLITTER.splitToList(path);
/* 136 */             counters.forEach(());
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 141 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 146 */   public long getStartTimeNano() { return this.startTimeNano; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   public int getStartTimeTicks() { return this.startTimeTicks; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   public long getEndTimeNano() { return this.endTimeNano; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 161 */   public int getEndTimeTicks() { return this.endTimeTicks; }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean saveResults(Path file) {
/* 166 */     writer = null;
/*     */     try {
/* 168 */       Files.createDirectories(file.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/* 169 */       writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/* 170 */       writer.write(getProfilerResults(getNanoDuration(), getTickDuration()));
/* 171 */       return true;
/* 172 */     } catch (Throwable t) {
/* 173 */       LOGGER.error("Could not save profiler results to {}", file, t);
/* 174 */       return false;
/*     */     } finally {
/* 176 */       IOUtils.closeQuietly(writer);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String getProfilerResults(long timespan, int tickspan) {
/* 181 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 183 */     ReportType.PROFILE.appendHeader(builder, List.of());
/*     */     
/* 185 */     builder.append("Version: ").append(SharedConstants.getCurrentVersion().id()).append('\n');
/* 186 */     builder.append("Time span: ").append(timespan / 1000000L).append(" ms\n");
/* 187 */     builder.append("Tick span: ").append(tickspan).append(" ticks\n");
/* 188 */     builder.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(tickspan / (float)timespan / 1.0E9F) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
/*     */     
/* 190 */     builder.append("--- BEGIN PROFILE DUMP ---\n\n");
/*     */     
/* 192 */     appendProfilerResults(0, "root", builder);
/*     */     
/* 194 */     builder.append("--- END PROFILE DUMP ---\n\n");
/*     */     
/* 196 */     Map<String, CounterCollector> counters = getCounterValues();
/*     */     
/* 198 */     if (!counters.isEmpty()) {
/* 199 */       builder.append("--- BEGIN COUNTER DUMP ---\n\n");
/* 200 */       appendCounters(counters, builder, tickspan);
/* 201 */       builder.append("--- END COUNTER DUMP ---\n\n");
/*     */     } 
/*     */     
/* 204 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProfilerResults() {
/* 209 */     StringBuilder builder = new StringBuilder();
/* 210 */     appendProfilerResults(0, "root", builder);
/* 211 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static StringBuilder indentLine(StringBuilder builder, int depth) {
/* 215 */     builder.append(String.format(Locale.ROOT, "[%02d] ", new Object[] { Integer.valueOf(depth) }));
/* 216 */     for (int j = 0; j < depth; j++) {
/* 217 */       builder.append("|   ");
/*     */     }
/* 219 */     return builder;
/*     */   }
/*     */   
/*     */   private void appendProfilerResults(int depth, String path, StringBuilder builder) {
/* 223 */     List<ResultField> results = getTimes(path);
/*     */     
/* 225 */     Object2LongMap<String> counters = ((ProfilerPathEntry)ObjectUtils.firstNonNull(new ProfilerPathEntry[] { (ProfilerPathEntry)this.entries.get(path), EMPTY })).getCounters();
/* 226 */     counters.forEach((id, value) -> 
/* 227 */         indentLine(builder, depth)
/* 228 */         .append('#')
/* 229 */         .append(id)
/* 230 */         .append(' ')
/* 231 */         .append(value)
/* 232 */         .append('/')
/* 233 */         .append(value / this.tickDuration)
/* 234 */         .append('\n'));
/*     */ 
/*     */     
/* 237 */     if (results.size() < 3) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 242 */     for (int i = 1; i < results.size(); i++) {
/* 243 */       ResultField result = (ResultField)results.get(i);
/*     */       
/* 245 */       indentLine(builder, depth)
/* 246 */         .append(result.name)
/* 247 */         .append('(')
/* 248 */         .append(result.count)
/* 249 */         .append('/')
/* 250 */         .append(String.format(Locale.ROOT, "%.0f", new Object[] { Float.valueOf((float)result.count / this.tickDuration)
/* 251 */             })).append(')')
/* 252 */         .append(" - ")
/* 253 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(result.percentage) })).append("%/")
/* 254 */         .append(String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(result.globalPercentage) })).append("%\n");
/*     */       
/* 256 */       if (!"unspecified".equals(result.name)) {
/*     */         try {
/* 258 */           appendProfilerResults(depth + 1, path + "\036" + path, builder);
/* 259 */         } catch (Exception e) {
/* 260 */           builder.append("[[ EXCEPTION ").append(e).append(" ]]");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void appendCounterResults(int depth, String name, CounterCollector result, int tickspan, StringBuilder builder) {
/* 267 */     indentLine(builder, depth)
/* 268 */       .append(name).append(" total:")
/* 269 */       .append(result.selfValue).append('/')
/* 270 */       .append(result.totalValue).append(" average: ")
/* 271 */       .append(result.selfValue / tickspan)
/* 272 */       .append('/')
/* 273 */       .append(result.totalValue / tickspan)
/* 274 */       .append('\n');
/* 275 */     result.children.entrySet().stream().sorted(COUNTER_ENTRY_COMPARATOR).forEach(e -> appendCounterResults(depth + 1, (String)e.getKey(), (CounterCollector)e.getValue(), tickspan, builder));
/*     */   }
/*     */   
/*     */   private void appendCounters(Map<String, CounterCollector> counters, StringBuilder builder, int tickspan) {
/* 279 */     counters.forEach((counter, counterRoot) -> {
/* 280 */           builder.append("-- Counter: ").append(counter).append(" --\n");
/* 281 */           appendCounterResults(0, "root", (CounterCollector)counterRoot.children.get("root"), tickspan, builder);
/* 282 */           builder.append("\n\n");
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 288 */   public int getTickDuration() { return this.tickDuration; }
/*     */   
/*     */   private static class CounterCollector
/*     */   {
/*     */     private long selfValue;
/*     */     private long totalValue;
/* 294 */     private final Map<String, CounterCollector> children = Maps.newHashMap();
/*     */     
/*     */     public void addValue(Iterator<String> path, long value) {
/* 297 */       this.totalValue += value;
/* 298 */       if (!path.hasNext()) {
/* 299 */         this.selfValue += value;
/*     */       } else {
/* 301 */         ((CounterCollector)this.children.computeIfAbsent((String)path.next(), k -> new CounterCollector())).addValue(path, value);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\FilledProfileResults.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */