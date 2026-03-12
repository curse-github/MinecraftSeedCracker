package net.minecraft.world.ticks;

@FunctionalInterface
interface PosAndContainerConsumer<T> {
  void accept(long paramLong, LevelChunkTicks<T> paramLevelChunkTicks);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\ticks\LevelTicks$PosAndContainerConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */