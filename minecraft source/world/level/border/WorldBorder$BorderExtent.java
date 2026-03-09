package net.minecraft.world.level.border;

import net.minecraft.world.phys.shapes.VoxelShape;

interface BorderExtent {
  double getMinX(float paramFloat);
  
  double getMaxX(float paramFloat);
  
  double getMinZ(float paramFloat);
  
  double getMaxZ(float paramFloat);
  
  double getSize();
  
  double getLerpSpeed();
  
  long getLerpTime();
  
  double getLerpTarget();
  
  BorderStatus getStatus();
  
  void onAbsoluteMaxSizeChange();
  
  void onCenterChange();
  
  BorderExtent update();
  
  VoxelShape getCollisionShape();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\border\WorldBorder$BorderExtent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */