package net.minecraft.server.jsonrpc.internalapi;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface MinecraftExecutorService {
  <V> CompletableFuture<V> submit(Supplier<V> paramSupplier);
  
  CompletableFuture<Void> submit(Runnable paramRunnable);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftExecutorService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */