package net.minecraft.core;

import net.minecraft.data.worldgen.BootstrapContext;

@FunctionalInterface
public interface RegistryBootstrap<T> {
  void run(BootstrapContext<T> paramBootstrapContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\core\RegistrySetBuilder$RegistryBootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */