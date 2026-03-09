package net.minecraft.world.level.storage;

public interface ValueOutputList {
  ValueOutput addChild();
  
  void discardLast();
  
  boolean isEmpty();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\ValueOutput$ValueOutputList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */