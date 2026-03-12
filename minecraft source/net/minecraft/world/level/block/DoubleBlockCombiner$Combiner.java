package net.minecraft.world.level.block;

public interface Combiner<S, T> {
  T acceptDouble(S paramS1, S paramS2);
  
  T acceptSingle(S paramS);
  
  T acceptNone();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\DoubleBlockCombiner$Combiner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */