package net.minecraft.world.level.entity;

public interface LevelCallback<T> {
  void onCreated(T paramT);
  
  void onDestroyed(T paramT);
  
  void onTickingStart(T paramT);
  
  void onTickingEnd(T paramT);
  
  void onTrackingStart(T paramT);
  
  void onTrackingEnd(T paramT);
  
  void onSectionChange(T paramT);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\entity\LevelCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */