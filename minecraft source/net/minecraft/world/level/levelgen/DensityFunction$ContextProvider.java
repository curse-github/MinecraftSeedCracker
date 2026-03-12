package net.minecraft.world.level.levelgen;

public interface ContextProvider {
  DensityFunction.FunctionContext forIndex(int paramInt);
  
  void fillAllDirectly(double[] paramArrayOfDouble, DensityFunction paramDensityFunction);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\DensityFunction$ContextProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */