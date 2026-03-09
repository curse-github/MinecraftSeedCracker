package net.minecraft.resources;

@FunctionalInterface
interface LoadingFunction {
  void apply(RegistryDataLoader.Loader<?> paramLoader, RegistryOps.RegistryInfoLookup paramRegistryInfoLookup);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\resources\RegistryDataLoader$LoadingFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */