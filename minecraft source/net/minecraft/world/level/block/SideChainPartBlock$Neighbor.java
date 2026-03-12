package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public interface Neighbor {
  BlockPos pos();
  
  boolean isConnectable();
  
  boolean isUnconnectableOrChainEnd();
  
  boolean connectsTowards(SideChainPart paramSideChainPart);
  
  default void connectToTheRight() {}
  
  default void connectToTheLeft() {}
  
  default void disconnectFromRight() {}
  
  default void disconnectFromLeft() {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\SideChainPartBlock$Neighbor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */