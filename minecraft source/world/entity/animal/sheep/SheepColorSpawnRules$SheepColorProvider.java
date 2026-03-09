package net.minecraft.world.entity.animal.sheep;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;

@FunctionalInterface
interface SheepColorProvider {
  DyeColor get(RandomSource paramRandomSource);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\animal\sheep\SheepColorSpawnRules$SheepColorProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */