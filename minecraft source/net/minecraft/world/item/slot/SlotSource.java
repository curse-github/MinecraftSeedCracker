package net.minecraft.world.item.slot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface SlotSource extends LootContextUser {
  MapCodec<? extends SlotSource> codec();
  
  SlotCollection provide(LootContext paramLootContext);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\slot\SlotSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */