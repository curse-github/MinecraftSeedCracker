package net.minecraft.data.loot;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

@FunctionalInterface
public interface LootTableSubProvider {
  void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> paramBiConsumer);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\data\loot\LootTableSubProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */