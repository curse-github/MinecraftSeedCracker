package net.minecraft.data;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface UpdateFunction {
  CompletableFuture<?> update(CachedOutput paramCachedOutput);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\HashCache$UpdateFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */