package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.RandomSource;

interface Formula {
  int calculateNewCount(RandomSource paramRandomSource, int paramInt1, int paramInt2);
  
  ApplyBonusCount.FormulaType getType();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount$Formula.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */