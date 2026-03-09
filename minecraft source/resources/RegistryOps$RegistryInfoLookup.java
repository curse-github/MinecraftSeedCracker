package net.minecraft.resources;

import java.util.Optional;
import net.minecraft.core.Registry;

public interface RegistryInfoLookup {
  <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryOps$RegistryInfoLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */