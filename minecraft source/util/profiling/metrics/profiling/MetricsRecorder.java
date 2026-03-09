package net.minecraft.util.profiling.metrics.profiling;

import net.minecraft.util.profiling.ProfilerFiller;

public interface MetricsRecorder {
  void end();
  
  void cancel();
  
  void startTick();
  
  boolean isRecording();
  
  ProfilerFiller getProfiler();
  
  void endTick();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\profiling\metrics\profiling\MetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */