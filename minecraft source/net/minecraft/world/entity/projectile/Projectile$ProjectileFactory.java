package net.minecraft.world.entity.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ProjectileFactory<T extends Projectile> {
  T create(ServerLevel paramServerLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack);
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\Projectile$ProjectileFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */