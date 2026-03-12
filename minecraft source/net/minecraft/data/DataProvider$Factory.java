package net.minecraft.data;

@FunctionalInterface
public interface Factory<T extends DataProvider> {
  T create(PackOutput paramPackOutput);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\DataProvider$Factory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */