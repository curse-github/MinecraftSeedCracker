package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootPoolEntry {
  int getWeight(float paramFloat);
  
  void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */