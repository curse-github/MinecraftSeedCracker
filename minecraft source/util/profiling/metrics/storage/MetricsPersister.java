/*     */ package net.minecraft.util.profiling.metrics.storage;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.time.ZoneId;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.CsvOutput;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class MetricsPersister
/*     */ {
/*  30 */   public static final Path PROFILING_RESULTS_DIR = Paths.get("debug/profiling", new String[0]);
/*     */   public static final String METRICS_DIR_NAME = "metrics";
/*     */   public static final String DEVIATIONS_DIR_NAME = "deviations";
/*     */   public static final String PROFILING_RESULT_FILENAME = "profiling.txt";
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final String rootFolderName;
/*     */ 
/*     */   
/*  39 */   public MetricsPersister(String rootFolderName) { this.rootFolderName = rootFolderName; }
/*     */ 
/*     */   
/*     */   public Path saveReports(Set<MetricSampler> samplers, Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler, ProfileResults profilerResults) {
/*     */     try {
/*  44 */       Files.createDirectories(PROFILING_RESULTS_DIR, new java.nio.file.attribute.FileAttribute[0]);
/*  45 */     } catch (IOException e) {
/*  46 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */     
/*     */     try {
/*  50 */       Path tempDir = Files.createTempDirectory("minecraft-profiling", new java.nio.file.attribute.FileAttribute[0]);
/*  51 */       tempDir.toFile().deleteOnExit();
/*     */       
/*  53 */       Files.createDirectories(PROFILING_RESULTS_DIR, new java.nio.file.attribute.FileAttribute[0]);
/*  54 */       Path workingDir = tempDir.resolve(this.rootFolderName);
/*  55 */       Path metricsDir = workingDir.resolve("metrics");
/*     */       
/*  57 */       saveMetrics(samplers, metricsDir);
/*     */       
/*  59 */       if (!deviationsBySampler.isEmpty()) {
/*  60 */         saveDeviations(deviationsBySampler, workingDir.resolve("deviations"));
/*     */       }
/*     */       
/*  63 */       saveProfilingTaskExecutionResult(profilerResults, workingDir);
/*  64 */       return tempDir;
/*  65 */     } catch (IOException e) {
/*  66 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveMetrics(Set<MetricSampler> samplers, Path dir) {
/*  71 */     if (samplers.isEmpty()) {
/*  72 */       throw new IllegalArgumentException("Expected at least one sampler to persist");
/*     */     }
/*     */     
/*  75 */     Map<MetricCategory, List<MetricSampler>> samplersByCategory = (Map)samplers.stream().collect(Collectors.groupingBy(MetricSampler::getCategory));
/*  76 */     samplersByCategory.forEach((category, samplersInCategory) -> saveCategory(category, samplersInCategory, dir));
/*     */   }
/*     */   
/*     */   private void saveCategory(MetricCategory category, List<MetricSampler> samplers, Path dir) {
/*  80 */     Path file = dir.resolve(Util.sanitizeName(category.getDescription(), Identifier::validPathChar) + ".csv");
/*     */     
/*  82 */     writer = null;
/*     */     try {
/*  84 */       Files.createDirectories(file.getParent(), new java.nio.file.attribute.FileAttribute[0]);
/*  85 */       writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*     */       
/*  87 */       CsvOutput.Builder csvBuilder = CsvOutput.builder();
/*  88 */       csvBuilder.addColumn("@tick");
/*  89 */       for (MetricSampler sampler : samplers) {
/*  90 */         csvBuilder.addColumn(sampler.getName());
/*     */       }
/*  92 */       CsvOutput csvOutput = csvBuilder.build(writer);
/*     */ 
/*     */ 
/*     */       
/*  96 */       List<MetricSampler.SamplerResult> results = (List)samplers.stream().map(MetricSampler::result).collect(Collectors.toList());
/*     */       
/*  98 */       int firstTick = results.stream().mapToInt(MetricSampler.SamplerResult::getFirstTick).summaryStatistics().getMin();
/*  99 */       int lastTick = results.stream().mapToInt(MetricSampler.SamplerResult::getLastTick).summaryStatistics().getMax();
/*     */       
/* 101 */       for (int tick = firstTick; tick <= lastTick; tick++) {
/* 102 */         int finalTick = tick;
/*     */         
/* 104 */         Stream<String> valuesStream = results.stream().map(it -> String.valueOf(it.valueAtTick(finalTick)));
/*     */         
/* 106 */         Object[] row = Stream.concat(Stream.of(String.valueOf(tick)), valuesStream).toArray(x$0 -> new String[x$0]);
/* 107 */         csvOutput.writeRow(row);
/*     */       } 
/*     */       
/* 110 */       LOGGER.info("Flushed metrics to {}", file);
/* 111 */     } catch (Exception e) {
/* 112 */       LOGGER.error("Could not save profiler results to {}", file, e);
/*     */     } finally {
/* 114 */       IOUtils.closeQuietly(writer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void saveDeviations(Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler, Path directory) {
/* 119 */     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss.SSS", Locale.UK).withZone(ZoneId.systemDefault());
/* 120 */     deviationsBySampler.forEach((sampler, deviations) -> 
/* 121 */         deviations.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   private void saveProfilingTaskExecutionResult(ProfileResults results, Path directory) { results.saveResults(directory.resolve("profiling.txt")); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\storage\MetricsPersister.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */