package net.minecraft.server.packs.repository;

import java.util.function.Consumer;

@FunctionalInterface
public interface RepositorySource {
  void loadPacks(Consumer<Pack> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\packs\repository\RepositorySource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */