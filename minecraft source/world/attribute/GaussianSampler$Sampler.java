package net.minecraft.world.attribute;

@FunctionalInterface
public interface Sampler<V> {
  V get(int paramInt1, int paramInt2, int paramInt3);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\GaussianSampler$Sampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */