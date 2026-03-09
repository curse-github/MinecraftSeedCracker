package net.minecraft.world.entity;

import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityFactory<T extends Entity> {
  T create(EntityType<T> paramEntityType, Level paramLevel);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntityType$EntityFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */