package net.minecraft.server.packs.resources;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface PreparationBarrier {
  <T> CompletableFuture<T> wait(T paramT);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\resources\PreparableReloadListener$PreparationBarrier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */