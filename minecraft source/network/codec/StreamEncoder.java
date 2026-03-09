package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamEncoder<O, T> {
  void encode(O paramO, T paramT);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\codec\StreamEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */