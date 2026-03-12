package net.minecraft.world.item.enchantment.effects;

import net.minecraft.util.RandomSource;

@FunctionalInterface
interface CoordinateSource {
  double getCoordinate(double paramDouble1, double paramDouble2, float paramFloat, RandomSource paramRandomSource);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSourceType$CoordinateSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */