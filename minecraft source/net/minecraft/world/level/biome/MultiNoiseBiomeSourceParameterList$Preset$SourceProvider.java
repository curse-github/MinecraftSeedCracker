package net.minecraft.world.level.biome;

import java.util.function.Function;
import net.minecraft.resources.ResourceKey;

@FunctionalInterface
interface SourceProvider {
  <T> Climate.ParameterList<T> apply(Function<ResourceKey<Biome>, T> paramFunction);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\MultiNoiseBiomeSourceParameterList$Preset$SourceProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */