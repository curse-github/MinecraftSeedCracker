/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.util.profiling.ActiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class ProfilerSamplerAdapter {
/* 15 */   private final Set<String> previouslyFoundSamplerNames = new ObjectOpenHashSet();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<MetricSampler> newSamplersFoundInProfiler(Supplier<ProfileCollector> profiler) {
/* 21 */     Set<MetricSampler> newSamplers = (Set)((ProfileCollector)profiler.get()).getChartedPaths().stream().filter(pathAndCategory -> !this.previouslyFoundSamplerNames.contains(pathAndCategory.getLeft())).map(pathAndCategory -> samplerForProfilingPath(profiler, (String)pathAndCategory.getLeft(), (MetricCategory)pathAndCategory.getRight())).collect(Collectors.toSet());
/*    */     
/* 23 */     for (MetricSampler sampler : newSamplers) {
/* 24 */       this.previouslyFoundSamplerNames.add(sampler.getName());
/*    */     }
/*    */     
/* 27 */     return newSamplers;
/*    */   }
/*    */   
/*    */   private static MetricSampler samplerForProfilingPath(Supplier<ProfileCollector> profiler, String profilerPath, MetricCategory category) {
/* 31 */     return MetricSampler.create(profilerPath, category, () -> {
/* 32 */           ActiveProfiler.PathEntry entry = ((ProfileCollector)profiler.get()).getEntry(profilerPath);
/* 33 */           return (entry == null) ? 0.0D : (entry.getMaxDuration() / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\ProfilerSamplerAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */