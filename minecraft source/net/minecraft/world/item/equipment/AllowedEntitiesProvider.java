package net.minecraft.world.item.equipment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface AllowedEntitiesProvider {
  HolderSet<EntityType<?>> get(HolderGetter<EntityType<?>> paramHolderGetter);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\equipment\AllowedEntitiesProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */