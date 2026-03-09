package net.minecraft.util.debugchart;

public interface SampleStorage {
  int capacity();
  
  int size();
  
  long get(int paramInt);
  
  long get(int paramInt1, int paramInt2);
  
  void reset();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debugchart\SampleStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */