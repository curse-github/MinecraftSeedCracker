package net.minecraft.world.level.storage.loot;

import net.minecraft.resources.ResourceKey;

@FunctionalInterface
public interface Validator<T> {
  void run(ValidationContext paramValidationContext, ResourceKey<T> paramResourceKey, T paramT);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootDataType$Validator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */