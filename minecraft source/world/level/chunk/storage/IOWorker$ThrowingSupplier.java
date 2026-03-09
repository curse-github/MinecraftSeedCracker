package net.minecraft.world.level.chunk.storage;

@FunctionalInterface
interface ThrowingSupplier<T> {
  T get() throws Exception;
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\chunk\storage\IOWorker$ThrowingSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */