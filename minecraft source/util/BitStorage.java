package net.minecraft.util;

import java.util.function.IntConsumer;

public interface BitStorage {
  int getAndSet(int paramInt1, int paramInt2);
  
  void set(int paramInt1, int paramInt2);
  
  int get(int paramInt);
  
  long[] getRaw();
  
  int getSize();
  
  int getBits();
  
  void getAll(IntConsumer paramIntConsumer);
  
  void unpack(int[] paramArrayOfInt);
  
  BitStorage copy();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\BitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */