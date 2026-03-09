package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface TickAccess<T> {
  void schedule(ScheduledTick<T> paramScheduledTick);
  
  boolean hasScheduledTick(BlockPos paramBlockPos, T paramT);
  
  int count();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\TickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */