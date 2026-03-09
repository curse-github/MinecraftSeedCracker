package net.minecraft.world.level.storage.loot.entries;

import java.util.List;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@FunctionalInterface
public interface EntryConstructor {
  LootPoolSingletonContainer build(int paramInt1, int paramInt2, List<LootItemCondition> paramList1, List<LootItemFunction> paramList2);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$EntryConstructor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */