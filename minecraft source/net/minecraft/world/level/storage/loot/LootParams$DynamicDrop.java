package net.minecraft.world.level.storage.loot;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface DynamicDrop {
  void add(Consumer<ItemStack> paramConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\LootParams$DynamicDrop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */