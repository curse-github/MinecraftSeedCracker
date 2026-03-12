package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.util.RandomSource;

abstract class FloorRoomCollection {
  public abstract String get1x1(RandomSource paramRandomSource);
  
  public abstract String get1x1Secret(RandomSource paramRandomSource);
  
  public abstract String get1x2SideEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2FrontEntrance(RandomSource paramRandomSource, boolean paramBoolean);
  
  public abstract String get1x2Secret(RandomSource paramRandomSource);
  
  public abstract String get2x2(RandomSource paramRandomSource);
  
  public abstract String get2x2Secret(RandomSource paramRandomSource);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$FloorRoomCollection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */