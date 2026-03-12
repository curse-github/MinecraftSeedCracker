package net.minecraft.util.profiling.metrics;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.util.profiling.ProfileCollector;

public interface MetricsSamplerProvider {
  Set<MetricSampler> samplers(Supplier<ProfileCollector> paramSupplier);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\MetricsSamplerProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */