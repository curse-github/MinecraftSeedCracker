package net.minecraft.commands.execution.tasks;

import net.minecraft.commands.execution.CommandQueueEntry;
import net.minecraft.commands.execution.Frame;

@FunctionalInterface
public interface TaskProvider<T, P> {
  CommandQueueEntry<T> create(Frame paramFrame, P paramP);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\commands\execution\tasks\ContinuationTask$TaskProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */