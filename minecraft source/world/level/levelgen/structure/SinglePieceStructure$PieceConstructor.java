package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.WorldgenRandom;

@FunctionalInterface
public interface PieceConstructor {
  StructurePiece construct(WorldgenRandom paramWorldgenRandom, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\SinglePieceStructure$PieceConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */