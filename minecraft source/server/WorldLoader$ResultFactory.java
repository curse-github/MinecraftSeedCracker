package net.minecraft.server;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.packs.resources.CloseableResourceManager;

@FunctionalInterface
public interface ResultFactory<D, R> {
  R create(CloseableResourceManager paramCloseableResourceManager, ReloadableServerResources paramReloadableServerResources, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, D paramD);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\WorldLoader$ResultFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */