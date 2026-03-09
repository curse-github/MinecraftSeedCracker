package net.minecraft.world.level.biome;

interface DistanceMetric<T> {
  long distance(Climate.RTree.Node<T> paramNode, long[] paramArrayOfLong);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\biome\Climate$DistanceMetric.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */