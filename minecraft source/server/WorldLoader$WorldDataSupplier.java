package net.minecraft.server;

@FunctionalInterface
public interface WorldDataSupplier<D> {
  WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext paramDataLoadContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\WorldLoader$WorldDataSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */