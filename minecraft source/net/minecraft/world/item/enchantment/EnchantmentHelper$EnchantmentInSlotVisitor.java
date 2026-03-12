package net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;

@FunctionalInterface
interface EnchantmentInSlotVisitor {
  void accept(Holder<Enchantment> paramHolder, int paramInt, EnchantedItemInUse paramEnchantedItemInUse);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\EnchantmentHelper$EnchantmentInSlotVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */