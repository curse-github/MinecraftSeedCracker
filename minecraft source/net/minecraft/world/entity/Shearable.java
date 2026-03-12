package net.minecraft.world.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public interface Shearable {
  void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack);
  
  boolean readyForShearing();
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\Shearable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */