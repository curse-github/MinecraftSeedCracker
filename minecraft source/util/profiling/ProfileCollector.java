package net.minecraft.util.profiling;

import java.util.Set;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;

public interface ProfileCollector extends ProfilerFiller {
  ProfileResults getResults();
  
  ActiveProfiler.PathEntry getEntry(String paramString);
  
  Set<Pair<String, MetricCategory>> getChartedPaths();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\ProfileCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */