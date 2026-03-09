package net.minecraft.world.level.timers;

import com.mojang.serialization.MapCodec;

public interface TimerCallback<T> {
  void handle(T paramT, TimerQueue<T> paramTimerQueue, long paramLong);
  
  MapCodec<? extends TimerCallback<T>> codec();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\timers\TimerCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */