package net.minecraft.world.entity;

import java.util.function.Consumer;

class null implements InsideBlockEffectApplier {
  public void apply(InsideBlockEffectType type) {}
  
  public void runBefore(InsideBlockEffectType type, Consumer<Entity> effect) {}
  
  public void runAfter(InsideBlockEffectType type, Consumer<Entity> effect) {}
}


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\InsideBlockEffectApplier$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */